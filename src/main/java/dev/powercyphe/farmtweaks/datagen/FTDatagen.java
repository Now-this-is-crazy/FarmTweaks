package dev.powercyphe.farmtweaks.datagen;

import dev.powercyphe.farmtweaks.datagen.provider.FTAltHarvestProvider;
import dev.powercyphe.farmtweaks.datagen.provider.FTHarvestableProvider;
import dev.powercyphe.farmtweaks.datagen.provider.FTReplenishableProvider;
import dev.powercyphe.farmtweaks.datagen.provider.tag.FTBlockTagProvider;
import dev.powercyphe.farmtweaks.datagen.provider.tag.FTItemTagProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class FTDatagen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        var blockTagProvider = pack.addProvider(FTBlockTagProvider::new);
        pack.addProvider((output, registries)
                -> new FTItemTagProvider(output, registries, blockTagProvider));

        pack.addProvider(FTAltHarvestProvider::new);
        pack.addProvider(FTHarvestableProvider::new);
        pack.addProvider(FTReplenishableProvider::new);
    }

}
