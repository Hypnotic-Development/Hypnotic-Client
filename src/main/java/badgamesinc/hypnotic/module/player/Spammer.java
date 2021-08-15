package badgamesinc.hypnotic.module.player;

import java.util.ArrayList;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.Timer;

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
        	mc.player.sendChatMessage(message);
        	mc.player.sendChatMessage(message);
        }
        mc.options.keySneak.setPressed(true);
        super.onTick();
    }
}

