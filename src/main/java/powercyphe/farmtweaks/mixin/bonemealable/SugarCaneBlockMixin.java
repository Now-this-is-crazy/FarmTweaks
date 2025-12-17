package powercyphe.farmtweaks.mixin.bonemealable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import powercyphe.farmtweaks.FarmTweaksUtil;


@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin implements BonemealableBlock {

    @Shadow @Final public static IntegerProperty AGE;

    @Override
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState state) {
        return FarmTweaksUtil.canBonemeal(state.getBlock());
    }

    @Override
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState state) {
            SugarCaneBlock block = (SugarCaneBlock) (Object) this;
            while (world.getBlockState(pos).is(Blocks.SUGAR_CANE)) {
                pos = pos.above();
            }
            pos = pos.below();
            state = world.getBlockState(pos);
            if (world.isEmptyBlock(pos.above())) {
                int i;
                for (i = 1; world.getBlockState(pos.below(i)).is(block); ++i) {
                }

                if (i < 3) {
                    int j = (Integer)state.getValue(AGE);
                    if (j == 15) {
                        world.setBlockAndUpdate(pos.above(), block.defaultBlockState());
                        world.setBlock(pos, (BlockState)state.setValue(AGE, 0), 4);
                    } else {
                        world.setBlock(pos, (BlockState)state.setValue(AGE, Mth.clamp(j + Mth.nextInt(RandomSource.create(), 3, 5), 0, 15)), 4);
                    }
                }
        }
    }
}
