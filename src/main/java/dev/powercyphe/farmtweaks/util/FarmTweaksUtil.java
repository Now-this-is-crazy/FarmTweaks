package dev.powercyphe.farmtweaks.util;

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
import dev.powercyphe.farmtweaks.FarmTweaks;
import dev.powercyphe.farmtweaks.FarmTweaksConfig;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static dev.powercyphe.farmtweaks.FarmTweaks.LOGGER;

public class FarmTweaksUtil {

    public static boolean allowAltHarvest() {
        return FarmTweaksConfig.allowAlternateHarvest;
    }

    public static boolean fastLeafDecay() {
        return FarmTweaksConfig.fastLeafDecay;
    }

    public static int leafDecaySpeed() {
        return FarmTweaksConfig.leafDecaySpeed;
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

    public static boolean canBonemeal(Block block) {
        return FarmTweaksConfig.enhancedBonemeal && !(getNonBonemealableBlocks().contains(block));
    }

    public static List<Block> getNonBonemealableBlocks() {
        return getParsedList(BuiltInRegistries.BLOCK, FarmTweaksConfig.nonBonemealableBlocks);
    }

    public static boolean shouldGrowSwampTree() {
        return FarmTweaksConfig.saplingsGrowSwampTrees;
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

    public static <T> void scanDirectory(
            final ResourceManager manager, final FileToIdConverter lister, final DynamicOps<JsonElement> ops, final Codec<T> codec, final Map<Identifier, T> result
    ) {
        for (Map.Entry<Identifier, Resource> entry : lister.listMatchingResources(manager).entrySet()) {
            Identifier location = entry.getKey();
            Identifier id = lister.fileToId(location);

            try {
                Reader reader = entry.getValue().openAsReader();

                try {
                    codec.parse(ops, StrictJsonParser.parse(reader)).ifSuccess(parsed -> {
                        if (result.putIfAbsent(id, parsed) != null) {
                            throw new IllegalStateException("Duplicate data file ignored with ID " + id);
                        }
                    }).ifError(error -> LOGGER.error("Couldn't parse data file '{}' from '{}': {}", new Object[]{id, location, error}));
                } catch (Throwable var13) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var12) {
                            var13.addSuppressed(var12);
                        }
                    }

                    throw var13;
                }

                if (reader != null) {
                    reader.close();
                }
            } catch (JsonParseException | IllegalArgumentException | IOException e) {
                LOGGER.error("Couldn't parse data file '{}' from '{}'", new Object[]{id, location, e});
            }
        }
    }
}
