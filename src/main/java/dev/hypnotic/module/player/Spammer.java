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
package dev.hypnotic.module.player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.Timer;

public class Spammer extends Mod {
    public NumberSetting delay = new NumberSetting("Delay", 5, 0.0, 20.0, 0.1);
    public int msgCount = 0;
    public Map<Integer, String> customMessages = new HashMap<>();
    public Map<Integer, String> randomShit = new HashMap<>();
    public Map<Integer, String> facts = new HashMap<>();
    public static Timer delayTimer = new Timer();
    public File spammerFile = new File(Hypnotic.hypnoticDir + "/spammer.txt");
    
    public Spammer() {
        super("Spammer", "Mine blocks faster", Category.PLAYER);
        this.addSettings(delay);
        
        try {
			spammerFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
        facts.put(1, "Hypnotic is proven to be 200% cooler than other clients");
        facts.put(2, "Hypnotic features a full JavaScript scripting system");
        facts.put(3, "https://discord.gg/aZStDUnb29 is the official discord to complain in");
        facts.put(4, "https://hypnotic.dev has a secret floppa api");
        facts.put(5, "Hypnotic features 100+ modules, 20+ commands, and beautiful graphics");
    }

    public void onTick() {
        if (delayTimer.hasTimeElapsed((long)delay.getValue() * 1000, true)) {
        	mc.player.sendChatMessage("/sell");
        }
        mc.options.keySneak.setPressed(true);
        super.onTick();
    }
    
    public void loadCustomMessages() {
    	
    }
    
    public void addCustomMessage(String message) {
    	
    }
}

