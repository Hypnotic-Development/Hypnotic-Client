package dev.hypnotic.module.hud.elements;

import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class Doll extends HudModule {

	public NumberSetting size = new NumberSetting("Size", 4, 1, 10, 1);
	public BooleanSetting bg = new BooleanSetting("Background", true);
	
	public Doll() {
		super("Doll", "Renders a mini model of you", 4, 100, 20, 50);
		addSettings(size, bg);
	}
	
	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		if (bg.isEnabled()) RenderUtils.fill(matrices, getX(), getY(), getX() + getWidth(), getY() + getHeight(), ColorUtils.transparent(150));
		InventoryScreen.drawEntity((int) (getX() + getWidth() / 2), (int) (getY() + this.getHeight() - size.getValue() * 1.5f), (int) size.getValue() * 10, MathHelper.wrapDegrees(mc.player.getYaw()), -mc.player.getPitch(), mc.player);
		this.setWidth((int)size.getValue() * 10);
		this.setHeight((int)size.getValue() * 22);
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
}
