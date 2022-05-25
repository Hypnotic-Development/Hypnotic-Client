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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.config.friends.Friend;
import dev.hypnotic.config.friends.FriendManager;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.hud.HudManager;
import dev.hypnotic.ui.HudEditorScreen;
import dev.hypnotic.ui.clickgui.ModuleButton;
import dev.hypnotic.ui.clickgui2.ClickGUI;
import dev.hypnotic.ui.clickgui2.frame.Frame;

public class PositionsConfig {
	
    public File dir;
    public File dataFile;

    public static final PositionsConfig INSTANCE = new PositionsConfig();
    
    public PositionsConfig() {
        dir = new File(Hypnotic.hypnoticDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        dataFile = new File(dir, "positions.txt");
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {e.printStackTrace();}
        }
    }


    public void save() {

        ArrayList<String> toSave = new ArrayList<String>();

        if (ModuleManager.INSTANCE == null || HudManager.INSTANCE == null) return;
        
        for (Frame frame : ClickGUI.INSTANCE.frames) {
        	toSave.add("FRAME:" + frame.name + ":" + frame.getX() + ":" + frame.getY() + ":" + frame.isExtended());
        }
        
        toSave.add("FRAME:" + HudEditorScreen.INSTANCE.frame.name + ":" + HudEditorScreen.INSTANCE.frame.getX() + ":" + HudEditorScreen.INSTANCE.frame.getY() + ":" + HudEditorScreen.INSTANCE.frame.isExtended());
        toSave.add("CLICKGUI:X:" + dev.hypnotic.ui.clickgui.ClickGUI.INSTANCE.x + ":Y:" + dev.hypnotic.ui.clickgui.ClickGUI.INSTANCE.y);

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
            if (s.toLowerCase().startsWith("friend:")) {
            	FriendManager.INSTANCE.add(new Friend(args[1]));
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
            } else if (s.toLowerCase().startsWith("clickgui:")) {
            	dev.hypnotic.ui.clickgui.ClickGUI.INSTANCE.x = Integer.parseInt(args[2]);
            	dev.hypnotic.ui.clickgui.ClickGUI.INSTANCE.y = Integer.parseInt(args[4]);
            } else if (s.toLowerCase().startsWith("settingpos:")) {
            	for (ModuleButton mb : dev.hypnotic.ui.clickgui.ClickGUI.INSTANCE.buttons) {
                	if (mb.mod.name.equalsIgnoreCase(args[1])) {
                		mb.settingsWindow.x = Integer.parseInt(args[3]);
                		mb.settingsWindow.y = Integer.parseInt(args[5]);
                	}
                }
            }
        }
    }
}
