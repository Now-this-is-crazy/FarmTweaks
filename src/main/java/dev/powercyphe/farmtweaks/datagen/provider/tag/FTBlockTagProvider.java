package dev.powercyphe.farmtweaks.datagen.provider.tag;

import dev.powercyphe.farmtweaks.init.FTTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.references.BlockItemIds;
import net.minecraft.tags.BlockTags;

import java.util.concurrent.CompletableFuture;

public class FTBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public FTBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        builder(FTTags.BONEMEAL_DUPLICATABLE)
                .add(BlockItemIds.LILY_PAD)
                .add(BlockItemIds.SPORE_BLOSSOM)
                .add(BlockItemIds.HANGING_ROOTS)

                .add(BlockItemIds.NETHER_SPROUTS)
                .add(BlockItemIds.CRIMSON_ROOTS)
                .add(BlockItemIds.WARPED_ROOTS)

                .forceAddTag(BlockTags.SMALL_FLOWERS)
                .forceAddTag(BlockTags.CORALS)
                .forceAddTag(BlockTags.CORAL_PLANTS)
                .forceAddTag(BlockTags.WALL_CORALS);

        builder(BlockTags.TURNS_INTO_FARMLAND)
                .add(BlockItemIds.PODZOL)
                .add(BlockItemIds.MYCELIUM);
    }
}
