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

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;

public class HUDModule extends Mod 
{
	public BooleanSetting customColor = new BooleanSetting("RGB Color", false);
	public NumberSetting red = new NumberSetting("Red", 255, 0, 255, 1);
	public NumberSetting green = new NumberSetting("Green", 20, 0, 255, 1);
	public NumberSetting blue = new NumberSetting("Blue", 100, 0, 255, 1);
	public HUDModule()
	{
		super("HUD", "Enhanced HUD", Category.RENDER);
		addSetting(customColor);
		addSetting(red);
		addSetting(green);
		addSetting(blue);
		this.enabled = true;
	}
}
