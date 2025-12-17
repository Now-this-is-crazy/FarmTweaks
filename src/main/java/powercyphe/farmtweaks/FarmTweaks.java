package powercyphe.farmtweaks;


import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
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
import powercyphe.farmtweaks.event.AlternateHoeUseEvent;

public class FarmTweaks implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("farmtweaks");
	public static final String MOD_ID = "farmtweaks";

	public static final NonNullList<Tuple<Block, Block>> GRASS_CONVERTABLE = NonNullList.create();

	public static final TagKey<Item> ALLOWS_ALTERNATE_HOE_USE = TagKey.create(Registries.ITEM, id("allows_alternate_hoe_use"));
	public static final TagKey<Item> GRASS_SEEDS_TAG = TagKey.create(Registries.ITEM, id("grass_seeds"));

	@Override
	public void onInitialize() {
		MidnightConfig.init(MOD_ID, FarmTweaksConfig.class);

		GRASS_CONVERTABLE.add(new Tuple<>(Blocks.DIRT, Blocks.GRASS_BLOCK));
		GRASS_CONVERTABLE.add(new Tuple<>(Blocks.COARSE_DIRT, Blocks.PODZOL));
		GRASS_CONVERTABLE.add(new Tuple<>(Blocks.ROOTED_DIRT, Blocks.MYCELIUM));

		UseBlockCallback.EVENT.register(new AlternateHoeUseEvent());
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	public static void errorMessage(String message) {
        LOGGER.error("[" + MOD_ID + "] {}", message);
	}
}