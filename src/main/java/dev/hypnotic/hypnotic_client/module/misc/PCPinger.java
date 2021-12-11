package dev.hypnotic.hypnotic_client.module.misc;

import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.utils.Utils;

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
