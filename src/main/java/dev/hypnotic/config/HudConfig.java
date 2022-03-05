package dev.hypnotic.config;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.module.hud.HudManager;
import dev.hypnotic.module.hud.HudModule;

public class HudConfig extends Config {

    public HudConfig(String name) {
        super(name);
        this.file = new File(Hypnotic.hypnoticDir, "/configs/hud/" + name + ".json");
    }

    @Override
    public String serialize() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        
        for (HudModule mod : HudManager.INSTANCE.hudModules) {
            
        }
		return gson.toString();
    }
}
