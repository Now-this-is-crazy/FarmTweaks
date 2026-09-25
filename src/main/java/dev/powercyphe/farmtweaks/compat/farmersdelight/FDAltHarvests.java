package dev.powercyphe.farmtweaks.compat.farmersdelight;

import dev.powercyphe.farmtweaks.init.FTRegistries;
import dev.powercyphe.farmtweaks.reloadlistener.data.AltHarvest;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import vectorwing.farmersdelight.common.registry.ModItems;

public interface FDAltHarvests {

    ResourceKey<AltHarvest> FLINT_KNIFE = register(ModItems.FLINT_KNIFE.get());
    ResourceKey<AltHarvest> COPPER_KNIFE = register(ModItems.COPPER_KNIFE.get());
    ResourceKey<AltHarvest> GOLDEN_KNIFE = register(ModItems.GOLDEN_KNIFE.get());
    ResourceKey<AltHarvest> IRON_KNIFE = register(ModItems.IRON_KNIFE.get());
    ResourceKey<AltHarvest> DIAMOND_KNIFE = register(ModItems.DIAMOND_KNIFE.get());
    ResourceKey<AltHarvest> NETHERITE_KNIFE = register(ModItems.NETHERITE_KNIFE.get());

    static void init() {}

    static ResourceKey<AltHarvest> register(Item item) {
        return register(item.builtInRegistryHolder().key().identifier());
    }

    static ResourceKey<AltHarvest> register(Identifier id) {
        return ResourceKey.create(FTRegistries.ALT_HARVEST, id);
    }
}
