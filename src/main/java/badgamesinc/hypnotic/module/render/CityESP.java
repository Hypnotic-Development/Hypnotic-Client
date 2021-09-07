package badgamesinc.hypnotic.module.render;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventRender3D;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import badgamesinc.hypnotic.utils.world.EntityUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class CityESP extends Mod {

	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	public CityESP() {
		super("CityESP", "Shows blocks that you can break to city players", Category.RENDER);
		addSetting(color);
	}
	
	@EventTarget
	public void event3d(EventRender3D event) {
		try {
			for (PlayerEntity player : mc.world.getPlayers()) {
				Vec3d pos = RenderUtils.getRenderPosition(EntityUtils.getCityBlock(player));
				RenderUtils.drawOutlineBox(event.getMatrices(), new Box(pos.x, pos.y, pos.z, pos.x + 1, pos.y + 1, pos.z + 1), color.getRGB(), true);
			}
		} catch(Exception e) {
			
		}
	}
}
