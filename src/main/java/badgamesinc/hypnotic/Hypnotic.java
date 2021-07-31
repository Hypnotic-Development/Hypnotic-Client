package badgamesinc.hypnotic;

import badgamesinc.hypnotic.config.ConfigManager;
import badgamesinc.hypnotic.config.SaveLoad;
import badgamesinc.hypnotic.event.EventManager;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.ui.altmanager.AltsFile;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.text.Fonts;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;

public class Hypnotic implements ModInitializer {

	public static Hypnotic INSTANCE = new Hypnotic();
	public static String name = "Hypnotic",
			version = "r1000",
			fullName = name + "-" + version,
			hypnoticDir = System.getenv("APPDATA") + "/.minecraft/Hypnotic",
			chatPrefix = ColorUtils.purple + name + ColorUtils.gray + ": ";
	public ModuleManager moduleManager;
	public EventManager eventManager;
	public ConfigManager cfgManager;
	public SaveLoad saveload;

	/*
	 * Called when Minecraft initializes.
	 * This is called AFTER mixins are injected
	 * Should probably inject the register function 
	 * into MinecraftClient.java but i'm too lazy
	 */
	@Override
	public void onInitialize() {
		MinecraftClient mc = MinecraftClient.getInstance();
		System.out.println("Loading Hypnotic stuff");
		register();
		loadFiles();	
	}

	/*
	 * Registers all of the good stuff
	 */
	public void register() {
		moduleManager = ModuleManager.INSTANCE;
		eventManager = EventManager.INSTANCE;
		cfgManager = new ConfigManager();
		saveload = new SaveLoad();
	}
	
	/*
	 * Loads all* of the stuff that should be saved
	 */
	public void loadFiles() {
		if (cfgManager.config.exists()) {
            cfgManager.loadConfig();
        }
        Thread configDaemon = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cfgManager.saveConfig();
            }
        });
        configDaemon.setDaemon(true);
        configDaemon.start();
        AltsFile.INSTANCE.loadAlts();
        saveload.load();
	}
}
