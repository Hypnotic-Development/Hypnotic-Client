package dev.hypnotic.hypnotic_client.module.render;

import java.awt.Color;

import dev.hypnotic.hypnotic_client.event.EventTarget;
import dev.hypnotic.hypnotic_client.event.events.EventRender3D;
import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ColorSetting;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.render.QuadColor;
import dev.hypnotic.hypnotic_client.utils.render.RenderUtils;
import dev.hypnotic.hypnotic_client.utils.world.EntityUtils;
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
