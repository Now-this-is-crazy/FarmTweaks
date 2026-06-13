package powercyphe.farmtweaks.mixin.accessor;

import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LeavesBlock.class)
public interface LeavesBlockAccessor {

    @Invoker("decaying")
    boolean farmtweaks$decaying(BlockState state);
}
