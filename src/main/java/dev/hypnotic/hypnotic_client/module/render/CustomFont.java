package dev.hypnotic.hypnotic_client.module.render;

import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.utils.font.FontManager;

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
