package powercyphe.farmtweaks;


import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FarmTweaks implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("farmtweaks");
	public static final String MOD_ID = "farmtweaks";

	@Override
	public void onInitialize() {
		MidnightConfig.init(MOD_ID, ModConfig.class);
	}

	public static void errorMessage(String message) {
		LOGGER.error("[" + MOD_ID + "] " + message);
	}
}