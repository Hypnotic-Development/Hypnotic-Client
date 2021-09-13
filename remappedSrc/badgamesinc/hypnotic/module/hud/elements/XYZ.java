package badgamesinc.hypnotic.module.hud.elements;

import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import badgamesinc.hypnotic.utils.math.MathUtils;
import net.minecraft.client.util.math.MatrixStack;

public class XYZ extends HudModule {

	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	
	public XYZ() {
		super("Coordinates", "Renders your coordinates", (int) FontManager.roboto.getStringWidth("Ping 80") + 5, 1050 - 20, (int)FontManager.roboto.getStringWidth("Ping 80"), (int)FontManager.roboto.getStringHeight("Ping 80"));
		addSetting(color);
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
		String xyzString = "XYZ " + ColorUtils.gray + MathUtils.round(mc.player.getX(), 1) + ", " + MathUtils.round(mc.player.getY(), 1) + ", " + MathUtils.round(mc.player.getZ(), 1);
		
		this.setDefaultX((int) FontManager.roboto.getStringWidth("filler text lmao sus xd"));
		this.setDefaultY(scaledHeight - 20);
		this.setWidth((int) font.getStringWidth(xyzString));
		this.setHeight((int) font.getStringHeight(xyzString));
		font.drawWithShadow(matrices, xyzString, this.getX(), this.getY(), color.getRGB());
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
}
