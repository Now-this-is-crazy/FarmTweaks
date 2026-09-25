package dev.powercyphe.farmtweaks.datagen.provider;

import dev.powercyphe.farmtweaks.datagen.FTDynamicRegistryProvider;
import dev.powercyphe.farmtweaks.init.FTRegistries;
import dev.powercyphe.farmtweaks.init.FTTags;
import dev.powercyphe.farmtweaks.reloadlistener.data.Replenishable;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

import static dev.powercyphe.farmtweaks.init.FTReplenishables.*;

public class FTReplenishableProvider extends FTDynamicRegistryProvider<Replenishable> {
    public FTReplenishableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void configure() {
        var grassSeeds = registries().get(FTTags.GRASS_SEEDS).get();

        register(DIRT, new Replenishable(Blocks.GRASS_BLOCK, grassSeeds));
        register(COARSE_DIRT, new Replenishable(Blocks.PODZOL, grassSeeds));
        register(ROOTED_DIRT, new Replenishable(Blocks.MYCELIUM, grassSeeds));
    }

    @Override
    public ResourceKey<Registry<Replenishable>> registry() {
        return FTRegistries.REPLENISHABLE;
    }
}
