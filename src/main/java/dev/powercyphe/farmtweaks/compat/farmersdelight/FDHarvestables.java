package dev.powercyphe.farmtweaks.compat.farmersdelight;

import dev.powercyphe.farmtweaks.init.FTRegistries;
import dev.powercyphe.farmtweaks.reloadlistener.data.Harvestable;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import vectorwing.farmersdelight.common.references.ModBlockItemIds;
import vectorwing.farmersdelight.common.registry.ModBlocks;

public interface FDHarvestables {

    ResourceKey<Harvestable> ONIONS = register(ModBlockItemIds.ONION_CROP.block());
    ResourceKey<Harvestable> CABBAGES = register(ModBlockItemIds.CABBAGE_CROP.block());
    ResourceKey<Harvestable> RICE_PANICLES = register(ModBlockItemIds.RICE_CROP_PANICLES.block());

    static void init() {}

    static ResourceKey<Harvestable> register(ResourceKey<Block> blockKey) {
        return register(blockKey.identifier());
    }

    static ResourceKey<Harvestable> register(Identifier id) {
        return ResourceKey.create(FTRegistries.HARVESTABLE, id);
    }
}
