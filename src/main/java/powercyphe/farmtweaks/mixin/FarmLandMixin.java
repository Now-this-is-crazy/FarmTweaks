package powercyphe.farmtweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import powercyphe.farmtweaks.FarmTweaksUtil;

@Mixin(FarmBlock.class)
public abstract class FarmLandMixin {


    @Redirect(method = "fallOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FarmBlock;turnToDirt(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void setToDirtMixin(Entity entity, BlockState state, Level world, BlockPos pos) {
        if (!FarmTweaksUtil.allowFarmLandTrampling()) {
            setToFarmLand(entity, state, world, pos);
        } else {
            FarmBlock.turnToDirt(entity, state, world, pos);
        }
    }

    @Unique
    private void setToFarmLand(@Nullable Entity entity, BlockState state, Level world, BlockPos pos) {
        BlockState blockState = Blocks.FARMLAND.defaultBlockState();
        world.setBlockAndUpdate(pos, blockState);
        world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, blockState));
    }
}
