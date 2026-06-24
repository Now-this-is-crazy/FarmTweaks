package powercyphe.farmtweaks.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

@SuppressWarnings("unchecked")
public record PropertySet(String name, String value) {
    public static final Codec<PropertySet> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.fieldOf("name").forGetter(PropertySet::name),
                    Codec.STRING.fieldOf("value").forGetter(PropertySet::value)
            ).apply(instance, PropertySet::new));

    public <T extends Comparable<T>> BlockState applyTo(BlockState state) {
        Block block = state.getBlock();
        Property<T> prop = (Property<T>) block.getStateDefinition().getProperty(this.name());

        if (prop != null && state.hasProperty(prop)) {
            for (var pVal : prop.getPossibleValues()) {
                if (this.value().equals(pVal.toString())) {
                    state = state.setValue(prop, pVal);
                    break;
                }
            }
        }
        return state;
    }

    public <T extends Comparable<T>> boolean in(BlockState state) {
        Block block = state.getBlock();
        Property<T> prop = (Property<T>) block.getStateDefinition().getProperty(this.name());

        if (prop != null && state.hasProperty(prop)) {
            return this.value().equals(state.getValue(prop).toString());
        }
        return false;
    }

}
