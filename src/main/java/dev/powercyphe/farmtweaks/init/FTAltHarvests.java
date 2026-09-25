package dev.powercyphe.farmtweaks.init;

import dev.powercyphe.farmtweaks.reloadlistener.data.AltHarvest;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public interface FTAltHarvests {

    ResourceKey<AltHarvest> WOODEN_HOE = register(Items.WOODEN_HOE);
    ResourceKey<AltHarvest> STONE_HOE = register(Items.STONE_HOE);
    ResourceKey<AltHarvest> COPPER_HOE = register(Items.COPPER_HOE);
    ResourceKey<AltHarvest> GOLDEN_HOE = register(Items.GOLDEN_HOE);
    ResourceKey<AltHarvest> IRON_HOE = register(Items.IRON_HOE);
    ResourceKey<AltHarvest> DIAMOND_HOE = register(Items.DIAMOND_HOE);
    ResourceKey<AltHarvest> NETHERITE_HOE = register(Items.NETHERITE_HOE);

    static void init() {}

    static ResourceKey<AltHarvest> register(Item item) {
        return register(item.builtInRegistryHolder().key().identifier());
    }

    static ResourceKey<AltHarvest> register(Identifier id) {
        return ResourceKey.create(FTRegistries.ALT_HARVEST, id);
    }
}
