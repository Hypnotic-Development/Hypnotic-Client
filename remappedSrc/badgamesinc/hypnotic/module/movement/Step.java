package badgamesinc.hypnotic.module.movement;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;

public class Step extends Mod {

	public NumberSetting height = new NumberSetting("Height", 1, 1, 5, 1);
	
	public Step() {
		super("Step", "Makes your step height higher", Category.MOVEMENT);
		addSettings(height);
	}

	@Override
	public void onTick() {
		mc.player.stepHeight = (float)height.getValue();
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		mc.player.stepHeight = 0.5f;
		super.onDisable();
	}
}

