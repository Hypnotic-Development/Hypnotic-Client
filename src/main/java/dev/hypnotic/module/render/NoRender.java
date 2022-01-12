package dev.hypnotic.module.render;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;

public class NoRender extends Mod {

	public BooleanSetting fire = new BooleanSetting("Fire", true);
	public BooleanSetting portal = new BooleanSetting("Portal", true);
	public BooleanSetting liquid = new BooleanSetting("Liquid", true);
	public BooleanSetting pumpkin = new BooleanSetting("Pumpkin", true);
	
	public NoRender() {
		super("NoRender", "Removes certain overlays", Category.RENDER);
		addSettings(fire, portal, liquid, pumpkin);
	}
}
