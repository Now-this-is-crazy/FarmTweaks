package powercyphe.farmtweaks.mixin.bonemealable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.Fertilizable;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import powercyphe.farmtweaks.FarmTweaksUtil;

@Mixin(CactusBlock.class)
public class CactusBlockMixin implements Fertilizable {

    @Shadow @Final public static IntProperty AGE;

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return FarmTweaksUtil.canBonemeal(state.getBlock());
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return FarmTweaksUtil.canBonemeal(state.getBlock());
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        CactusBlock block = (CactusBlock) (Object) this;

        if (FarmTweaksUtil.canBonemeal(state.getBlock())) {
            BlockPos blockPos = pos.up();
            if (world.isAir(blockPos)) {
                int i = 1;
                int j = state.get(AGE);

                while (world.getBlockState(pos.down(i)).isOf(block)) {
                    ++i;
                    if (i == 3 && j == 15) {
                        return;
                    }
                }

                if (j == 8 && block.getDefaultState().canPlaceAt(world, pos.up())) {
                    double d = i >= 3 ? 0.25 : 0.1;
                    if (random.nextDouble() <= d) {
                        world.setBlockState(blockPos, Blocks.CACTUS_FLOWER.getDefaultState());
                    }
                } else if (j == 15 && i < 3) {
                    world.setBlockState(blockPos, block.getDefaultState());
                    BlockState blockState = state.with(AGE, 0);
                    world.setBlockState(pos, blockState, 260);
                    world.updateNeighbor(blockState, blockPos, block, null, false);
                }

                if (j < 15) {
                    world.setBlockState(pos, state.with(AGE, j + 1), 260);
                }

            }
        }
    }

}
