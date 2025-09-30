package powercyphe.farmtweaks;


import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import powercyphe.farmtweaks.event.AlternateHoeUseEvent;

public class FarmTweaks implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("farmtweaks");
	public static final String MOD_ID = "farmtweaks";

	public static final DefaultedList<Pair<Block, Block>> GRASS_CONVERTABLE = DefaultedList.of();

	public static final TagKey<Item> ALLOWS_ALTERNATE_HOE_USE = TagKey.of(RegistryKeys.ITEM, id("allows_alternate_hoe_use"));
	public static final TagKey<Item> GRASS_SEEDS_TAG = TagKey.of(RegistryKeys.ITEM, id("grass_seeds"));

	@Override
	public void onInitialize() {
		MidnightConfig.init(MOD_ID, FarmTweaksConfig.class);

		GRASS_CONVERTABLE.add(new Pair<>(Blocks.DIRT, Blocks.GRASS_BLOCK));
		GRASS_CONVERTABLE.add(new Pair<>(Blocks.COARSE_DIRT, Blocks.PODZOL));
		GRASS_CONVERTABLE.add(new Pair<>(Blocks.ROOTED_DIRT, Blocks.MYCELIUM));

		UseBlockCallback.EVENT.register(new AlternateHoeUseEvent());
	}

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	public static void errorMessage(String message) {
		LOGGER.error("[" + MOD_ID + "] " + message);
	}
}