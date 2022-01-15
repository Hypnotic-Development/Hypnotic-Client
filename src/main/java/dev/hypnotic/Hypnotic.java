package dev.hypnotic;

import static dev.hypnotic.utils.MCUtils.mc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.graalvm.polyglot.HostAccess;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.hypnotic.config.ConfigManager;
import dev.hypnotic.config.SaveLoad;
import dev.hypnotic.event.EventManager;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.render.CustomFont;
import dev.hypnotic.scripting.ScriptManager;
import dev.hypnotic.ui.HUD;
import dev.hypnotic.ui.altmanager.altmanager2.AltsFile;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.input.MouseUtils;
import dev.hypnotic.utils.player.DamageUtils;
import dev.hypnotic.utils.world.BlockIterator;
import net.fabricmc.api.ModInitializer;

public class Hypnotic implements ModInitializer {

	public static Hypnotic INSTANCE = new Hypnotic();
	public static Executor EXECUTOR = Executors.newCachedThreadPool();
	public static Logger LOGGER = LogManager.getLogger("Hypnotic");
	public static String name = "Hypnotic",
			version = "r1000",
			fullName = name + "-" + version,
			hypnoticDir = mc.runDirectory.getPath() + File.separator + "Hypnotic",
			scriptDir = hypnoticDir + "/scripts",
			chatPrefix = ColorUtils.red + name + ColorUtils.gray + ": ";
	public ModuleManager moduleManager;
	public ScriptManager scriptManager;
	public EventManager eventManager;
	public ConfigManager cfgManager;
	public SaveLoad saveload;
	
    public List<String> users = new ArrayList<>();

	/*
	 * Called when Minecraft initializes.
	 * This is called AFTER mixins are injected
	 * Should probably inject the register function 
	 * into MinecraftClient.java but i'm too lazy
	 */
	@Override
	public void onInitialize() {
		System.out.println("Loading Hypnotic stuff");
		register();
		loadFiles();
	}

	/*
	 * Registers all of the good stuff
	 */
	public void register() {
		moduleManager = ModuleManager.INSTANCE;
		scriptManager = ScriptManager.INSTANCE;
		eventManager = EventManager.INSTANCE;
		cfgManager = new ConfigManager();
		saveload = new SaveLoad();
		EventManager.INSTANCE.register(HUD.INSTANCE);
		EventManager.INSTANCE.register(DamageUtils.getInstance());
		EventManager.INSTANCE.register(BlockIterator.INSTANCE);
		EventManager.INSTANCE.register(MouseUtils.class);
		
		ScriptManager.INSTANCE.makeScriptsFolder();
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
                saveload.save();
            }
        });
        configDaemon.setDaemon(true);
        configDaemon.start();
        AltsFile.INSTANCE.loadAlts();
        saveload.load();
        if (ModuleManager.INSTANCE.getModule(CustomFont.class).isEnabled()) FontManager.setMcFont(false);
        else FontManager.setMcFont(true);
	}

	public void shutdown() {
		System.out.println("SHUTING DOWN HYPNOTIC, GOODBYE");
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
