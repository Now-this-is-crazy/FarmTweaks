package dev.powercyphe.farmtweaks.datagen.provider.tag;

import dev.powercyphe.farmtweaks.init.FTTags;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class FTBlockTagProvider extends FabricTagsProvider.BlockTagsProvider {
    public FTBlockTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registryLookupFuture) {
        super(output, registryLookupFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        valueLookupBuilder(FTTags.BONEMEAL_DUPLICATABLE)
                .add(Blocks.LILY_PAD)
                .add(Blocks.SPORE_BLOSSOM)
                .add(Blocks.HANGING_ROOTS)

                .add(Blocks.NETHER_SPROUTS)
                .add(Blocks.CRIMSON_ROOTS)
                .add(Blocks.WARPED_ROOTS)

                .forceAddTag(BlockTags.SMALL_FLOWERS)
                .forceAddTag(BlockTags.CORALS)
                .forceAddTag(BlockTags.CORAL_PLANTS)
                .forceAddTag(BlockTags.WALL_CORALS);
    }
}
