package powercyphe.farmtweaks.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import powercyphe.farmtweaks.FarmTweaks;

public interface FTTags {

    TagKey<Item> GRASS_SEEDS = key(Registries.ITEM, "grass_seeds");

    TagKey<Block> BONEMEAL_DUPLICATABLE = key(Registries.BLOCK, "bonemeal_duplicatable");

    static void init() {}

    static <A> TagKey<A> key(ResourceKey<Registry<A>> registry, String name) {
        return TagKey.create(registry, FarmTweaks.id(name));
    }
}
