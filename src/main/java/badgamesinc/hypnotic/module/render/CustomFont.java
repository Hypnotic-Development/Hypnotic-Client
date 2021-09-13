package badgamesinc.hypnotic.module.render;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.config.SaveLoad;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import net.minecraft.entity.player.PlayerEntity;

public class CustomFont extends Mod {

	public CustomFont() {
		super("CustomFont", "gamer font", Category.RENDER);
	}

	@Override
	public void onEnable() {
		FontManager.setMcFont(false);
		for (PlayerEntity e : mc.world.getPlayers()) {
			System.out.println(Hypnotic.INSTANCE.api.checkOnline(e.getName().asString().replace(ColorUtils.colorChar, "").replace(" ", "")));
		}
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		FontManager.setMcFont(true);
		super.onDisable();
	}
}
