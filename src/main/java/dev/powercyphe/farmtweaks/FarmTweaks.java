package dev.powercyphe.farmtweaks;


import dev.powercyphe.farmtweaks.compat.FTBuiltInCompat;
import dev.powercyphe.farmtweaks.init.*;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.ItemEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import dev.powercyphe.farmtweaks.event.*;
import dev.powercyphe.farmtweaks.reloadlistener.AltHarvestReloadListener;
import dev.powercyphe.farmtweaks.reloadlistener.HarvestableReloadListener;
import dev.powercyphe.farmtweaks.reloadlistener.ReplenishableReloadListener;

public class FarmTweaks implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("farmtweaks");
	public static final String MOD_ID = "farmtweaks";

	@Override
	public void onInitialize() {
		MidnightConfig.init(MOD_ID, FarmTweaksConfig.class);

		FTRegistries.init();
		FTTags.init();

		FTAltHarvests.init();
		FTHarvestables.init();
		FTReplenishables.init();

		FTBuiltInCompat.init();

		// DO THE DISPENSER DATADRIVEN !!!!

		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(id("alt_harvest"), new AltHarvestReloadListener());
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(id("harvestable"), new HarvestableReloadListener());
		ResourceLoader.get(PackType.SERVER_DATA).registerReloadListener(id("replenishable"), new ReplenishableReloadListener());

		ItemEvents.USE_ON.register(new AlternateHoeUseEvent());
		ItemEvents.USE_ON.register(new BonemealDuplicationEvent());
		ItemEvents.USE_ON.register(new BonemealGrowEvent());
		ItemEvents.USE_ON.register(new ReplenishableEvent());

		PlayerBlockBreakEvents.BEFORE.register(CropExperienceEvent.INSTANCE);
		PlayerBlockBreakEvents.AFTER.register(CropExperienceEvent.INSTANCE);
		PlayerBlockBreakEvents.CANCELED.register(CropExperienceEvent.INSTANCE);

        ServerTickEvents.END_LEVEL_TICK.register(LeafDecayEvent.get());
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void errorMessage(String message) {
        LOGGER.error(message);
	}
}