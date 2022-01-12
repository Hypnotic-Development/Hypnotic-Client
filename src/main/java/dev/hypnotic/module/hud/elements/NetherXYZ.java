package dev.hypnotic.module.hud.elements;

import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.math.MathUtils;
import dev.hypnotic.utils.player.PlayerUtils;
import dev.hypnotic.utils.world.Dimension;
import net.minecraft.client.util.math.MatrixStack;

public class NetherXYZ extends HudModule {

	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	
	public NetherXYZ() {
		super("Nehter Coordinates", "Renders your nether coordinates", (int) FontManager.roboto.getStringWidth("Ping 80") + 5, 1050 - 20, (int)FontManager.roboto.getStringWidth("Ping 80"), (int)FontManager.roboto.getStringHeight("Ping 80"));
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
		double x = PlayerUtils.getDimension() == Dimension.NETHER ? mc.player.getX() * 8 : mc.player.getX() / 8;
		double y = PlayerUtils.getDimension() == Dimension.NETHER ? mc.player.getY() : mc.player.getY();
		double z = PlayerUtils.getDimension() == Dimension.NETHER ? mc.player.getZ() * 8 : mc.player.getZ() / 8;
		String netherXyzString = (PlayerUtils.getDimension() == Dimension.NETHER ? "Overworld " : "Nether ") + ColorUtils.gray + MathUtils.round(x, 1) + ", " + MathUtils.round(y, 1) + ", " + MathUtils.round(z, 1);
		this.setDefaultX((int) FontManager.roboto.getStringWidth("hello person looking at this ;)"));
		this.setDefaultY(scaledHeight - 20);
		this.setWidth((int) font.getStringWidth(netherXyzString));
		this.setHeight((int) font.getStringHeight(netherXyzString));
		font.drawWithShadow(matrices, netherXyzString, this.getX(), this.getY(), color.getRGB());
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
}
