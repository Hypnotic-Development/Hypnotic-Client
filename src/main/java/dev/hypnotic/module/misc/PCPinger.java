package dev.hypnotic.module.misc;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.utils.Utils;

public class PCPinger extends Mod {

	public PCPinger() {
		super("PCPinger", "Pings your pc", Category.EXPLOIT);
	}

	@Override
	public void onEnable() {
		Utils.openURL("baba-a461.github.io/L");
		this.toggle();
		super.onEnable();
	}
}
