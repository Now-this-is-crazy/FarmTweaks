package dev.powercyphe.farmtweaks.mixin.path;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.BlockTransformer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.component.BlockTransformers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import dev.powercyphe.farmtweaks.util.FarmTweaksUtil;

@Mixin(BlockTransformer.class)
public abstract class BlockTransformerMixin {

    @Shadow
    public abstract InteractionResult transformBlock(UseOnContext context);

    @Unique
    private boolean below = false;

    @ModifyReturnValue(method = "transformBlock", at = @At("RETURN"))
    private InteractionResult farmtweaks$smartPath(InteractionResult original, UseOnContext context) {
        BlockTransformer transformer = (BlockTransformer) (Object) this;

        if (FarmTweaksUtil.useSmartPathMaking() && original == InteractionResult.PASS && !this.below) {
            Level level = context.getLevel();
            var shovelTransform = level.registryAccess().get(BlockTransformers.SHOVEL);

            if (shovelTransform.isPresent() && transformer == shovelTransform.get().value()) {
                BlockPos pos = context.getClickedPos();
                BlockState state = level.getBlockState(pos);

                if (state.canBeReplaced()) {
                    this.below = true;

                    BlockPos bPos = pos.below();
                    UseOnContext secContext = new UseOnContext(
                            context.getPlayer(), context.getHand(),
                            new BlockHitResult(
                                    context.getClickLocation().subtract(0, 1, 0),
                                    context.getClickedFace(),
                                    bPos,
                                    false
                            )
                    );
                    InteractionResult result = this.transformBlock(secContext);

                    this.below = false;
                    return result;
                }
            }
        }
        return original;
    }
}
