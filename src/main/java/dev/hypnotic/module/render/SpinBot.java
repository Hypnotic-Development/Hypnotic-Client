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

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventMotionUpdate;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.RotationUtils;

public class SpinBot extends Mod {

	public NumberSetting speed = new NumberSetting("Speed", 1, 1, 50, 0.1);
	public SpinBot() {
		super("SpinBot", "Turns your player around", Category.RENDER);
		addSetting(speed);
	}
	
	float rot = 0;
	
	@EventTarget
	public void onMotion(EventMotionUpdate event) {
		if (event.isPre()) {
			if (rot < 350) rot+=speed.getValue() * 0.5;
			else rot = 0;
			
			RotationUtils.setSilentPitch(0);
			RotationUtils.setSilentYaw(rot);
			event.setPitch(0);
			event.setYaw(rot);
		}
	}
	
	@Override
	public void onDisable() {
		RotationUtils.resetPitch();
		RotationUtils.resetYaw();
		super.onDisable();
	}
}
