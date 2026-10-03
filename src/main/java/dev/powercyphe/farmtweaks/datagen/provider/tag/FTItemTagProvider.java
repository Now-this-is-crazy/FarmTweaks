package dev.powercyphe.farmtweaks.datagen.provider.tag;

import dev.powercyphe.farmtweaks.compat.FTBuiltInCompat;
import dev.powercyphe.farmtweaks.init.FTTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.HolderLookup;
import org.jspecify.annotations.Nullable;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.concurrent.CompletableFuture;

public class FTItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
    public FTItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture, @Nullable BlockTagsProvider blockTagsProvider) {
        super(output, registryLookupFuture, blockTagsProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(FTTags.GRASS_SEEDS)
                .forceAddTag(ConventionalItemTags.SEEDS);

        if (FTBuiltInCompat.FARMERS_DELIGHT) {
            builder(FTTags.GRASS_SEEDS).add(ModItems.RICE.get().builtInRegistryHolder().key());
        }
    }
}
