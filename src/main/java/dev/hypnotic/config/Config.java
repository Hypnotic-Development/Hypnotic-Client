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
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.settings.Setting;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.settings.settingtypes.KeybindSetting;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;

public class Config {

    private final String name;
    protected File file;

    public Config(String name) {
        this.name = name;
        this.file = new File(Hypnotic.hypnoticDir, "/configs/" + name + ".json");
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
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
                	cfgSetting.value = ((ColorSetting) setting).getHex();
                }

                settings.add(cfgSetting);
            }
            module.cfgSettings = settings.toArray(new ConfigSetting[0]);
        }
        return gson.toJson(ModuleManager.INSTANCE.getAllModules());
    }

}
