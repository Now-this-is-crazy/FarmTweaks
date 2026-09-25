package dev.powercyphe.farmtweaks.compat;

import dev.powercyphe.farmtweaks.compat.farmersdelight.FDAltHarvests;
import dev.powercyphe.farmtweaks.compat.farmersdelight.FDHarvestables;
import net.fabricmc.loader.api.FabricLoader;

public class FTBuiltInCompat {

    public static final String FARMERS_DELIGHT_ID = "farmersdelight";
    public static final boolean FARMERS_DELIGHT = FabricLoader.getInstance().isModLoaded(FARMERS_DELIGHT_ID);

    public static void init() {
        if (FARMERS_DELIGHT) {
            FDAltHarvests.init();
            FDHarvestables.init();
        }
    }
}
