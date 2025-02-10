package powercyphe.farmtweaks;

import net.minecraft.block.Block;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

public class FarmTweaksUtil {

    public static boolean allowAltHoeUse(Block block) {
        return FarmTweaksConfig.allowAlternateHoeUse && FarmTweaksUtil.getHarvestableBlocks().contains(block);
    }

    public static boolean allowFarmLandTrampling() {
        return FarmTweaksConfig.allowFarmlandTrampling;
    }

    public static boolean allowGrassReplenishment() {
        return FarmTweaksConfig.allowGrassReplenishment;
    }

    public static void farmtweaks$dropExp(World world, BlockPos pos) {
        if (new Random().nextInt(100) < FarmTweaksConfig.cropExperienceChance) {
            ExperienceOrbEntity exp = new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), 1);
            world.spawnEntity(exp);
        }
    }

    public static boolean canBonemeal(Block block) {
        return FarmTweaksConfig.enhancedBonemeal && !(getNonBonemealableBlocks().contains(block));
    }

    public static ArrayList<Block> getNonBonemealableBlocks() {
        ArrayList<Block> blocks = new ArrayList<>();

        int iter = 0;
        while (iter < FarmTweaksConfig.nonBonemealableBlocks.size()) {
            String entry = FarmTweaksConfig.nonBonemealableBlocks.get(iter);
            String[] split = entry.split(":");
            if (Identifier.isValid(entry)) {
                Block block = Registries.BLOCK.get(Identifier.of(split[0], split[1]));
                blocks.add(block);
            } else {
                FarmTweaks.errorMessage("Invalid Identifier at " + entry);
            }
            iter++;

        }
        return blocks;
    }

    public static ArrayList<Item> getDispensableItems() {
        ArrayList<Item> items = new ArrayList<>();

        int iter = 0;
        while (iter < FarmTweaksConfig.dispensableItems.size()) {
            String entry = FarmTweaksConfig.dispensableItems.get(iter);
            if (Identifier.isValid(entry)) {
                String[] split = entry.split(":");
                Item item = Registries.ITEM.get(Identifier.of(split[0], split[1]));
                items.add(item);

                System.out.println(item);
            } else {
                FarmTweaks.errorMessage("Invalid Identifier at " + entry);
            }
            iter++;

        }
        return items;
    }

    public static ArrayList<Block> getHarvestableBlocks() {
        ArrayList<Block> blocks = new ArrayList<>();

        int iter = 0;
        while (iter < FarmTweaksConfig.harvestableBlocks.size()) {
            String entry = FarmTweaksConfig.harvestableBlocks.get(iter);
            String[] split = entry.split(":");
            if (Identifier.isValid(entry)) {
                Block block = Registries.BLOCK.get(Identifier.of(split[0], split[1]));
                blocks.add(block);
            } else {
                FarmTweaks.errorMessage("Invalid Identifier at " + entry);
            }
            iter++;

        }
        return blocks;
    }
}
