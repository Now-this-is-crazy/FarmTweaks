package powercyphe.farmtweaks;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.Arrays;
import java.util.List;

public class FarmTweaksConfig extends MidnightConfig {

    @Entry
    public static boolean fastLeafDecay = true;

    @Entry
    public static boolean allowAlternateHoeUse = true;

    @Entry
    public static boolean allowFarmlandTrampling = false;

    @Entry
    public static boolean allowGrassReplenishment = true;

    @Entry
    public static int cropExperienceChance = 50;

    @Entry
    public static boolean enhancedBonemeal = true;

    @Entry
    public static List<String> nonBonemealableBlocks = Arrays.asList(
            "minecraft:nether_wart",
            "minecraft:wither_rose",
            "minecraft:torchflower",
            "minecraft:pitcher_plant"
    );

    @Entry
    public static List<String> dispensableItems = Arrays.asList(
            "minecraft:wheat_seeds",
            "minecraft:beetroot_seeds",
            "minecraft:carrot",
            "minecraft:potato",
            "minecraft:melon_seeds",
            "minecraft:pumpkin_seeds",
            "minecraft:pitcher_pod",
            "minecraft:torchflower_seeds",
            "minecraft:nether_wart"
    );


    @Entry
    public static List<String> harvestableBlocks = Arrays.asList(
            "minecraft:wheat",
            "minecraft:beetroots",
            "minecraft:carrots",
            "minecraft:potatoes",
            "minecraft:nether_wart"
    );
}
