package badgamesinc.hypnotic.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.config.friends.Friend;
import badgamesinc.hypnotic.config.friends.FriendManager;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.hud.HudManager;
import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.settings.Setting;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.ui.HudEditorScreen;
import badgamesinc.hypnotic.ui.clickgui2.ClickGUI;
import badgamesinc.hypnotic.ui.clickgui2.frame.Frame;
import badgamesinc.hypnotic.utils.Logger;

public class SaveLoad {
    public File dir;
    public File configs;
    public File dataFile;

    public static SaveLoad INSTANCE = new SaveLoad();
    //Saves various aspects of the client
    
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

    	Logger.logInfo("Saving...");
        ArrayList<String> toSave = new ArrayList<String>();

        for (Mod mod : ModuleManager.INSTANCE.modules) {
            toSave.add("MOD:" + mod.getName() + ":" + mod.isEnabled() + ":" + mod.getKey());
            for (Setting set : mod.settings) {
            	if (set instanceof ColorSetting) {
            		ColorSetting color = (ColorSetting)set;
            		toSave.add("COLOR:" + mod.getName() + ":" + color.name + ":" + color.hue + ":" + color.sat + ":" + color.bri);
            	}
            }
        }
        
        //Will port later
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
        
        /*for (String message : ModuleManager.INSTANCE.chatSpammer.custom) {
        	toSave.add("MESSAGE:" + message);
        }
        
        toSave.add("MAINMENUBG:" + GuiMainMenu.getMenuIndex());
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
            }/*else if (s.toLowerCase().startsWith("message:")) {
            	ModuleManager.INSTANCE.chatSpammer.custom.add(args[1]);
            } else if (s.toLowerCase().startsWith("mainmenubg")) {
            	GuiMainMenu.menuIndex = Integer.parseInt(args[1]);
            } */
        }
    }
}
