package dev.hypnotic.module.render;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.utils.font.FontManager;

public class CustomFont extends Mod {

	public CustomFont() {
		super("CustomFont", "gamer font", Category.RENDER);
		this.setEnabled(true);
	}

	@Override
	public void onEnable() {
		FontManager.setMcFont(false);
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		FontManager.setMcFont(true);
		super.onDisable();
	}
}
