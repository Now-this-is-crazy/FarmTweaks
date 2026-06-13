package powercyphe.farmtweaks.mixin.path;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import powercyphe.farmtweaks.FarmTweaksUtil;

import java.util.Map;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin {
    @Shadow
    public abstract InteractionResult useOn(UseOnContext context);

    @Shadow
    @Final
    protected static Map<Block, BlockState> FLATTENABLES;
    @Unique
    private boolean below = false;

    @Inject(method = "useOn", at = @At("RETURN"), cancellable = true)
    private void farmtweaks$smartPath(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (!this.below && FarmTweaksUtil.useSmartPathMaking()) {
            Level level = context.getLevel();
            BlockPos pos = context.getClickedPos();
            BlockState state = level.getBlockState(pos);

            if (state.canBeReplaced()) {
                BlockPos bPos = pos.below();
                BlockState bState = level.getBlockState(bPos);

                if (FLATTENABLES.containsKey(bState.getBlock())) {
                    level.destroyBlock(pos, true, context.getPlayer());

                    this.below = true;
                    var result = this.useOn(new UseOnContext(context.getLevel(), context.getPlayer(), context.getHand(), context.getItemInHand(),
                            new BlockHitResult(context.getClickLocation().subtract(0, -1, 0), context.getClickedFace(),
                                    bPos, context.isInside())));
                    if (result != InteractionResult.PASS) {
                        cir.setReturnValue(result);
                    }

                    this.below = false;
                }
            }
        }
    }
}
