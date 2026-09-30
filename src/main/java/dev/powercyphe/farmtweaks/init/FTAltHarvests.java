package dev.powercyphe.farmtweaks.init;

import dev.powercyphe.farmtweaks.reloadlistener.data.AltHarvest;
import net.minecraft.references.ItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public interface FTAltHarvests {

    ResourceKey<AltHarvest> WOODEN_HOE = register(ItemIds.WOODEN_HOE);
    ResourceKey<AltHarvest> STONE_HOE = register(ItemIds.STONE_HOE);
    ResourceKey<AltHarvest> COPPER_HOE = register(ItemIds.COPPER_HOE);
    ResourceKey<AltHarvest> GOLDEN_HOE = register(ItemIds.GOLDEN_HOE);
    ResourceKey<AltHarvest> IRON_HOE = register(ItemIds.IRON_HOE);
    ResourceKey<AltHarvest> DIAMOND_HOE = register(ItemIds.DIAMOND_HOE);
    ResourceKey<AltHarvest> NETHERITE_HOE = register(ItemIds.NETHERITE_HOE);

    static void init() {}

    static ResourceKey<AltHarvest> register(ResourceKey<Item> itemKey) {
        return register(itemKey.identifier());
    }

    static ResourceKey<AltHarvest> register(Identifier id) {
        return ResourceKey.create(FTRegistries.ALT_HARVEST, id);
    }
}
