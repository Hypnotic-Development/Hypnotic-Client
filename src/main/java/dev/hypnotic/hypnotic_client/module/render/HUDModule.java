package dev.hypnotic.hypnotic_client.module.render;

import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.settings.settingtypes.BooleanSetting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.NumberSetting;

public class HUDModule extends Mod 
{
	public BooleanSetting customColor = new BooleanSetting("RGB Color", false);
	public NumberSetting red = new NumberSetting("Red", 255, 0, 255, 1);
	public NumberSetting green = new NumberSetting("Green", 20, 0, 255, 1);
	public NumberSetting blue = new NumberSetting("Blue", 100, 0, 255, 1);
	public HUDModule()
	{
		super("HUD", "Enhanced HUD", Category.RENDER);
		addSetting(customColor);
		addSetting(red);
		addSetting(green);
		addSetting(blue);
		this.enabled = true;
	}
}
