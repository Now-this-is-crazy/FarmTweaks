package powercyphe.farmtweaks.mixin.crops;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import powercyphe.farmtweaks.FarmTweaksUtil;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "playerDestroy", at = @At("HEAD"))
    private void afterBreakMixin(Level world, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
            if (state.getBlock() instanceof CropBlock) {
                if (((CropBlock) state.getBlock()).isMaxAge(state)) {
                    FarmTweaksUtil.farmtweaks$dropExp(world, pos);
                }
            }
    }
    @Inject(method = "playerWillDestroy", at = @At("HEAD"))
    private void onBreakMixin(Level world, BlockPos pos, BlockState state, Player player, CallbackInfoReturnable<BlockState> cir) {
            if ((state.is(Blocks.MELON) || state.is(Blocks.PUMPKIN)) && !player.isCreative()) {
                for (Direction direction : Direction.values()) {
                    if (direction.getAxis() != Direction.Axis.Y) {
                        BlockPos blockPos = pos.relative(direction.getOpposite());
                        if (world.getBlockState(blockPos).getBlock() instanceof AttachedStemBlock) {
                            if (world.getBlockState(blockPos).getValue(AttachedStemBlock.FACING) == direction) {
                                FarmTweaksUtil.farmtweaks$dropExp(world, pos);

                            }
                        }
                    }
                }
        }

    }
}