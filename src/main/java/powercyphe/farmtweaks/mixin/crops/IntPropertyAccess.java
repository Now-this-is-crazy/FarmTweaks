package powercyphe.farmtweaks.mixin.crops;

import net.minecraft.state.property.IntProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(IntProperty.class)
public interface IntPropertyAccess {
    @Accessor("max")
    int getMax();

    @Accessor("min")
    int getMin();
}
