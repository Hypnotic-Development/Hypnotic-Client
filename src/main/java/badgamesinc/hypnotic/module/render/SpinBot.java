package badgamesinc.hypnotic.module.render;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventMotionUpdate;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.RotationUtils;

public class SpinBot extends Mod {

	public NumberSetting speed = new NumberSetting("Speed", 1, 1, 50, 0.1);
	public SpinBot() {
		super("SpinBot", "Turns your player around", Category.RENDER);
		addSetting(speed);
	}
	
	float rot = 0;
	
	@EventTarget
	public void onMotion(EventMotionUpdate event) {
		if (event.isPre()) {
			if (rot < 350) rot+=speed.getValue() * 0.5;
			else rot = 0;
			
			RotationUtils.setSilentPitch(0);
			RotationUtils.setSilentYaw(rot);
			event.setPitch(0);
			event.setYaw(rot);
		}
	}
	
	@Override
	public void onDisable() {
		RotationUtils.resetPitch();
		RotationUtils.resetYaw();
		super.onDisable();
	}
}
