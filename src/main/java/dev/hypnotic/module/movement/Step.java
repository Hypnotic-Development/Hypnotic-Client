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
package dev.hypnotic.module.movement;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.NumberSetting;

public class Step extends Mod {

	public NumberSetting height = new NumberSetting("Height", 1, 1, 5, 1);
	
	public Step() {
		super("Step", "Makes your step height higher", Category.MOVEMENT);
		addSettings(height);
	}

	@Override
	public void onTick() {
		mc.player.stepHeight = (float)height.getValue();
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		mc.player.stepHeight = 0.5f;
		super.onDisable();
	}
}

