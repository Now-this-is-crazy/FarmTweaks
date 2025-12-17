package powercyphe.farmtweaks.mixin.bonemealable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import powercyphe.farmtweaks.FarmTweaksUtil;

@Mixin(CactusBlock.class)
public class CactusBlockMixin implements BonemealableBlock {

    @Shadow @Final public static IntegerProperty AGE;

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return FarmTweaksUtil.canBonemeal(state.getBlock());
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return FarmTweaksUtil.canBonemeal(state.getBlock());
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
        CactusBlock block = (CactusBlock) (Object) this;

        if (FarmTweaksUtil.canBonemeal(state.getBlock())) {
            BlockPos blockPos = pos.above();
            if (world.isEmptyBlock(blockPos)) {
                int i = 1;
                int j = state.getValue(AGE);

                while (world.getBlockState(pos.below(i)).is(block)) {
                    ++i;
                    if (i == 3 && j == 15) {
                        return;
                    }
                }

                if (j == 8 && block.defaultBlockState().canSurvive(world, pos.above())) {
                    double d = i >= 3 ? 0.25 : 0.1;
                    if (random.nextDouble() <= d) {
                        world.setBlockAndUpdate(blockPos, Blocks.CACTUS_FLOWER.defaultBlockState());
                    }
                } else if (j == 15 && i < 3) {
                    world.setBlockAndUpdate(blockPos, block.defaultBlockState());
                    BlockState blockState = state.setValue(AGE, 0);
                    world.setBlock(pos, blockState, 260);
                    world.neighborChanged(blockState, blockPos, block, null, false);
                }

                if (j < 15) {
                    world.setBlock(pos, state.setValue(AGE, j + 1), 260);
                }

            }
        }
    }

}
