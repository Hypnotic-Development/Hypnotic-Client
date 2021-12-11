package dev.hypnotic.hypnotic_client.module.render;

import dev.hypnotic.hypnotic_client.event.EventTarget;
import dev.hypnotic.hypnotic_client.event.events.EventParticle;
import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.settings.settingtypes.BooleanSetting;
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
