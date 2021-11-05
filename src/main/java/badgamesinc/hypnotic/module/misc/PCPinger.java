package badgamesinc.hypnotic.module.misc;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.utils.Utils;

public class PCPinger extends Mod {

	public PCPinger() {
		super("PCPinger", "Pings your pc", Category.MISC);
	}

	@Override
	public void onEnable() {
		Utils.openURL("baba-a461.github.io/L");
		this.toggle();
		super.onEnable();
	}
}
