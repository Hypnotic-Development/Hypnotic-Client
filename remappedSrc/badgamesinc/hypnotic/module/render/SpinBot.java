package badgamesinc.hypnotic.module.render;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.RotationUtils;

public class SpinBot extends Mod {

	public NumberSetting speed = new NumberSetting("Speed", 1, 1, 5, 0.1);
	public SpinBot() {
		super("SpinBot", "Turns your player around", Category.RENDER);
		addSetting(speed);
	}
	
	float rot = 0;
	@Override
	public void onTick() {
		if (rot < 350) rot+=speed.getValue() * 0.5;
		else rot = 0;
		
		RotationUtils.setSilentPitch(0);
		RotationUtils.setSilentYaw(rot);
		
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		RotationUtils.resetPitch();
		RotationUtils.resetYaw();
		super.onDisable();
	}
}
