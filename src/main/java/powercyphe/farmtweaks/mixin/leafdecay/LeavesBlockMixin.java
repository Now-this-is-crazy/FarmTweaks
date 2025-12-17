package powercyphe.farmtweaks.mixin.leafdecay;

import net.minecraft.core.BlockBox;
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

@Mixin(LeavesBlock.class)
public abstract class LeavesBlockMixin extends Block {
    public LeavesBlockMixin(Properties properties) {
        super(properties);
    }

    @Shadow
    protected abstract boolean decaying(BlockState blockState);

    @Inject(method = "randomTick", at = @At("HEAD"))
    private void farmtweaks$fastLeafDecay(BlockState state, ServerLevel level, BlockPos blockPos, RandomSource random, CallbackInfo ci) {
        if (this.decaying(state) && FarmTweaksConfig.fastLeafDecay) {
            BlockBox box = BlockBox.of(blockPos.offset(1, 1, 1), blockPos.offset(-1, -1, -1));
            box.forEach(adjPos -> {
                BlockState adjState = level.getBlockState(adjPos);

                if (adjState.is(this) && !blockPos.equals(adjPos)) {
                    level.scheduleTick(blockPos, this, random.nextInt(30));
                }
            });

        }
    }
}
