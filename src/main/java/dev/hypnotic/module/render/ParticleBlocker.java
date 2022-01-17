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
import dev.hypnotic.event.events.EventParticle;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import net.minecraft.client.particle.BlockDustParticle;
import net.minecraft.client.particle.RainSplashParticle;
import net.minecraft.client.particle.WaterSplashParticle;

public class ParticleBlocker extends Mod {

	public BooleanSetting block = new BooleanSetting("Block-Break", true);
	public BooleanSetting rain = new BooleanSetting("Explosion", true);
	public BooleanSetting water = new BooleanSetting("Water", true);
	
	public ParticleBlocker() {
		super("ParticleBlocker", "Stop those annoying particles", Category.RENDER);
		addSettings(block, rain, water);
	}
	
	@EventTarget 
	public void particle(EventParticle.Normal event) {
		if (event.getParticle() instanceof BlockDustParticle && block.isEnabled()) event.setCancelled(true);
		if (event.getParticle() instanceof RainSplashParticle && rain.isEnabled()) event.setCancelled(true);
		if (event.getParticle() instanceof WaterSplashParticle && water.isEnabled()) event.setCancelled(true);
	}
}
