package powercyphe.farmtweaks.event;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import powercyphe.farmtweaks.mixin.accessor.IntegerPropertyAccessor;
import powercyphe.farmtweaks.FarmTweaks;
import powercyphe.farmtweaks.FarmTweaksUtil;

public class AlternateHoeUseEvent implements UseBlockCallback {

    @Override
    public InteractionResult interact(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getItemInHand(hand);

        if (stack.is(FarmTweaks.ALLOWS_ALTERNATE_HOE_USE)) {
            BlockPos blockPos = hitResult.getBlockPos();
            BlockState state = world.getBlockState(blockPos);
            Block block = state.getBlock();

            if (!world.isClientSide()) {
                if (FarmTweaksUtil.allowAltHoeUse(state.getBlock())) {

                    // Get Age Property
                    IntegerProperty ageProperty = null;
                    for (Property<?> property : state.getProperties()) {
                        if (property.getName().equalsIgnoreCase("age") && property instanceof IntegerProperty intProperty) {
                            ageProperty = intProperty;
                            break;
                        }
                    }


                    if (ageProperty != null) {
                        IntegerPropertyAccessor accessor = (IntegerPropertyAccessor) (Object) ageProperty;

                        if (state.getValue(ageProperty) >= accessor.farmtweaks$getMax() &&
                                !(state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF) &&
                                        state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) != DoubleBlockHalf.LOWER)) {
                            block.playerDestroy(world, player, blockPos, state, world.getBlockEntity(blockPos), stack);
                            world.setBlockAndUpdate(blockPos, state.setValue(ageProperty, accessor.farmtweaks$getMin()));
                            world.gameEvent(player, GameEvent.BLOCK_DESTROY, blockPos);

                            world.playSound(null, blockPos, SoundEvents.HOE_TILL, SoundSource.BLOCKS);

                            BlockParticleOption particleEffect = new BlockParticleOption(ParticleTypes.BLOCK, state);
                            ((ServerLevel) world).sendParticles(particleEffect, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 14, 0, 0, 0, 1);
                            ((ServerLevel) world).sendParticles(ParticleTypes.SWEEP_ATTACK, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0, 0, 0, 0, 1);

                            player.swing(hand, true);
                            stack.hurtAndBreak(1, player, hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND);
                            return InteractionResult.SUCCESS;
                        }
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }
}
