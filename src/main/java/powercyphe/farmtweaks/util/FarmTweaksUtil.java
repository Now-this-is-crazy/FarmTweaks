package powercyphe.farmtweaks.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StrictJsonParser;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import powercyphe.farmtweaks.FarmTweaks;
import powercyphe.farmtweaks.FarmTweaksConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class FarmTweaksUtil {

    public static boolean allowAltHarvest() {
        return FarmTweaksConfig.allowAlternateHarvest;
    }

    public static boolean rangedAltHarvest() {
        return FarmTweaksConfig.rangedAlternateHarvest;
    }

    public static boolean allowFarmLandTrampling() {
        return FarmTweaksConfig.allowFarmlandTrampling;
    }

    public static boolean useSmartPathMaking() {
        return FarmTweaksConfig.smartPathMaking;
    }

    public static boolean allowReplenishment() {
        return FarmTweaksConfig.allowReplenishment;
    }

    public static void dropExp(Level world, BlockPos pos) {
        if (RandomSource.create().nextInt(100) < FarmTweaksConfig.cropExperienceChance) {
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
