package badgamesinc.hypnotic.module.hud.elements;

import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import net.minecraft.client.util.math.MatrixStack;

public class NetherXYZ extends HudModule {

	public NetherXYZ() {
		super("Nehter Coordinates", "Renders your nether coordinates", (int) FontManager.roboto.getStringWidth("Ping 80") + 5, 1050 - 20, (int)FontManager.roboto.getStringWidth("Ping 80"), (int)FontManager.roboto.getStringHeight("Ping 80"));
	}
	
	@Override
	public void onEnable() {
		this.setDefaultX((int) FontManager.roboto.getStringWidth("Ping 80"));
		this.setDefaultY(mc.getWindow().getScaledHeight() - 20);
		if (this.getX() > mc.getWindow().getScaledWidth()) this.setX(this.getDefaultX());
		if (this.getY() > mc.getWindow().getScaledHeight()) this.setY(this.getDefaultY());
		super.onEnable();
	}

	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		String bpsString = "XYZ";
		this.setDefaultX((int) FontManager.roboto.getStringWidth("TPS 20"));
		this.setDefaultY(scaledHeight - 20);
		this.setWidth((int) font.getWidth(bpsString));
		this.setHeight((int) font.getStringHeight(bpsString));
		font.drawWithShadow(matrices, bpsString, this.getX(), this.getY(), ColorUtils.getClientColorInt());
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
}
