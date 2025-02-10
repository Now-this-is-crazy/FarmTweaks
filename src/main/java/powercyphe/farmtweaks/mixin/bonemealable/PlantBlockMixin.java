package powercyphe.farmtweaks.mixin.bonemealable;

import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import powercyphe.farmtweaks.FarmTweaksUtil;

@Mixin(PlantBlock.class)
public class PlantBlockMixin implements Fertilizable {


    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        return FarmTweaksUtil.canBonemeal(state.getBlock());
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return FarmTweaksUtil.canBonemeal(state.getBlock());
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (FarmTweaksUtil.canBonemeal(state.getBlock())) {
            ItemStack item = ((Block)(Object)this).asItem().getDefaultStack();
            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, item);
            world.spawnEntity(itemEntity);
        }
    }
}
