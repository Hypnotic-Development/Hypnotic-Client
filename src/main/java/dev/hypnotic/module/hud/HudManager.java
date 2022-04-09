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
package dev.hypnotic.module.hud;

import java.util.ArrayList;

import dev.hypnotic.module.hud.elements.*;
import dev.hypnotic.module.render.TargetHUD;

public class HudManager {

	public static HudManager INSTANCE = new HudManager();
	public ArrayList<HudModule> hudModules = new ArrayList<>();
	
	public HudManager() {
		registerHudElements(
				new Radar(),
				new TPS(),
				new FPS(),
				new Ping(),
				new BPS(),
				new XYZ(),
				new NetherXYZ(),
				new Armor(),
				new Logo(),
				new ArrayListModule(),
				new Packets(),
				new Doll(),
				new TargetHUD(),
				new LagIndicator()
			);
	}
	
	
	public void registerHudElements(HudModule... elements) {
		for (HudModule element : elements) {
			hudModules.add(element);
		}
	}
}
