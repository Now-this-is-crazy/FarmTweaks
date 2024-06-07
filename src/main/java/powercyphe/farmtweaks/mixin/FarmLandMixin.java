package powercyphe.farmtweaks.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import powercyphe.farmtweaks.util.ConfigUtil;

@Mixin(FarmlandBlock.class)
public abstract class FarmLandMixin {

    @Shadow
    public static void setToDirt(@Nullable Entity entity, BlockState state, World world, BlockPos pos) {
    }

    @Redirect(method = "onLandedUpon", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/FarmlandBlock;setToDirt(Lnet/minecraft/entity/Entity;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)V"))
    private void setToDirtMixin(Entity entity, BlockState state, World world, BlockPos pos) {
        if (!ConfigUtil.canTrampleFarmland()) {
            setToFarmLand(entity, state, world, pos);
        } else {
            setToDirt(entity, state, world, pos);
        }
    }

    @Unique
    private void setToFarmLand(@Nullable Entity entity, BlockState state, World world, BlockPos pos) {
        BlockState blockState = Blocks.FARMLAND.getDefaultState();
        world.setBlockState(pos, blockState);
        world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(entity, blockState));
    }
}
