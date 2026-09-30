package dev.powercyphe.farmtweaks.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import dev.powercyphe.farmtweaks.util.FarmTweaksUtil;

@Mixin(FarmlandBlock.class)
public abstract class FarmlandBlockMixin {

    @WrapOperation(method = "fallOn", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/FarmlandBlock;turnToBaseBlock(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V"))
    private void setToDirtMixin(FarmlandBlock instance, Entity sourceEntity, BlockState state, Level level, BlockPos pos, Operation<Void> original) {
        if (FarmTweaksUtil.allowFarmLandTrampling()) {
            original.call(instance, sourceEntity, state, level, pos);
        }
    }
}
