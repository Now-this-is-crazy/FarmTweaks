package dev.powercyphe.farmtweaks.init;

import dev.powercyphe.farmtweaks.reloadlistener.data.Harvestable;
import net.minecraft.references.BlockItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public interface FTHarvestables {

    ResourceKey<Harvestable> WHEAT = register(BlockItemIds.WHEAT_CROP.block());
    ResourceKey<Harvestable> CARROTS = register(BlockItemIds.CARROT_CROP.block());
    ResourceKey<Harvestable> POTATOES = register(BlockItemIds.POTATO_CROP.block());
    ResourceKey<Harvestable> BEETROOTS = register(BlockItemIds.BEETROOT_CROP.block());
    ResourceKey<Harvestable> COCOA = register(BlockItemIds.COCOA_CROP.block());
    ResourceKey<Harvestable> PUMPKIN = register(BlockItemIds.PUMPKIN.block());
    ResourceKey<Harvestable> MELON = register(BlockItemIds.MELON.block());
    ResourceKey<Harvestable> NETHER_WART = register(BlockItemIds.NETHER_WART.block());

    static void init() {}

    static ResourceKey<Harvestable> register(ResourceKey<Block> blockKey) {
        return register(blockKey.identifier());
    }

    static ResourceKey<Harvestable> register(Identifier id) {
        return ResourceKey.create(FTRegistries.HARVESTABLE, id);
    }
    
}
