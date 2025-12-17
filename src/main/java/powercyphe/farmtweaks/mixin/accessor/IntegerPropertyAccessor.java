package powercyphe.farmtweaks.mixin.accessor;

import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(IntegerProperty.class)
public interface IntegerPropertyAccessor {
    @Accessor("max")
    int farmtweaks$getMax();

    @Accessor("min")
    int farmtweaks$getMin();
}
