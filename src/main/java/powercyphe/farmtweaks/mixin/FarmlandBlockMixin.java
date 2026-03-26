package powercyphe.farmtweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import powercyphe.farmtweaks.FarmTweaksUtil;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin {


    @WrapOperation(method = "fallOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FarmlandBlock;turnToDirt(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void setToDirtMixin(Entity entity, BlockState state, Level level, BlockPos pos, Operation<Void> original) {
        if (!FarmTweaksUtil.allowFarmLandTrampling()) {
            setToFarmLand(entity, state, level, pos);
        } else {
            FarmlandBlock.turnToDirt(entity, state, level, pos);
        }
    }

    @Unique
    private void setToFarmLand(@Nullable Entity entity, BlockState state, Level world, BlockPos pos) {
        BlockState blockState = Blocks.FARMLAND.defaultBlockState();
        world.setBlockAndUpdate(pos, blockState);
        world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(entity, blockState));
    }
}
