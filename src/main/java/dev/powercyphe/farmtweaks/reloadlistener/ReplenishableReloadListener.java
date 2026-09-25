package dev.powercyphe.farmtweaks.reloadlistener;

import com.mojang.serialization.JsonOps;
import dev.powercyphe.farmtweaks.init.FTRegistries;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.SimpleReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.level.block.Block;
import dev.powercyphe.farmtweaks.FarmTweaks;
import dev.powercyphe.farmtweaks.reloadlistener.data.Replenishable;

import java.util.HashMap;
import java.util.Map;

public class ReplenishableReloadListener extends SimpleReloadListener<Map<Block, Replenishable>> {
    @Override
    protected Map<Block, Replenishable> prepare(SharedState state) {
        Map<Identifier, Replenishable> raw = new HashMap<>();
        SimpleJsonResourceReloadListener.scanDirectory(state.resourceManager(),
                FileToIdConverter.registry(FTRegistries.REPLENISHABLE),
                state.get(ResourceLoader.REGISTRY_LOOKUP_KEY).createSerializationContext(JsonOps.INSTANCE),
                Replenishable.CODEC, raw
        );

        Map<Block, Replenishable> prepared = new HashMap<>();
        raw.forEach((id, repl) -> BuiltInRegistries.BLOCK.get(id).ifPresent(block ->
                prepared.put(block.value(), repl)

        ));
        return prepared;
    }

    @Override
    protected void apply(Map<Block, Replenishable> prepared, SharedState state) {
        Replenishable.VALUES.clear();
        Replenishable.VALUES.putAll(prepared);

    }
}
