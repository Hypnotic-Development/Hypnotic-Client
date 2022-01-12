package dev.hypnotic.module.render;

import dev.hypnotic.Hypnotic;
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
		if (this.isEnabled() && (player == mc.player || Hypnotic.isHypnoticUser(player.getName().getString()))) {
			return mode.is("Hypnotic") ? new Identifier("hypnotic", "textures/cape.png") : new Identifier("hypnotic", "textures/sigmacape.png");
		} else {
			return null;
		}
	}
}