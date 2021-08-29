package badgamesinc.hypnotic.module.hud.elements;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import net.minecraft.client.util.math.MatrixStack;

public class Logo extends HudModule {

	public ColorSetting color = new ColorSetting("Color", ColorUtils.getClientColor().getRed(), ColorUtils.getClientColor().getGreen(), ColorUtils.getClientColor().getBlue(), false);
	
	public Logo() {
		super("Logo", "Renders the Hypnotic logo", 4, 4, (int) FontManager.robotoMed.getStringWidth(Hypnotic.name + ColorUtils.gray + " " + Hypnotic.version), 4);
		addSetting(color);
	}
	
	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		this.setHeight((int) font.getStringHeight(Hypnotic.fullName));
		font.drawWithShadow(matrices, Hypnotic.name + ColorUtils.gray + " " + Hypnotic.version, this.getX(), this.getY(), color.getRGB());
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
}
