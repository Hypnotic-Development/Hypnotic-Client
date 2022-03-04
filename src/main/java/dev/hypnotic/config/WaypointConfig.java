package dev.hypnotic.config;

import java.io.File;

import dev.hypnotic.Hypnotic;

public class WaypointConfig extends Config {

    public WaypointConfig(String name) {
        super(name);
        this.file = new File(Hypnotic.hypnoticDir, "/configs/waypoints/" + name + ".json");
    }

    @Override
    public String serialize() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        
        for (Waypoint waypoint : WaypointManager.INSTANE.waypoints) {
            
        }
    }
}