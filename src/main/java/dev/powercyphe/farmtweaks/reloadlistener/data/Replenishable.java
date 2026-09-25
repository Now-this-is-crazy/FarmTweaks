package dev.powercyphe.farmtweaks.reloadlistener.data;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.powercyphe.farmtweaks.util.PropertySet;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record Replenishable(boolean allowed, Optional<List<PropertySet>> required, Either<Block, BlockState> replenished, HolderSet<Item> items) {
    public static final Map<Block, Replenishable> VALUES = new HashMap<>();

    public static final Codec<Replenishable> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("altHarvest", true).forGetter(Replenishable::allowed),
                    PropertySet.CODEC.listOf().optionalFieldOf("required").forGetter(Replenishable::required),
                    Codec.either(BuiltInRegistries.BLOCK.byNameCodec(), BlockState.CODEC).fieldOf("replenished").forGetter(Replenishable::replenished),
                    HolderSetCodec.create(Registries.ITEM, Item.CODEC, false).fieldOf("items").forGetter(Replenishable::items)
            ).apply(instance, Replenishable::new)
    );

    public Replenishable(List<PropertySet> required, Block replenished, HolderSet<Item> items) {
        this(true, Optional.of(required), Either.left(replenished), items);
    }

    public Replenishable(List<PropertySet> required, BlockState replenished, HolderSet<Item> items) {
        this(true, Optional.of(required), Either.right(replenished), items);
    }

    public Replenishable(Block replenished, HolderSet<Item> items) {
        this(true, Optional.empty(), Either.left(replenished), items);
    }

    public Replenishable(BlockState replenished, HolderSet<Item> items) {
        this(true, Optional.empty(), Either.right(replenished), items);
    }

    public static Optional<BlockState> getReplenished(Item item, BlockState state) {
        Block block = state.getBlock();

        if (VALUES.containsKey(block)) {
            Replenishable replenishable = VALUES.get(block);

            if (replenishable.items().contains(item.builtInRegistryHolder())) {
                for (PropertySet cond : replenishable.required.orElse(List.of())) {
                    if (!cond.in(state)) {
                        return Optional.empty();
                    }
                }

                var repl = replenishable.replenished();
                if (repl.left().isPresent()) {
                    return Optional.of(repl.left().get().defaultBlockState());
                } else if (repl.right().isPresent()) {
                    return Optional.of(repl.right().get());
                }
            }
        }
        return Optional.empty();
    }
}
