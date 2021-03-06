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
package dev.hypnotic.module.misc;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventSound;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import net.minecraft.sound.SoundEvents;

public class SoundBlocker extends Mod {

	public BooleanSetting explosion = new BooleanSetting("Explosion", true);
	public BooleanSetting exp = new BooleanSetting("Exp", true);
	public BooleanSetting water = new BooleanSetting("Water", true);
	public BooleanSetting rain = new BooleanSetting("Rain", true);
	public BooleanSetting totem = new BooleanSetting("Totem Pop", true);
	public BooleanSetting portal = new BooleanSetting("Portal", true);
	
	public SoundBlocker() {
		super("SoundBlocker", "Blocks sounds", Category.MISC);
		addSettings(explosion, exp, water, rain, totem, portal);
	}
	
	@EventTarget
	public void onSound(EventSound event) {
		if ((event.sound.getId().getPath().equals("entity.player.attack.strong") || event.sound.getId().getPath().equals("entity.generic.explode")) && explosion.isEnabled()) event.setCancelled(true);
		if (event.sound == SoundEvents.ITEM_TOTEM_USE && totem.isEnabled()) event.setCancelled(true);
		if (event.sound == SoundEvents.BLOCK_WATER_AMBIENT && water.isEnabled()) event.setCancelled(true);
		if ((event.sound == SoundEvents.WEATHER_RAIN || event.sound == SoundEvents.WEATHER_RAIN_ABOVE) && rain.isEnabled()) event.setCancelled(true);
		if (event.sound.getId().getPath().equals("entity.experience_orb.pickup") && exp.isEnabled()) event.setCancelled(true);
		if ((event.sound == SoundEvents.BLOCK_PORTAL_AMBIENT || event.sound == SoundEvents.BLOCK_PORTAL_TRIGGER || event.sound == SoundEvents.BLOCK_PORTAL_TRAVEL) && portal.isEnabled()) event.setCancelled(true);
	}
}
