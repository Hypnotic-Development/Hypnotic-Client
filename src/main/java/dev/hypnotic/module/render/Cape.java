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
import dev.hypnotic.settings.settingtypes.ModeSetting;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class Cape extends Mod {

	public ModeSetting mode = new ModeSetting("Texture", "Hypnotic", "Hypnotic", "Sigma", "Custom");
	
	public Cape() {
		super("Cape", "Renders a custom cape on you", Category.RENDER);
		addSettings(mode);
	}
	
	public Identifier getTexture(PlayerEntity player) {
		if (this.isEnabled() && (player == mc.player)) {
			return mode.is("Hypnotic") ? new Identifier("hypnotic", "textures/cape.png") : new Identifier("hypnotic", "textures/sigmacape.png");
		} else {
			return null;
		}
	}
}
