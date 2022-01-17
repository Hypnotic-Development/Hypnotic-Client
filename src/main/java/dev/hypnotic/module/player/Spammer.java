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

import java.util.ArrayList;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.Timer;

public class Spammer extends Mod {
    public NumberSetting delay = new NumberSetting("Delay", 5D, 0.0D, 20.0D, 0.1D);
    public ArrayList<String> messages = new ArrayList<>();
    public static String message = "/sell";
    public static Timer delayTimer = new Timer();
    
    public Spammer() {
        super("Spammer", "Mine blocks faster", Category.PLAYER);
        this.addSettings(delay);
    }

    public void onTick() {
        if (delayTimer.hasTimeElapsed((long)delay.getValue() * 1000, true)) {
        	mc.player.sendChatMessage("/sell");
        }
        mc.options.keySneak.setPressed(true);
        super.onTick();
    }
}

