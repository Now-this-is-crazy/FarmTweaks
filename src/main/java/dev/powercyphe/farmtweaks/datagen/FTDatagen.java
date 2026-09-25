package dev.powercyphe.farmtweaks.datagen;

import dev.powercyphe.farmtweaks.datagen.provider.FTAltHarvestProvider;
import dev.powercyphe.farmtweaks.datagen.provider.FTHarvestableProvider;
import dev.powercyphe.farmtweaks.datagen.provider.FTReplenishableProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class FTDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(FTAltHarvestProvider::new);
        pack.addProvider(FTHarvestableProvider::new);
        pack.addProvider(FTReplenishableProvider::new);
    }

}
