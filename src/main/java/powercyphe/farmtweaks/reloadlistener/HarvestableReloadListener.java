package powercyphe.farmtweaks.reloadlistener;

import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.fabricmc.fabric.api.resource.v1.reloader.SimpleReloadListener;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.level.block.Block;
import powercyphe.farmtweaks.FarmTweaks;
import powercyphe.farmtweaks.util.Harvestable;

import java.util.HashMap;
import java.util.Map;

public class HarvestableReloadListener extends SimpleReloadListener<Map<Block, Harvestable>> {
    public static final String DIRECTORY = FarmTweaks.MOD_ID + "/harvestable";

    @Override
    protected Map<Block, Harvestable> prepare(SharedState state) {
        Map<Identifier, Harvestable> raw = new HashMap<>();
        SimpleJsonResourceReloadListener.scanDirectory(state.resourceManager(), FileToIdConverter.json(DIRECTORY),
                state.get(ResourceLoader.REGISTRY_LOOKUP_KEY).createSerializationContext(JsonOps.INSTANCE),
                Harvestable.CODEC, raw
        );

        Map<Block, Harvestable> prepared = new HashMap<>();
        raw.forEach((id, harv) -> BuiltInRegistries.BLOCK.getOptional(id).ifPresent(block -> {
            if (harv.allowed()) {
                prepared.put(block, harv);
            }
        }));

        return prepared;
    }

    @Override
    protected void apply(Map<Block, Harvestable> prepared, SharedState state) {
        Harvestable.VALUES.clear();
        Harvestable.VALUES.putAll(prepared); 
    }
}
