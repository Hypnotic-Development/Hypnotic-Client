package badgamesinc.hypnotic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import badgamesinc.hypnotic.config.ConfigManager;
import badgamesinc.hypnotic.config.SaveLoad;
import badgamesinc.hypnotic.event.EventManager;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.render.CustomFont;
import badgamesinc.hypnotic.ui.HUD;
import badgamesinc.hypnotic.ui.altmanager.altmanager2.AltsFile;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.api.ApiUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import badgamesinc.hypnotic.utils.input.MouseUtils;
import badgamesinc.hypnotic.utils.player.DamageUtils;
import badgamesinc.hypnotic.utils.world.BlockIterator;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Hypnotic implements ModInitializer {

	public static Hypnotic INSTANCE = new Hypnotic();
	public static Executor EXECUTOR = Executors.newCachedThreadPool();
	public static String name = "Hypnotic",
			version = "r1000",
			fullName = name + "-" + version,
			hypnoticDir = System.getenv("APPDATA") + "/.minecraft/Hypnotic",
			chatPrefix = ColorUtils.purple + name + ColorUtils.gray + ": ";
	public ModuleManager moduleManager;
	public EventManager eventManager;
	public ConfigManager cfgManager;
	public SaveLoad saveload;
	public ApiUtils api = new ApiUtils();
	
	public static final Identifier BOOM_SOUND = new Identifier("tutorial:boom");
    public static SoundEvent BOOM_SOUND_EVENT = new SoundEvent(BOOM_SOUND);
    public List<String> users = new ArrayList<>();

	/*
	 * Called when Minecraft initializes.
	 * This is called AFTER mixins are injected
	 * Should probably inject the register function 
	 * into MinecraftClient.java but i'm too lazy
	 */
	@Override
	public void onInitialize() {
		@SuppressWarnings("unused")
		MinecraftClient mc = MinecraftClient.getInstance();
		System.out.println("Loading Hypnotic stuff");
		Registry.register(Registry.SOUND_EVENT, Hypnotic.BOOM_SOUND, BOOM_SOUND_EVENT);
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
		EventManager.INSTANCE.register(HUD.INSTANCE);
		EventManager.INSTANCE.register(DamageUtils.getInstance());
		EventManager.INSTANCE.register(BlockIterator.INSTANCE);
		EventManager.INSTANCE.register(MouseUtils.class);
		
	}
	
	/*
	 * Loads all* of the stuff that should be saved
	 */
	public void loadFiles() {
		if (cfgManager.config.exists()) {
            cfgManager.loadConfig();
        }
        configDaemon.setDaemon(true);
        configDaemon.start();
        AltsFile.INSTANCE.loadAlts();
        saveload.load();
        Thread configDaemon = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cfgManager.saveConfig();
                saveload.save();
            }
        });
        if (ModuleManager.INSTANCE.getModule(CustomFont.class).isEnabled()) FontManager.setMcFont(false);
        else FontManager.setMcFont(true);
	}

	public void shutdown() {
		System.out.println("SHUTING DOWN HYPNOTIC, GOODBYE");
		cfgManager.saveConfig();
        saveload.save();
		AltsFile.INSTANCE.saveAlts();
	}
	
	public static boolean isHypnoticUser(String name) {
		return Hypnotic.INSTANCE.users.contains(name);
	}
	
	public static void setHypnoticUser(String name, boolean using) {
		if (using) Hypnotic.INSTANCE.users.add(name);
		else if (!using && Hypnotic.INSTANCE.users.contains(name)) Hypnotic.INSTANCE.users.remove(name);
	}
}