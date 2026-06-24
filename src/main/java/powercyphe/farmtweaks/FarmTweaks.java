package powercyphe.farmtweaks;


import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import powercyphe.farmtweaks.event.*;
import powercyphe.farmtweaks.init.FTTags;
import powercyphe.farmtweaks.reloadlistener.AltHarvestReloadListener;
import powercyphe.farmtweaks.reloadlistener.HarvestableReloadListener;
import powercyphe.farmtweaks.reloadlistener.ReplenishableReloadListener;

public class FarmTweaks implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("farmtweaks");
	public static final String MOD_ID = "farmtweaks";

	@Override
	public void onInitialize() {
		MidnightConfig.init(MOD_ID, FarmTweaksConfig.class);

		// DO THE DISPENSER DATADRIVEN !!!!

		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(id("alt_harvest"), new AltHarvestReloadListener());
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(id("harvestable"), new HarvestableReloadListener());
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(id("replenishable"), new ReplenishableReloadListener());

		FTTags.init();

		ItemEvents.USE_ON.register(new AlternateHoeUseEvent());
		ItemEvents.USE_ON.register(new BonemealDuplicationEvent());
		ItemEvents.USE_ON.register(new BonemealGrowEvent());
		ItemEvents.USE_ON.register(new ReplenishableEvent());

        ServerTickEvents.END_LEVEL_TICK.register(LeafDecayEvent.get());
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void errorMessage(String message) {
        LOGGER.error(message);
	}
}