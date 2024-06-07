package powercyphe.farmtweaks.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropBlock;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import powercyphe.farmtweaks.util.ConfigUtil;

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
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        if (!world.isClient && blockState.getBlock() instanceof CropBlock cropBlock) {
            if (ConfigUtil.allowAltHoeUse()) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) context.getPlayer();
                if (cropBlock.isMature(blockState)) {
                    cropBlock.afterBreak(world, serverPlayer, blockPos, blockState, null, context.getStack());
                    world.setBlockState(blockPos, cropBlock.getDefaultState());
                    serverPlayer.swingHand(context.getHand());
                    world.playSound(null, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS);
                    world.addParticle(ParticleTypes.SWEEP_ATTACK, blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5, 0, 0, 0);
                    context.getStack().damage(1, serverPlayer, (p) -> {
                        p.sendToolBreakStatus(context.getHand());
                    });
                    cir.setReturnValue(ActionResult.SUCCESS);
                }
            }
        }
    }


}
