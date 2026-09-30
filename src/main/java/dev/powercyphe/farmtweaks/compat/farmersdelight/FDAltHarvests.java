package dev.powercyphe.farmtweaks.compat.farmersdelight;

import dev.powercyphe.farmtweaks.init.FTRegistries;
import dev.powercyphe.farmtweaks.reloadlistener.data.AltHarvest;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.references.ModItemIds;

public interface FDAltHarvests {

    ResourceKey<AltHarvest> FLINT_KNIFE = register(ModItemIds.FLINT_KNIFE);
    ResourceKey<AltHarvest> COPPER_KNIFE = register(ModItemIds.COPPER_KNIFE);
    ResourceKey<AltHarvest> GOLDEN_KNIFE = register(ModItemIds.GOLDEN_KNIFE);
    ResourceKey<AltHarvest> IRON_KNIFE = register(ModItemIds.IRON_KNIFE);
    ResourceKey<AltHarvest> DIAMOND_KNIFE = register(ModItemIds.DIAMOND_KNIFE);
    ResourceKey<AltHarvest> NETHERITE_KNIFE = register(ModItemIds.NETHERITE_KNIFE);

    static void init() {}

    static ResourceKey<AltHarvest> register(ResourceKey<Item> itemKey) {
        return register(itemKey.identifier());
    }

    static ResourceKey<AltHarvest> register(Identifier id) {
        return ResourceKey.create(FTRegistries.ALT_HARVEST, id);
    }
}
