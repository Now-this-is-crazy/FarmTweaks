package powercyphe.farmtweaks.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import powercyphe.farmtweaks.FarmTweaks;
import powercyphe.farmtweaks.ModConfig;

import java.util.ArrayList;

public class ConfigUtil {

    public static ArrayList<Item> getDispensableItems() {
        ArrayList<Item> items = new ArrayList<>();

        int iter = 0;
        while (iter < ModConfig.dispensableItems.size()) {
            String entry = ModConfig.dispensableItems.get(iter);
            String[] split = entry.split(":");
            if (Identifier.isNamespaceValid(split[0])) {
                Item item = Registries.ITEM.get(Identifier.of(split[0], split[1]));
                    items.add(item);
            } else {
                FarmTweaks.errorMessage("Invalid Namespace at " + entry);
            }
            iter++;

        }
        return items;
    }

    public static ArrayList<Block> getNonBonemealableFlowers() {
        ArrayList<Block> items = new ArrayList<>();

        int iter = 0;
        while (iter < ModConfig.nonBonemealableFlowers.size()) {
            String entry = ModConfig.nonBonemealableFlowers.get(iter);
            String[] split = entry.split(":");
            if (Identifier.isNamespaceValid(split[0])) {
                Block block = Registries.BLOCK.get(Identifier.of(split[0], split[1]));
                items.add(block);
            } else {
                FarmTweaks.errorMessage("Invalid Namespace at " + entry);
            }
            iter++;

        }
        return items;
    }

    public static boolean canTrampleFarmland() {
        return ModConfig.allowFarmlandTrampling;
    }

    public static boolean canBonemealSugarcane() {
        return ModConfig.bonemealableSugarcane;
    }

    public static boolean allowAltHoeUse() {
        return ModConfig.allowAlternateHoeUse;
    }
}
