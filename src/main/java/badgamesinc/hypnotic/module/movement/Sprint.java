package badgamesinc.hypnotic.module.movement;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.utils.ColorUtils;

public class Sprint extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Omni", "Omni", "Vanilla");
	
    public Sprint() {
        super("Sprint", "Makes you sprint all the time", Category.MOVEMENT);
        addSetting(mode);
    }

    @Override
    public void onMotion() {
    	this.setDisplayName("Sprint " + ColorUtils.gray + mode.getSelected());
    	if (mc.player != null && mc.player.input != null) {
	    	if (mode.is("Vanilla")) mc.options.keySprint.setPressed(true);
	    	else if (mode.is("Omni")) mc.player.setSprinting(true);
    	}
    		
        super.onTick();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
