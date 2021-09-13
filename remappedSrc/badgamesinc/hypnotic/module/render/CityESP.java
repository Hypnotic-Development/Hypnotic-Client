package badgamesinc.hypnotic.module.render;

import java.awt.Color;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventRender3D;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.render.QuadColor;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import badgamesinc.hypnotic.utils.world.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class CityESP extends Mod {

	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	public CityESP() {
		super("CityESP", "Shows blocks that you can break to city players", Category.RENDER);
		addSetting(color);
	}
	
	@EventTarget
	public void event3d(EventRender3D event) {
		for (Entity player : mc.world.getEntities()) {
			if (player instanceof PlayerEntity && EntityUtils.getCityBlock((PlayerEntity) player) != null) {
				RenderUtils.drawBoxOutline(EntityUtils.getCityBlock((PlayerEntity)player), QuadColor.single(Color.WHITE.getRGB()), 2);
				RenderUtils.drawBoxFill(EntityUtils.getCityBlock((PlayerEntity)player), QuadColor.single(new Color(255, 255, 255, 100).getRGB()));
			}
		}
	}
}
