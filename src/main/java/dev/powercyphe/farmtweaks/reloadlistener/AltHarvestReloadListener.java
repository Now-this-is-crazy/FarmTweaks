package dev.powercyphe.farmtweaks.reloadlistener;

import com.mojang.serialization.JsonOps;
import dev.powercyphe.farmtweaks.init.FTRegistries;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.SimpleReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.item.Item;
import dev.powercyphe.farmtweaks.reloadlistener.data.AltHarvest;

import java.util.HashMap;
import java.util.Map;

public class AltHarvestReloadListener extends SimpleReloadListener<Map<Item, AltHarvest>> {
    @Override
    protected Map<Item, AltHarvest> prepare(SharedState state) {
        Map<Identifier, AltHarvest> raw = new HashMap<>();
        SimpleJsonResourceReloadListener.scanDirectory(state.resourceManager(),
                FileToIdConverter.registry(FTRegistries.ALT_HARVEST),
                state.get(ResourceLoader.REGISTRY_LOOKUP_KEY).createSerializationContext(JsonOps.INSTANCE),
                AltHarvest.CODEC, raw
        );

        Map<Item, AltHarvest> prepared = new HashMap<>();
        raw.forEach((id, harvest) -> BuiltInRegistries.ITEM.getOptional(id).ifPresent(item -> {
            if (harvest.allowed()) {
                prepared.put(item, harvest);
            }
        }));

        return prepared;
    }

    @Override
    protected void apply(Map<Item, AltHarvest> prepared, SharedState state) {
        AltHarvest.VALUES.clear();
        AltHarvest.VALUES.putAll(prepared);
    }
}
