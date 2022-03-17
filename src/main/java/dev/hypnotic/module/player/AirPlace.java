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
package dev.hypnotic.module.player;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventRender3D;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.Timer;
import dev.hypnotic.utils.render.QuadColor;
import dev.hypnotic.utils.render.RenderUtils;
import dev.hypnotic.utils.world.WorldUtils;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class AirPlace extends Mod {

	public NumberSetting range = new NumberSetting("Range", 4, 1, 6, 0.1);
	public NumberSetting delay = new NumberSetting("Place Delay", 6, 0, 10, 1);
	public BooleanSetting outline = new BooleanSetting("Render Outline", true);
	public ColorSetting outlineColor = new ColorSetting("Outline Color", 0, 0.71f, 0.64f, 1f, true);
	
	private BlockPos placePos;
	
	private Timer placeTimer = new Timer();
	private boolean hasPlacedOne = false;
	
	public AirPlace() {
		super("AirPlace", "Places a block in the air where you are looking", Category.PLAYER);
		addSettings(range, delay, outline, outlineColor);
	}
	
	@Override
	public void onTick() {
		if (mc.world != null && mc.player != null) {
			placePos = new BlockPos(mc.getCameraEntity().raycast(range.getValue(), 0, false).getPos());
			if (placePos != null && mc.world.getBlockState(placePos).getMaterial().isReplaceable() && mc.options.useKey.isPressed()) {
				if (placeTimer.hasTimeElapsed((long)delay.getValue() * 60, true) || !hasPlacedOne) {
					if (mc.player.getMainHandStack().getItem() instanceof BlockItem) {
						WorldUtils.placeBlockMainHand(placePos, false, true);
					} else if (mc.player.getOffHandStack().getItem() instanceof BlockItem) {
						WorldUtils.placeBlockNoRotate(Hand.OFF_HAND, placePos, true);
					}
					hasPlacedOne = true;
				}
			} else {
				hasPlacedOne = false;
			}
		}
		super.onTick();
	}
	
	@EventTarget
	public void render(EventRender3D event) {
		if (outline.isEnabled() && placePos != null && mc.world.getBlockState(placePos).getMaterial().isReplaceable() && (mc.player.getMainHandStack().getItem() instanceof BlockItem || mc.player.getOffHandStack().getItem() instanceof BlockItem)) {
			float[] c = outlineColor.getRGBFloat();
			RenderUtils.drawBoxOutline(placePos, QuadColor.single(c[0], c[1], c[2], 1f), 1);
		}
	}
	
	@Override
	public void onTickDisabled() {
		outlineColor.setVisible(outline.isEnabled());
		super.onTickDisabled();
	}
}
