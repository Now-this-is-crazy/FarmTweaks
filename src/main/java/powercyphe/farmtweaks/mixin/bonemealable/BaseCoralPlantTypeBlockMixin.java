package powercyphe.farmtweaks.mixin.bonemealable;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseCoralPlantTypeBlock;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import powercyphe.farmtweaks.FarmTweaksUtil;

@Mixin(BaseCoralPlantTypeBlock.class)
public class BaseCoralPlantTypeBlockMixin implements BonemealableBlock {

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
        BaseCoralPlantTypeBlock block = (BaseCoralPlantTypeBlock) (Object) this;

        if (FarmTweaksUtil.canBonemeal(state.getBlock())) {
            ItemStack item = block.asItem().getDefaultInstance();
            ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, item);
            world.addFreshEntity(itemEntity);
        }
    }
}
