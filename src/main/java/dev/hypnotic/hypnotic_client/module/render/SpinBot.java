package dev.hypnotic.hypnotic_client.module.render;

import dev.hypnotic.hypnotic_client.event.EventTarget;
import dev.hypnotic.hypnotic_client.event.events.EventMotionUpdate;
import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.settings.settingtypes.NumberSetting;
import dev.hypnotic.hypnotic_client.utils.RotationUtils;

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
