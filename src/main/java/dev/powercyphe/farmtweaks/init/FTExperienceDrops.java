package dev.powercyphe.farmtweaks.init;

import dev.powercyphe.farmtweaks.reloadlistener.data.ExperienceDrop;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public interface FTExperienceDrops {

    ResourceKey<ExperienceDrop> WHEAT = register(Blocks.WHEAT);
    ResourceKey<ExperienceDrop> CARROTS = register(Blocks.CARROTS);
    ResourceKey<ExperienceDrop> POTATOES = register(Blocks.POTATOES);
    ResourceKey<ExperienceDrop> BEETROOTS = register(Blocks.BEETROOTS);
    ResourceKey<ExperienceDrop> COCOA = register(Blocks.COCOA);

    static void init() {}

    static ResourceKey<ExperienceDrop> register(Block block) {
        return register(block.builtInRegistryHolder().key().identifier());
    }

    static ResourceKey<ExperienceDrop> register(Identifier id) {
        return ResourceKey.create(FTRegistries.EXPERIENCE_DROP, id);
    }
}
