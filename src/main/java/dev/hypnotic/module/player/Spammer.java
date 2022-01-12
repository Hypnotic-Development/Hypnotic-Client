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

