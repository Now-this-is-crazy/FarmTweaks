package dev.powercyphe.farmtweaks.init;

import dev.powercyphe.farmtweaks.reloadlistener.data.Replenishable;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public interface FTReplenishables {

    ResourceKey<Replenishable> DIRT = register(Blocks.DIRT);
    ResourceKey<Replenishable> COARSE_DIRT = register(Blocks.COARSE_DIRT);
    ResourceKey<Replenishable> ROOTED_DIRT = register(Blocks.ROOTED_DIRT);
    
    static void init() {}

    static ResourceKey<Replenishable> register(Block block) {
        return register(block.builtInRegistryHolder().key().identifier());
    }

    static ResourceKey<Replenishable> register(Identifier id) {
        return ResourceKey.create(FTRegistries.REPLENISHABLE, id);
    }
}
