package badgamesinc.hypnotic.module.render;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;

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
