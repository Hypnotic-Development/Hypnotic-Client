package dev.hypnotic.hypnotic_client.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import dev.hypnotic.hypnotic_client.Hypnotic;
import dev.hypnotic.hypnotic_client.config.friends.Friend;
import dev.hypnotic.hypnotic_client.config.friends.FriendManager;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.module.ModuleManager;
import dev.hypnotic.hypnotic_client.module.hud.HudManager;
import dev.hypnotic.hypnotic_client.module.hud.HudModule;
import dev.hypnotic.hypnotic_client.ui.HudEditorScreen;
import dev.hypnotic.hypnotic_client.ui.clickgui.ModuleButton;
import dev.hypnotic.hypnotic_client.ui.clickgui2.ClickGUI;
import dev.hypnotic.hypnotic_client.ui.clickgui2.frame.Frame;
import dev.hypnotic.hypnotic_client.waypoint.Waypoint;
import dev.hypnotic.hypnotic_client.waypoint.WaypointManager;
import net.minecraft.util.math.BlockPos;

public class SaveLoad {
    public File dir;
    public File configs;
    public File dataFile;

    public static SaveLoad INSTANCE = new SaveLoad();
    //Currently saves keybinds, hud positions, friends, and frame positions
    
    public SaveLoad() {
        dir = new File(Hypnotic.hypnoticDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        configs = new File(Hypnotic.hypnoticDir);
        if (!configs.exists()) {
            configs.mkdir();
        }
        dataFile = new File(configs, "data.txt");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {e.printStackTrace();}
        }
    }


    public void save() {

    	Hypnotic.LOGGER.info("Saving...");
        ArrayList<String> toSave = new ArrayList<String>();

        for (Mod mod : ModuleManager.INSTANCE.modules) {
            toSave.add("MOD:" + mod.getName() + ":" + mod.isEnabled() + ":" + mod.getKey());
        }
        
        for (Friend friend : FriendManager.INSTANCE.friends) {
        	toSave.add("FRIEND:" + friend.name);
        }
        
        for (HudModule element : HudManager.INSTANCE.hudModules) {
        	toSave.add("HUD:" + element.getName() + ":" + element.getX() + ":" + element.getY());
        }
        
        for (Frame frame : ClickGUI.INSTANCE.frames) {
        	toSave.add("FRAME:" + frame.name + ":" + frame.getX() + ":" + frame.getY() + ":" + frame.isExtended());
        }
        
        toSave.add("FRAME:" + HudEditorScreen.INSTANCE.frame.name + ":" + HudEditorScreen.INSTANCE.frame.getX() + ":" + HudEditorScreen.INSTANCE.frame.getY() + ":" + HudEditorScreen.INSTANCE.frame.isExtended());
        toSave.add("CLICKGUI:X:" + dev.hypnotic.hypnotic_client.ui.clickgui.ClickGUI.INSTANCE.x + ":Y:" + dev.hypnotic.hypnotic_client.ui.clickgui.ClickGUI.INSTANCE.y);
        
        for (ModuleButton mb : dev.hypnotic.hypnotic_client.ui.clickgui.ClickGUI.INSTANCE.buttons) {
        	if (mb.settingsWindow != null) toSave.add("SETTINGPOS:" + mb.mod.name + ":X:" + mb.settingsWindow.x + ":Y:" + mb.settingsWindow.y);
        }
        
        for (Waypoint waypoint : WaypointManager.INSTANCE.waypoints) {
        	toSave.add("WAYPOINT:NAME:" + waypoint.getName() + ":X:" + waypoint.getX() + ":Y:" + waypoint.getY() + ":Z:" + waypoint.getZ());
        }
        
        /*for (String message : ModuleManager.INSTANCE.chatSpammer.custom) {
        	toSave.add("MESSAGE:" + message);
        }
        */

        try {
            PrintWriter pw = new PrintWriter(this.dataFile);
            for (String str : toSave) {
                pw.println(str);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void load() {


        ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.dataFile));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String s : lines) {
            String[] args = s.split(":");
            if (s.toLowerCase().startsWith("mod:")) {
                Mod m = ModuleManager.INSTANCE.getModuleByName(args[1]);
                if (m != null) {
                    m.setKey(Integer.parseInt(args[3]));
                }
                
            } else if (s.toLowerCase().startsWith("friend:")) {
            	FriendManager.INSTANCE.add(new Friend(args[1]));
            } else if (s.toLowerCase().startsWith("hud:")) {
            	for (HudModule element : HudManager.INSTANCE.hudModules) {
            		if (element.getName().equalsIgnoreCase(args[1])) {
            			element.setX(Integer.parseInt(args[2]));
            			element.setY(Integer.parseInt(args[3]));
            		}
            	}
            } else if (s.toLowerCase().startsWith("frame:")) {
            	for (Frame frame : ClickGUI.INSTANCE.frames) {
            		if (frame.name.equalsIgnoreCase(args[1])) {
            			frame.setX(Integer.parseInt(args[2]));
            			frame.setY(Integer.parseInt(args[3]));
            			frame.setExtended(Boolean.parseBoolean(args[4]));
            		}
            	}
            	HudEditorScreen.INSTANCE.frame.setX(Integer.parseInt(args[2]));
            	HudEditorScreen.INSTANCE.frame.setY(Integer.parseInt(args[3]));
            	HudEditorScreen.INSTANCE.frame.setExtended(Boolean.parseBoolean(args[4]));
            } else if (s.toLowerCase().startsWith("waypoint:")) {
            	for (Waypoint waypoint : WaypointManager.INSTANCE.waypoints) {
            		if (waypoint.getName().equalsIgnoreCase(args[1])) {
            			int x = Integer.parseInt(args[2]);
            			int y = Integer.parseInt(args[3]);
            			int z = Integer.parseInt(args[4]);
            			waypoint.setX(x);
            			waypoint.setY(y);
            			waypoint.setZ(z);
            			waypoint.setPos(new BlockPos(x, y, z));
            		}
            	}
            } else if (s.toLowerCase().startsWith("clickgui:")) {
            	dev.hypnotic.hypnotic_client.ui.clickgui.ClickGUI.INSTANCE.x = Integer.parseInt(args[2]);
            	dev.hypnotic.hypnotic_client.ui.clickgui.ClickGUI.INSTANCE.y = Integer.parseInt(args[4]);
            } else if (s.toLowerCase().startsWith("settingpos:")) {
            	for (ModuleButton mb : dev.hypnotic.hypnotic_client.ui.clickgui.ClickGUI.INSTANCE.buttons) {
                	if (mb.mod.name.equalsIgnoreCase(args[1])) {
                		mb.settingsWindow.x = Integer.parseInt(args[3]);
                		mb.settingsWindow.y = Integer.parseInt(args[5]);
                	}
                }
            }
            
            /*else if (s.toLowerCase().startsWith("message:")) {
            	ModuleManager.INSTANCE.chatSpammer.custom.add(args[1]);
            } else if (s.toLowerCase().startsWith("mainmenubg")) {
            	GuiMainMenu.menuIndex = Integer.parseInt(args[1]);
            } */
        }
    }
}
