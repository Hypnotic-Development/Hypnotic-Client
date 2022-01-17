/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.module.render;

import java.awt.Color;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventRender3D;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.render.QuadColor;
import dev.hypnotic.utils.render.RenderUtils;
import dev.hypnotic.utils.world.EntityUtils;
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
