package dev.powercyphe.farmtweaks.compat.farmersdelight;

import dev.powercyphe.farmtweaks.init.FTRegistries;
import dev.powercyphe.farmtweaks.reloadlistener.data.Harvestable;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public interface FDHarvestables {

    ResourceKey<Harvestable> ONIONS = register(ModBlocks.ONION_CROP.get());
    ResourceKey<Harvestable> CABBAGES = register(ModBlocks.CABBAGE_CROP.get());
    ResourceKey<Harvestable> RICE_PANICLES = register(ModBlocks.RICE_CROP_PANICLES.get());

    static void init() {}

    static ResourceKey<Harvestable> register(Block block) {
        return register(block.builtInRegistryHolder().key().identifier());
    }

    static ResourceKey<Harvestable> register(Identifier id) {
        return ResourceKey.create(FTRegistries.HARVESTABLE, id);
    }
}
