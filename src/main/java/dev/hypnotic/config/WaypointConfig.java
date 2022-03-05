package dev.hypnotic.config;

import java.io.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.waypoint.Waypoint;
import dev.hypnotic.waypoint.WaypointManager;

public class WaypointConfig extends Config {

    public WaypointConfig(String name) {
        super(name);
        this.file = new File(Hypnotic.hypnoticDir, "/configs/waypoints/" + name + ".json");
    }

    @Override
    public String serialize() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
        
        for (Waypoint waypoint : WaypointManager.INSTANCE.waypoints) {
            
        }
        return null;
    }
}