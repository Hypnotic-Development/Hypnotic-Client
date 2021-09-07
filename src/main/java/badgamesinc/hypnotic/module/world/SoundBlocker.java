package badgamesinc.hypnotic.module.world;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventSound;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import net.minecraft.sound.SoundEvents;

public class SoundBlocker extends Mod {

	public BooleanSetting explosion = new BooleanSetting("Explosion", true);
	public BooleanSetting exp = new BooleanSetting("Exp", true);
	public BooleanSetting water = new BooleanSetting("Water", true);
	public BooleanSetting rain = new BooleanSetting("Rain", true);
	public BooleanSetting totem = new BooleanSetting("Totem Pop", true);
	public BooleanSetting portal = new BooleanSetting("Portal", true);
	
	public SoundBlocker() {
		super("SoundBlocker", "Blocks sounds", Category.WORLD);
		addSettings(explosion, exp, water, rain, totem, portal);
	}
	
	@EventTarget
	public void onSound(EventSound event) {
		System.out.println(event.sound.getId());
		if ((event.sound.getId().getPath().equals("entity.player.attack.strong") || event.sound.getId().getPath().equals("entity.generic.explode")) && explosion.isEnabled()) event.setCancelled(true);
		if (event.sound == SoundEvents.ITEM_TOTEM_USE && totem.isEnabled()) event.setCancelled(true);
		if (event.sound == SoundEvents.BLOCK_WATER_AMBIENT && water.isEnabled()) event.setCancelled(true);
		if ((event.sound == SoundEvents.WEATHER_RAIN || event.sound == SoundEvents.WEATHER_RAIN_ABOVE) && rain.isEnabled()) event.setCancelled(true);
		if (event.sound.getId().getPath().equals("entity.experience_orb.pickup") && exp.isEnabled()) event.setCancelled(true);
		if ((event.sound == SoundEvents.BLOCK_PORTAL_AMBIENT || event.sound == SoundEvents.BLOCK_PORTAL_TRIGGER || event.sound == SoundEvents.BLOCK_PORTAL_TRAVEL) && portal.isEnabled()) event.setCancelled(true);
	}
}
