package badgamesinc.hypnotic.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.config.friends.FriendManager;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;

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

        ArrayList<String> toSave = new ArrayList<String>();

        for (Mod mod : ModuleManager.INSTANCE.modules) {
            toSave.add("MOD:" + mod.getName() + ":" + mod.isEnabled() + ":" + mod.getKey());
        }
        
        //Will port later
        for (String friends : FriendManager.INSTANCE.friends) {
        	toSave.add("FRIEND:" + friends);
        }
        
        /*for (String message : ModuleManager.INSTANCE.chatSpammer.custom) {
        	toSave.add("MESSAGE:" + message);
        }
        
        for (Frame frame : ClickGui.instance.frames) {
        	toSave.add("CLICKGUI:" + frame.category + ":X:" + frame.getX() + ":Y:" + frame.getY() + ":OPEN:" + frame.isOpen());
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
            	FriendManager.INSTANCE.addFriend(args[1]);
            } /*else if (s.toLowerCase().startsWith("message:")) {
            	ModuleManager.INSTANCE.chatSpammer.custom.add(args[1]);
            } else if (s.toLowerCase().startsWith("mainmenubg")) {
            	GuiMainMenu.menuIndex = Integer.parseInt(args[1]);
            } */
        }
    }
}
