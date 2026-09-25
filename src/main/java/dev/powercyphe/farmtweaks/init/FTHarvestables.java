package dev.powercyphe.farmtweaks.init;

import dev.powercyphe.farmtweaks.reloadlistener.data.Harvestable;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public interface FTHarvestables {

    ResourceKey<Harvestable> WHEAT = register(Blocks.WHEAT);
    ResourceKey<Harvestable> CARROTS = register(Blocks.CARROTS);
    ResourceKey<Harvestable> POTATOES = register(Blocks.POTATOES);
    ResourceKey<Harvestable> BEETROOTS = register(Blocks.BEETROOTS);
    ResourceKey<Harvestable> COCOA = register(Blocks.COCOA);
    ResourceKey<Harvestable> PUMPKIN = register(Blocks.PUMPKIN);
    ResourceKey<Harvestable> MELON = register(Blocks.MELON);

    static void init() {}

    static ResourceKey<Harvestable> register(Block block) {
        return register(block.builtInRegistryHolder().key().identifier());
    }

    static ResourceKey<Harvestable> register(Identifier id) {
        return ResourceKey.create(FTRegistries.HARVESTABLE, id);
    }
    
}
