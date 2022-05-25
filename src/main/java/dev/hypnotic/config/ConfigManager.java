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
package dev.hypnotic.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.settings.Setting;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.settings.settingtypes.KeybindSetting;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.ChatUtils;
import dev.hypnotic.utils.ColorUtils;

public class ConfigManager {

	public static final ConfigManager INSTANCE = new ConfigManager();
    private static final List<Config> configs = new ArrayList<>();
    private final File file = new File(Hypnotic.hypnoticDir, "/configs");
    public File config = new File(Hypnotic.hypnoticDir, "/Config.json");
    String[] pathnames;

    public ConfigManager() {
        file.mkdirs();
    }

    public static Config getConfigByName(String name) {
        for (Config config : configs) {
            if (config.getName().equalsIgnoreCase(name)) return config;
        }
        return null;
    }

    public boolean load(String name) {
        Config config = new Config(name);
        try {
            String configString = new String(Files.readAllBytes(config.getFile().toPath()));
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            Mod[] modules = gson.fromJson(configString, Mod[].class);
            HudModule[] hudModules = gson.fromJson(configString, HudModule[].class);
            
            for (Mod module : ModuleManager.INSTANCE.getAllModules()) {
            	for (HudModule configHudModule : hudModules) {
            		HudModule hudMod = (HudModule)module;
                	hudMod.setX(configHudModule.getX());
                	hudMod.setY(configHudModule.getY());
            	}
                for (Mod configModule : modules) {
                    if (module.getName().equals(configModule.getName())) {
                        try {
                            if (configModule.isEnabled() && !module.isEnabled())
                                module.toggle();
                            else if (!configModule.isEnabled() && module.isEnabled())
                                module.setEnabled(false);
                            
                            module.setKey(configModule.getKey());
                            
                            if (module instanceof HudModule) {
                            	HudModule hudMod = (HudModule)module;
                            	HudModule configHudMod = (HudModule)configModule;
                            	hudMod.setX(configHudMod.getX());
                            	hudMod.setY(configHudMod.getY());
                            }
                            
                            for (Setting setting : module.getSettings()) {
                                for (ConfigSetting cfgSetting : configModule.cfgSettings) {
                                    if (setting.name.equals(cfgSetting.name)) {
                                        if (setting instanceof BooleanSetting) {
                                            ((BooleanSetting) setting).setEnabled((boolean) cfgSetting.value);
                                        }
                                        if (setting instanceof ModeSetting) {
                                            ((ModeSetting) setting).setSelected((String) cfgSetting.value);
                                        }
                                        if (setting instanceof NumberSetting) {
                                            ((NumberSetting) setting).setValue((double) cfgSetting.value);
                                        }
                                        if (setting instanceof ColorSetting) {
                                        	int[] color = ((ColorSetting)setting).hexToRgbInt((String)cfgSetting.value);
                                        	((ColorSetting) setting).setRGB(color[0], color[1], color[2], color[3]);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean loadConfig() {
        try {
            String configString = new String(Files.readAllBytes(config.toPath()));
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
            Mod[] modules = gson.fromJson(configString, Mod[].class);
            HudModule[] hudModules = gson.fromJson(configString, HudModule[].class);

            for (Mod module : ModuleManager.INSTANCE.getAllModules()) {
            	if (module instanceof HudModule) {
            		HudModule hudMod = (HudModule)module;
	            	for (HudModule configHudModule : hudModules) {
	            		if (hudMod.getName().equals(configHudModule.getName())) {
		                	hudMod.setX(configHudModule.getX());
		                	hudMod.setY(configHudModule.getY());
	            		}
	            	}
            	}
                for (Mod configModule : modules) {
                    if (module.getName().equals(configModule.getName())) {
                        try {
                            if (configModule.isEnabled() && !module.isEnabled())
                                module.setEnabled(true);
                            else if (!configModule.isEnabled() && module.isEnabled())
                                module.setEnabled(false);
                            
                            module.setKey(configModule.getKey());
                            
                            for (Setting setting : module.getSettings()) {
                                for (ConfigSetting cfgSetting : configModule.cfgSettings) {
                                    if (setting.name.equals(cfgSetting.name)) {
                                        if (setting instanceof BooleanSetting) {
                                            ((BooleanSetting) setting).setEnabled((boolean) cfgSetting.value);
                                        }
                                        if (setting instanceof ModeSetting) {
                                            ((ModeSetting) setting).setSelected((String) cfgSetting.value);
                                        }
                                        if (setting instanceof NumberSetting) {
                                            ((NumberSetting) setting).setValue((double) cfgSetting.value);
                                        }
                                        if (setting instanceof ColorSetting) {
                                        	int[] color = ((ColorSetting)setting).hexToRgbInt((String)cfgSetting.value);
                                        	((ColorSetting) setting).setRGB(color[0], color[1], color[2], color[3]);
                                        }
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean save(String name) {


        Config config = new Config(name);

        try {
            config.getFile().getParentFile().mkdirs();
            Files.write(config.getFile().toPath(), config.serialize().getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save " + config);
            return false;
        }
    }

    public void saveConfig() {
        try {
            config.getParentFile().mkdirs();
            Files.write(config.toPath(), serialize().getBytes(StandardCharsets.UTF_8));

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save " + config);
        }
    }

    public String serialize() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        for (Mod module : ModuleManager.INSTANCE.getAllModules()) {
            List<ConfigSetting> settings = new ArrayList<>();
            for (Setting setting : module.getSettings()) {
                if (setting instanceof KeybindSetting)
                    continue;

                ConfigSetting cfgSetting = new ConfigSetting(null, null);
                cfgSetting.name = setting.name;
                if (setting instanceof BooleanSetting) {
                    cfgSetting.value = ((BooleanSetting) setting).isEnabled();
                }
                if (setting instanceof ModeSetting) {
                    cfgSetting.value = ((ModeSetting) setting).getSelected();
                }
                if (setting instanceof NumberSetting) {
                    cfgSetting.value = ((NumberSetting) setting).getValue();
                }
                if (setting instanceof ColorSetting) {
                	ColorSetting colorSet = (ColorSetting)setting;
                	cfgSetting.value = colorSet.getHex();
                }
                settings.add(cfgSetting);
            }
            module.cfgSettings = settings.toArray(new ConfigSetting[0]);
        }
        return gson.toJson(ModuleManager.INSTANCE.getAllModules());
    }

    public boolean save(Config config) {
        return this.save(config);
    }

    public void saveAll() {
        configs.forEach(config -> save(config.getName()));
    }

    public void loadConfigs() {
    	configs.clear();
        for (File file : file.listFiles()) {
            configs.add(new Config(file.getName().replace(".json", "")));
        }
    }

    public List<Config> getConfigs() {
        return configs;
    }

    public void list() {
        pathnames = file.list();
        for (String pathname : pathnames) {
        	System.out.println(pathname.substring(0, pathname.length() - 5));
            ChatUtils.tellPlayerRaw(ColorUtils.red + "Config" + ColorUtils.gray + ": " + pathname.substring(0, pathname.length() - 5));
        }
    }

    public void delete(String configName) {
        Config config = new Config(configName);
        try {
            Files.deleteIfExists(config.getFile().toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
