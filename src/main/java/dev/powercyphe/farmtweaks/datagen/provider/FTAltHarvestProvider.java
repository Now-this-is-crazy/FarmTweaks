package dev.powercyphe.farmtweaks.datagen.provider;

import dev.powercyphe.farmtweaks.compat.FTBuiltInCompat;
import dev.powercyphe.farmtweaks.datagen.FTDynamicRegistryProvider;
import dev.powercyphe.farmtweaks.init.FTRegistries;
import dev.powercyphe.farmtweaks.reloadlistener.data.AltHarvest;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.concurrent.CompletableFuture;

import static dev.powercyphe.farmtweaks.init.FTAltHarvests.*;
import static dev.powercyphe.farmtweaks.compat.farmersdelight.FDAltHarvests.*;

public class FTAltHarvestProvider extends FTDynamicRegistryProvider<AltHarvest> {
    public FTAltHarvestProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void configure() {
        register(WOODEN_HOE, new AltHarvest(0));
        register(STONE_HOE, new AltHarvest(0));
        register(COPPER_HOE, new AltHarvest(1));
        register(GOLDEN_HOE, new AltHarvest(1));
        register(IRON_HOE, new AltHarvest(1.3));
        register(DIAMOND_HOE, new AltHarvest(1.6));
        register(NETHERITE_HOE, new AltHarvest(2));

        if (FTBuiltInCompat.FARMERS_DELIGHT) {
            register(FLINT_KNIFE, new AltHarvest(0));
            register(COPPER_KNIFE, new AltHarvest(1));
            register(GOLDEN_KNIFE, new AltHarvest(1));
            register(IRON_KNIFE, new AltHarvest(1.3));
            register(DIAMOND_KNIFE, new AltHarvest(1.6));
            register(NETHERITE_KNIFE, new AltHarvest(2));
        }
    }

    @Override
    public ResourceKey<Registry<AltHarvest>> registry() {
        return FTRegistries.ALT_HARVEST;
    }
}
