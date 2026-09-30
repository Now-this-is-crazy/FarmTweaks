package dev.powercyphe.farmtweaks.init;

import dev.powercyphe.farmtweaks.reloadlistener.data.Replenishable;
import net.minecraft.references.BlockItemIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public interface FTReplenishables {

    ResourceKey<Replenishable> DIRT = register(BlockItemIds.DIRT.block());
    ResourceKey<Replenishable> COARSE_DIRT = register(BlockItemIds.COARSE_DIRT.block());
    ResourceKey<Replenishable> ROOTED_DIRT = register(BlockItemIds.ROOTED_DIRT.block());
    
    static void init() {}

    static ResourceKey<Replenishable> register(ResourceKey<Block> blockKey) {
        return register(blockKey.identifier());
    }

    static ResourceKey<Replenishable> register(Identifier id) {
        return ResourceKey.create(FTRegistries.REPLENISHABLE, id);
    }
}
