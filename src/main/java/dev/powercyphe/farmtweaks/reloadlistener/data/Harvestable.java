package dev.powercyphe.farmtweaks.reloadlistener.data;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.powercyphe.farmtweaks.mixin.accessor.IntegerPropertyAccessor;
import dev.powercyphe.farmtweaks.util.PropertySet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record Harvestable(boolean altHarvest, List<PropertySet> required, Either<List<PropertySet>, BlockState> harvested, Optional<ExperienceDrop> experienceDrop) {
    public static final Map<Block, Harvestable> VALUES = new HashMap<>();

    public static final Codec<Harvestable> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("altHarvest", true).forGetter(Harvestable::altHarvest),
                    PropertySet.CODEC.listOf().fieldOf("required").forGetter(Harvestable::required),
                    Codec.either(PropertySet.CODEC.listOf(), BlockState.CODEC).fieldOf("harvestable").forGetter(Harvestable::harvested),
                    ExperienceDrop.CODEC.optionalFieldOf("experienceDrop").forGetter(Harvestable::experienceDrop)
            ).apply(instance, Harvestable::new)
    );

    public Harvestable(boolean altHarvest, List<PropertySet> required, List<PropertySet> harvested, ExperienceDrop experienceDrop) {
        this(altHarvest, required, Either.left(harvested), Optional.of(experienceDrop));
    }

    public Harvestable(boolean altHarvest, List<PropertySet> required, List<PropertySet> harvested) {
        this(altHarvest, required, Either.left(harvested), Optional.empty());
    }

    public Harvestable(boolean altHarvest, List<PropertySet> required, BlockState harvested, ExperienceDrop experienceDrop) {
        this(altHarvest, required, Either.right(harvested), Optional.of(experienceDrop));
    }

    public Harvestable(boolean altHarvest, List<PropertySet> required, BlockState harvested) {
        this(altHarvest, required, Either.right(harvested), Optional.empty());
    }

    public static Harvestable crop(IntegerProperty ageProperty, ExperienceDrop experienceDrop) {
        var accessor = (IntegerPropertyAccessor) (Object) ageProperty;
        return new Harvestable(true,
                List.of(new PropertySet(ageProperty.getName(), accessor.farmtweaks$max())),
                List.of(new PropertySet(ageProperty.getName(), accessor.farmtweaks$min())),
                experienceDrop
        );
    }

    public static Harvestable crop(IntegerProperty ageProperty) {
        var accessor = (IntegerPropertyAccessor) (Object) ageProperty;
        return new Harvestable(true, 
                List.of(new PropertySet(ageProperty.getName(), accessor.farmtweaks$max())),
                List.of(new PropertySet(ageProperty.getName(), accessor.farmtweaks$min())),
                new ExperienceDrop(1, 3, 0.2F)
        );
    }

    public static Harvestable stemCrop(ExperienceDrop experienceDrop) {
        return new Harvestable(false, List.of(), List.of(), experienceDrop);
    }

    public static Harvestable stemCrop() {
        return stemCrop(new ExperienceDrop(3, 5, 0.3F, ExperienceDrop.CropCondition.HAS_STEM));
    }

    public static Optional<BlockState> getHarvested(BlockState state) {
        Block block = state.getBlock();

        if (VALUES.containsKey(block)) {
            var harvestable = VALUES.get(block);
            boolean canHarvest = true;

            for (var check : harvestable.required()) {
                if (!check.in(state)) {
                    canHarvest = false;
                    break;
                }
            }
            if (canHarvest) {
                BlockState newState = block.withPropertiesOf(state);
                var either = harvestable.harvested();

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

    public static void tryExperienceDrop(BlockState state, BlockPos blockPos, Level level) {
        Block block = state.getBlock();

        if (VALUES.containsKey(block)) {
            var harvestable = VALUES.get(block);
            harvestable.experienceDrop.ifPresent(drop ->
                    drop.dropExperience(state, blockPos, level));
        }
    }

    public static boolean shouldDropExperience(BlockState state, BlockPos blockPos, Level level) {
        Block block = state.getBlock();

        if (VALUES.containsKey(block)) {
            var harvestable = VALUES.get(block);
            return harvestable.experienceDrop.map(drop ->
                    drop.shouldDrop(state, blockPos, level)).orElse(false);
        }
        return false;
    }
}
