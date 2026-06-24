package powercyphe.farmtweaks.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public record Replenishable(boolean allowed, Either<Block, BlockState> replenished, HolderSet<Item> items) {
    public static final Map<Block, Replenishable> VALUES = new HashMap<>();

    public static final Codec<Replenishable> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.BOOL.optionalFieldOf("allowed", true).forGetter(Replenishable::allowed),
                    Codec.either(BuiltInRegistries.BLOCK.byNameCodec(), BlockState.CODEC).fieldOf("replenished").forGetter(Replenishable::replenished),
                    HolderSetCodec.create(Registries.ITEM, Item.CODEC, false).fieldOf("items").forGetter(Replenishable::items)
            ).apply(instance, Replenishable::new)
    );

    public static Optional<BlockState> getReplenished(Item item, BlockState state) {
        Block block = state.getBlock();

        if (VALUES.containsKey(block)) {
            var replenishable = VALUES.get(block);

            if (replenishable.items().contains(item.builtInRegistryHolder())) {
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
