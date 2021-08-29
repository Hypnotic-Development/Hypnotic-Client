package badgamesinc.hypnotic.module.render;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.utils.font.FontManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class CustomFont extends Mod {

	public CustomFont() {
		super("CustomFont", "gmer font", Category.RENDER);
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
