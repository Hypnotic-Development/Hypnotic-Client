package dev.hypnotic.config;

import java.io.File;

import dev.hypnotic.Hypnotic;

public class HudConfig extends Config {

    public HudConfig(String name) {
        super(name);
        this.file = new File(Hypnotic.hypnoticDir, "/configs/hud/" + name + ".json");
    }

    @Override
    public String serialize() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        
        for (HudModule mod : HudManager.INSTANE.hudModules) {
            
        }
    }
}