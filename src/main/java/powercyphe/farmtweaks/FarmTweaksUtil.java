package powercyphe.farmtweaks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
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

    public static void farmtweaks$dropExp(Level world, BlockPos pos) {
        if (new Random().nextInt(100) < FarmTweaksConfig.cropExperienceChance) {
            ExperienceOrb exp = new ExperienceOrb(world, pos.getX(), pos.getY(), pos.getZ(), 1);
            world.addFreshEntity(exp);
        }
    }

    public static boolean canBonemeal(Block block) {
        return FarmTweaksConfig.enhancedBonemeal && !(getNonBonemealableBlocks().contains(block));
    }

    public static List<Block> getNonBonemealableBlocks() {
        return getParsedList(BuiltInRegistries.BLOCK, FarmTweaksConfig.nonBonemealableBlocks);
    }

    public static List<Item> getDispensableItems() {
        return getParsedList(BuiltInRegistries.ITEM, FarmTweaksConfig.dispensableItems);
    }

    public static List<Block> getHarvestableBlocks() {
        return getParsedList(BuiltInRegistries.BLOCK, FarmTweaksConfig.harvestableBlocks);
    }

    public static <T> List<T> getParsedList(Registry<T> registry, List<String> identifiers) {
        List<T> values = new ArrayList<>();

        for (String strIdentifier : identifiers) {
            Identifier id = Identifier.parse(strIdentifier);

            registry.get(id).ifPresentOrElse(
                    (t) -> values.add(t.value()),
                    () -> FarmTweaks.errorMessage("Failed parsing " + strIdentifier + " from List: " + identifiers)
            );
        }
        return values;
    }
}
