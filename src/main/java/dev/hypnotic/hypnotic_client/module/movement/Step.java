package dev.hypnotic.hypnotic_client.module.movement;

import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.settings.settingtypes.NumberSetting;

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

