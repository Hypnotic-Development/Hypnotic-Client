package dev.hypnotic.hypnotic_client.module.hud.elements;

import dev.hypnotic.hypnotic_client.module.hud.HudModule;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ColorSetting;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.font.FontManager;
import net.minecraft.client.util.math.MatrixStack;

public class Ping extends HudModule {

	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	
	public Ping() {
		super("Ping Display", "Renders your ping", 5, 1050 - 20, (int)FontManager.roboto.getWidth("Ping 80"), (int)FontManager.roboto.getStringHeight("Ping 80"));
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
		String pingString = "Ping " + ColorUtils.gray + (mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()) == null ? 0 : mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()).getLatency());
		this.setDefaultX((int) FontManager.roboto.getStringWidth("TPS 20"));
		this.setDefaultY(scaledHeight - 20);
		this.setWidth((int) font.getStringWidth(pingString));
		this.setHeight((int) font.getStringHeight(pingString));
		font.drawWithShadow(matrices, pingString, this.getX(), this.getY(), color.getRGB());
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
}
