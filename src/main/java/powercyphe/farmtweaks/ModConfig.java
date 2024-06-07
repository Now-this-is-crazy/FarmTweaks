package powercyphe.farmtweaks;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.Arrays;
import java.util.List;

public class ModConfig extends MidnightConfig {
    @Entry
    public static List<String> dispensableItems = Arrays.asList(
            "minecraft:wheat_seeds",
            "minecraft:beetroot_seeds",
            "minecraft:carrot",
            "minecraft:potato",
            "minecraft:melon_seeds",
            "minecraft:pumpkin_seeds",
            "minecraft:pitcher_pod",
            "minecraft:torchflower_seeds"
    );
    @Entry
    public static List<String> nonBonemealableFlowers = Arrays.asList(
            "minecraft:wither_rose",
            "minecraft:torchflower"
    );
    @Entry
    public static Boolean allowFarmlandTrampling = false;
    @Entry
    public static Boolean bonemealableSugarcane = true;
    @Entry
    public static Boolean allowAlternateHoeUse = true;
    @Entry
    public static Boolean cropsDropExperience = true;
}
