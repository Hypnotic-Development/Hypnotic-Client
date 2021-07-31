package badgamesinc.hypnotic.module.render;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;

public class Fullbright extends Mod {

	public Fullbright() {
		super("Fullbright", "Turns your gamma very hight", Category.RENDER);
	}

	@Override
	public void onEnable() {
		mc.options.gamma = 100;
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		mc.options.gamma = 0;
		super.onDisable();
	}
}
