package powercyphe.farmtweaks.mixin.crops;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import powercyphe.farmtweaks.FarmTweaksConfig;
import powercyphe.farmtweaks.FarmTweaksUtil;

import java.util.Arrays;
import java.util.List;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(method = "afterBreak", at = @At("HEAD"))
    private void afterBreakMixin(World world, PlayerEntity player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack tool, CallbackInfo ci) {
            if (state.getBlock() instanceof CropBlock) {
                if (((CropBlock) state.getBlock()).isMature(state)) {
                    FarmTweaksUtil.farmtweaks$dropExp(world, pos);
                }
            }
    }
    @Inject(method = "onBreak", at = @At("HEAD"))
    private void onBreakMixin(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<BlockState> cir) {
            if ((state.isOf(Blocks.MELON) || state.isOf(Blocks.PUMPKIN)) && !player.isCreative()) {
                for (Direction direction : Direction.values()) {
                    if (direction.getAxis() != Direction.Axis.Y) {
                        BlockPos blockPos = pos.offset(direction.getOpposite());
                        if (world.getBlockState(blockPos).getBlock() instanceof AttachedStemBlock) {
                            if (world.getBlockState(blockPos).get(AttachedStemBlock.FACING) == direction) {
                                FarmTweaksUtil.farmtweaks$dropExp(world, pos);

                            }
                        }
                    }
                }
        }

    }
}