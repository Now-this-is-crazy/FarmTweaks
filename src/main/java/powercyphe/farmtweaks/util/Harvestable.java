package powercyphe.farmtweaks.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record Harvestable(boolean allowed, List<PropertySet> required, Either<List<PropertySet>, BlockState> harvested) {
    public static final Map<Block, Harvestable> VALUES = new HashMap<>();

    public static final Codec<Harvestable> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("allowed", true).forGetter(Harvestable::allowed),
                    PropertySet.CODEC.listOf().fieldOf("required").forGetter(Harvestable::required),
                    Codec.either(PropertySet.CODEC.listOf(), BlockState.CODEC).fieldOf("harvested").forGetter(Harvestable::harvested)
            ).apply(instance, Harvestable::new)
    );

    public static Optional<BlockState> getHarvested(BlockState state) {
        Block block = state.getBlock();

        if (VALUES.containsKey(block)) {
            var harvested = VALUES.get(block);
            boolean harvestable = true;

            for (var check : harvested.required()) {
                if (!check.in(state)) {
                    harvestable = false;
                    break;
                }
            }
            if (harvestable) {
                BlockState newState = block.withPropertiesOf(state);
                var either = harvested.harvested();

                if (either.left().isPresent()) {
                    for (var change : either.left().get()) {
                        newState = change.applyTo(newState);
                    }
                } else if (either.right().isPresent()) {
                    newState = either.right().get();
                }
                return Optional.of(newState);
            }
        }
        return Optional.empty();
    }
}
