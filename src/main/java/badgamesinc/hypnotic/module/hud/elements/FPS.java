package badgamesinc.hypnotic.module.hud.elements;

import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import net.minecraft.client.util.math.MatrixStack;

public class FPS extends HudModule {

	public FPS() {
		super("Fps Display", "Renders your fps", (int) FontManager.roboto.getStringWidth("TPS 20"), 1050, (int)FontManager.roboto.getStringWidth("FPS 100"), (int)FontManager.roboto.getStringHeight("FPS 100"));
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
		String fpsString = "FPS " + ColorUtils.gray + mc.fpsDebugString.split(" ")[0];
		this.setDefaultX((int) FontManager.roboto.getStringWidth("TPS 20"));
		this.setDefaultY(scaledHeight - 30);
		this.setWidth((int) font.getStringWidth(fpsString));
		this.setHeight((int) font.getStringHeight(fpsString));
		font.drawWithShadow(matrices, fpsString, this.getX(), this.getY(), ColorUtils.getClientColorInt());
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
}
