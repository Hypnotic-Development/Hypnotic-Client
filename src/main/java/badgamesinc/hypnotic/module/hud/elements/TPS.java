package badgamesinc.hypnotic.module.hud.elements;

import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import badgamesinc.hypnotic.utils.math.MathUtils;
import badgamesinc.hypnotic.utils.math.TPSUtils;
import net.minecraft.client.util.math.MatrixStack;

public class TPS extends HudModule {

	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	
	public TPS() {
		super("Tps Display", "Renders the servers esimated tps", 5, 1050, (int)FontManager.robotoMed.getStringWidth("TPS 20"), (int)FontManager.robotoMed.getStringHeight("TPS 20"));
		addSetting(color);
	}
	
	@Override
	public void onEnable() {
		this.setDefaultX(5);
		this.setDefaultY(mc.getWindow().getScaledHeight() - 30);
		if (this.getX() > mc.getWindow().getScaledWidth()) this.setX(this.getDefaultX());
		if (this.getY() > mc.getWindow().getScaledHeight()) this.setY(this.getDefaultY());
		super.onEnable();
	}

	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		String tpsString = "TPS " + ColorUtils.gray + MathUtils.round(TPSUtils.INSTANCE.getAverageTPS(), 2);
		this.setDefaultX(5);
		this.setDefaultY(scaledHeight - 30);
		this.setWidth((int) font.getStringWidth(tpsString));
		this.setHeight((int) font.getStringHeight(tpsString));
		font.drawWithShadow(matrices, tpsString, this.getX(), this.getY(), color.getRGB());
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
}
