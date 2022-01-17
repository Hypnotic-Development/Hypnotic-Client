/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic;

import static dev.hypnotic.utils.MCUtils.mc;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
}
