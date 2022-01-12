package dev.hypnotic.module.hud.elements;

import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.font.FontManager;
import net.minecraft.client.util.math.MatrixStack;

public class FPS extends HudModule {

	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	public FPS() {
		super("Fps Display", "Renders your fps", (int) FontManager.roboto.getStringWidth("TPS 20"), 1050, (int)FontManager.roboto.getStringWidth("FPS 100"), (int)FontManager.roboto.getStringHeight("FPS 100"));
		addSetting(color);
	}
	
	@Override
	public void onEnable() {
		this.setDefaultX((int) FontManager.roboto.getStringWidth("TPS 20"));
		this.setDefaultY(mc.getWindow().getScaledHeight() - 30);
		if (this.getX() > mc.getWindow().getScaledWidth()) this.setX(this.getDefaultX());
		if (this.getY() > mc.getWindow().getScaledHeight()) this.setY(this.getDefaultY());
		super.onEnable();
	}

	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		matrices.push();
//		color.hue = 0.5f;
//		color.sat = 0.5f;
//		color.bri = 1f;
		matrices.translate(this.getX(), this.getY(), 0);
		matrices.scale((float) this.getScaleX(), (float) this.getScaleY(), 0);
		String fpsString = "FPS " + ColorUtils.gray + mc.fpsDebugString.split(" ")[0];
		this.setDefaultX((int) FontManager.roboto.getStringWidth("TPS 20"));
		this.setDefaultY(scaledHeight - 30);
		this.setScaleX(1);
		this.setScaleY(1);
		this.setWidth((int) font.getStringWidth(fpsString));
		this.setHeight((int) font.getStringHeight(fpsString));
		font.drawWithShadow(matrices, fpsString, 0, 0, color.getRGB());
		matrices.pop();
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
}
