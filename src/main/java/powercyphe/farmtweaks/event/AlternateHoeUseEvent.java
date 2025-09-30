package powercyphe.farmtweaks.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import powercyphe.farmtweaks.FarmTweaks;
import powercyphe.farmtweaks.FarmTweaksUtil;
import powercyphe.farmtweaks.mixin.crops.IntPropertyAccess;

public class AlternateHoeUseEvent implements UseBlockCallback {

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getStackInHand(hand);

        if (stack.isIn(FarmTweaks.ALLOWS_ALTERNATE_HOE_USE)) {
            BlockPos blockPos = hitResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);
            Block block = blockState.getBlock();

            if (!world.isClient()) {
                if (FarmTweaksUtil.allowAltHoeUse(blockState.getBlock())) {
                    IntProperty ageProperty = null;
                    for (Property<?> property : blockState.getProperties()) {
                        if (property.getName().equalsIgnoreCase("age") && property instanceof IntProperty intProperty) {
                            ageProperty = intProperty;
                            break;
                        }
                    }

                    if (ageProperty != null && blockState.get(ageProperty) >= ((IntPropertyAccess) (Object) ageProperty).getMax() && !(blockState.getEntries().containsKey(Properties.DOUBLE_BLOCK_HALF) && blockState.get(Properties.DOUBLE_BLOCK_HALF) != DoubleBlockHalf.LOWER)) {
                        block.afterBreak(world, player, blockPos, blockState, world.getBlockEntity(blockPos), stack);
                        world.setBlockState(blockPos, blockState.with(ageProperty, ((IntPropertyAccess) (Object) ageProperty).getMin()));
                        world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, blockPos);

                        world.playSound(null, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS);

                        BlockStateParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState);
                        ((ServerWorld) world).spawnParticles(particleEffect, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 14, 0, 0, 0, 1);
                        ((ServerWorld) world).spawnParticles(ParticleTypes.SWEEP_ATTACK, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0, 0, 0, 0, 1);

                        player.swingHand(hand, true);
                        stack.damage(1, player, hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                        return ActionResult.SUCCESS;
                    }
                }
            }
        }
        return ActionResult.PASS;
    }
}
