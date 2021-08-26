package badgamesinc.hypnotic.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.settings.Setting;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.KeybindSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;

public class ConfigManager {

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

            for (Mod module : ModuleManager.INSTANCE.getAllModules()) {
                for (Mod configModule : modules) {
                    if (module.getName().equals(configModule.getName())) {
                        try {
                            if (configModule.isEnabled() && !module.isEnabled())
                                module.toggle();
                            else if (!configModule.isEnabled() && module.isEnabled())
                                module.setEnabled(false);
                            
                            for (Setting setting : module.settings) {
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

            for (Mod module : ModuleManager.INSTANCE.getAllModules()) {
                for (Mod configModule : modules) {
                    if (module.getName().equals(configModule.getName())) {
                        try {
                            if (configModule.isEnabled() && !module.isEnabled())
                                module.setEnabled(true);
                            else if (!configModule.isEnabled() && module.isEnabled())
                                module.setEnabled(false);
                            for (Setting setting : module.settings) {
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
            for (Setting setting : module.settings) {
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
//            Wrapper.tellPlayer(pathname.substring(0, pathname.length() - 5));
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
