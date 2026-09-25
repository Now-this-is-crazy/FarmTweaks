package dev.powercyphe.farmtweaks.init;

import com.mojang.serialization.Codec;
import dev.powercyphe.farmtweaks.FarmTweaks;
import dev.powercyphe.farmtweaks.reloadlistener.data.AltHarvest;
import dev.powercyphe.farmtweaks.reloadlistener.data.ExperienceDrop;
import dev.powercyphe.farmtweaks.reloadlistener.data.Harvestable;
import dev.powercyphe.farmtweaks.reloadlistener.data.Replenishable;
import net.fabricmc.fabric.api.event.registry.DynamicRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface FTRegistries {

    ResourceKey<Registry<AltHarvest>> ALT_HARVEST = register("alt_harvest", AltHarvest.CODEC);
    ResourceKey<Registry<Harvestable>> HARVESTABLE = register("harvestable", Harvestable.CODEC);
    ResourceKey<Registry<Replenishable>> REPLENISHABLE = register("replenishable", Replenishable.CODEC);
    ResourceKey<Registry<ExperienceDrop>> EXPERIENCE_DROP = register("experience_drop", ExperienceDrop.CODEC);

    static void init() {}

    static <T> ResourceKey<Registry<T>> register(String name, Codec<T> codec) {
        ResourceKey<Registry<T>> registry = ResourceKey.createRegistryKey(FarmTweaks.id(name));
        DynamicRegistries.register(registry, codec);
        return registry;
    }
}
