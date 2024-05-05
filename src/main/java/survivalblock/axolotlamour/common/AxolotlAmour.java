package survivalblock.axolotlamour.common;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import survivalblock.axolotlamour.common.init.AxolotlAmourItems;

public class AxolotlAmour implements ModInitializer {
	public static final String MOD_ID = "axolotl_amour";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static boolean hasTameableAxolotlsLoaded = false;

	@Override
	public void onInitialize() {
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) LOGGER.info("Axolotl, mon amour");
		AxolotlAmourItems.init();
		hasTameableAxolotlsLoaded = FabricLoader.getInstance().isModLoaded("tameable_axolotls");
	}

	public static Identifier id(String name) {
		return new Identifier(MOD_ID, name);
	}
}