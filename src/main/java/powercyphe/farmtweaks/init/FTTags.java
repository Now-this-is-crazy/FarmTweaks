package powercyphe.farmtweaks.init;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import powercyphe.farmtweaks.FarmTweaks;

public interface FTTags {

    static void init() {}

    interface Items {
        TagKey<Item> ALLOWS_ALTERNATE_HOE_USE = key(Registries.ITEM, "allows_alternate_hoe_use");
        TagKey<Item> GRASS_SEEDS_TAG = key(Registries.ITEM, "grass_seeds");
    }

    interface Blocks {
        TagKey<Block> BONEMEAL_DUPLICATABLE = key(Registries.BLOCK, "bonemeal_duplicatable");
    }

    static <A> TagKey<A> key(ResourceKey<Registry<A>> registry, String name) {
        return TagKey.create(registry, FarmTweaks.id(name));
    }
}
