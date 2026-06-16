package powercyphe.farmtweaks;


import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import powercyphe.farmtweaks.event.*;
import powercyphe.farmtweaks.init.FTTags;

public class FarmTweaks implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("farmtweaks");
	public static final String MOD_ID = "farmtweaks";

	@Override
	public void onInitialize() {
		MidnightConfig.init(MOD_ID, FarmTweaksConfig.class);
		FTTags.init();

		ItemEvents.USE_ON.register(new AlternateHoeUseEvent());
		ItemEvents.USE_ON.register(new BonemealDuplicationEvent());
		ItemEvents.USE_ON.register(new BonemealGrowEvent());
		ItemEvents.USE_ON.register(new ConvertToGrassEvent());

        ServerTickEvents.END_LEVEL_TICK.register(LeafDecayEvent.get());
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void errorMessage(String message) {
        LOGGER.error(message);
	}
}