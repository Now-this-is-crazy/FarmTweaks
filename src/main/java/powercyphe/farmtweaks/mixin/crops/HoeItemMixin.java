package powercyphe.farmtweaks.mixin.crops;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import powercyphe.farmtweaks.FarmTweaksUtil;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static net.minecraft.item.HoeItem.createTillAction;


@Mixin(HoeItem.class)
public class HoeItemMixin {

    @Shadow
    @Final
    protected static Map<Block, Pair<Predicate<ItemUsageContext>, Consumer<ItemUsageContext>>> TILLING_ACTIONS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addTillingActions(CallbackInfo ci) {
        TILLING_ACTIONS.put(Blocks.MYCELIUM, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())));
        TILLING_ACTIONS.put(Blocks.PODZOL, Pair.of(HoeItem::canTillFarmland, createTillAction(Blocks.FARMLAND.getDefaultState())));
    }


    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void useOnBlockMixin(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();

        BlockPos blockPos = context.getBlockPos();
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

                if (ageProperty != null && blockState.get(ageProperty) >= ((IntPropertyAccess) ageProperty).getMax()) {
                    block.afterBreak(world, player, blockPos, blockState, world.getBlockEntity(blockPos), context.getStack());
                    world.setBlockState(blockPos, block.getDefaultState());
                    world.emitGameEvent(player, GameEvent.BLOCK_DESTROY, blockPos);

                    world.playSound(null, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS);

                    BlockStateParticleEffect particleEffect = new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState);
                    ((ServerWorld)world).spawnParticles(particleEffect, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 14, 0, 0,0, 1);
                    ((ServerWorld)world).spawnParticles(ParticleTypes.SWEEP_ATTACK, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0, 0, 0,0, 1);

                    if (player != null) {
                        player.swingHand(context.getHand(), true);
                        context.getStack().damage(1, player, (p) -> {
                            p.sendToolBreakStatus(context.getHand());
                        });

                    }
                    cir.setReturnValue(ActionResult.SUCCESS);
                }
            }
        }
    }
}
