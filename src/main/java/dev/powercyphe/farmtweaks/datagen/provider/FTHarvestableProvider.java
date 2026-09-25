package dev.powercyphe.farmtweaks.datagen.provider;

import dev.powercyphe.farmtweaks.compat.FTBuiltInCompat;
import dev.powercyphe.farmtweaks.datagen.FTDynamicRegistryProvider;
import dev.powercyphe.farmtweaks.init.FTRegistries;
import dev.powercyphe.farmtweaks.mixin.accessor.IntegerPropertyAccessor;
import dev.powercyphe.farmtweaks.reloadlistener.data.Harvestable;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.*;
import vectorwing.farmersdelight.common.block.CabbageBlock;
import vectorwing.farmersdelight.common.block.OnionBlock;
import vectorwing.farmersdelight.common.block.RicePaniclesBlock;

import java.util.concurrent.CompletableFuture;

import static dev.powercyphe.farmtweaks.init.FTHarvestables.*;
import static dev.powercyphe.farmtweaks.compat.farmersdelight.FDHarvestables.*;

public class FTHarvestableProvider extends FTDynamicRegistryProvider<Harvestable> {
    public FTHarvestableProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void configure() {
        register(WHEAT, Harvestable.crop(CropBlock.AGE));
        register(CARROTS, Harvestable.crop(CarrotBlock.AGE));
        register(POTATOES, Harvestable.crop(PotatoBlock.AGE));
        register(BEETROOTS, Harvestable.crop(BeetrootBlock.AGE));
        register(COCOA, Harvestable.crop(CocoaBlock.AGE));
        register(PUMPKIN, Harvestable.stemCrop());
        register(MELON, Harvestable.stemCrop());

        if (FTBuiltInCompat.FARMERS_DELIGHT) {
            register(ONIONS, Harvestable.crop(OnionBlock.AGE));
            register(CABBAGES, Harvestable.crop(CabbageBlock.AGE));
            register(RICE_PANICLES, Harvestable.crop(RicePaniclesBlock.RICE_AGE));
        }
    }

    @Override
    public ResourceKey<Registry<Harvestable>> registry() {
        return FTRegistries.HARVESTABLE;
    }

}
