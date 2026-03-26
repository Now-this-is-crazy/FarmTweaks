package powercyphe.farmtweaks.mixin.leafdecay;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import powercyphe.farmtweaks.FarmTweaksConfig;
import powercyphe.farmtweaks.event.LeafDecayEvent;

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block {
    @Shadow
    protected abstract boolean decaying(BlockState blockState);

    public LeavesBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void farmtweaks$fastLeafDecay(BlockState state, ServerLevel level, BlockPos blockPos, RandomSource random, CallbackInfo ci) {
        LeavesBlock leavesBlock = (LeavesBlock) (Object) this;

        if (FarmTweaksConfig.fastLeafDecay && this.decaying(state)) {
            LeafDecayEvent.get().queueNearby(level, leavesBlock, blockPos);
        }
    }
}
