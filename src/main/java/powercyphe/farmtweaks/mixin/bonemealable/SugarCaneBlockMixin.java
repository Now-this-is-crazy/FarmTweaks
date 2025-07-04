package powercyphe.farmtweaks.mixin.bonemealable;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Fertilizable;
import net.minecraft.block.SugarCaneBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import powercyphe.farmtweaks.FarmTweaksUtil;


@Mixin(SugarCaneBlock.class)
public class SugarCaneBlockMixin implements Fertilizable {

    @Shadow @Final public static IntProperty AGE;

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return FarmTweaksUtil.canBonemeal(state.getBlock());
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
            SugarCaneBlock block = (SugarCaneBlock) (Object) this;
            while (world.getBlockState(pos).isOf(Blocks.SUGAR_CANE)) {
                pos = pos.up();
            }
            pos = pos.down();
            state = world.getBlockState(pos);
            if (world.isAir(pos.up())) {
                int i;
                for (i = 1; world.getBlockState(pos.down(i)).isOf(block); ++i) {
                }

                if (i < 3) {
                    int j = (Integer)state.get(AGE);
                    if (j == 15) {
                        world.setBlockState(pos.up(), block.getDefaultState());
                        world.setBlockState(pos, (BlockState)state.with(AGE, 0), 4);
                    } else {
                        world.setBlockState(pos, (BlockState)state.with(AGE, MathHelper.clamp(j + MathHelper.nextInt(Random.create(), 3, 5), 0, 15)), 4);
                    }
                }
        }
    }
}
