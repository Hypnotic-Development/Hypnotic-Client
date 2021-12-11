package dev.hypnotic.hypnotic_client.ui;

import dev.hypnotic.hypnotic_client.event.EventTarget;
import dev.hypnotic.hypnotic_client.event.events.EventRenderGUI;
import dev.hypnotic.hypnotic_client.module.hud.HudManager;
import dev.hypnotic.hypnotic_client.module.hud.HudModule;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.Timer;
import dev.hypnotic.hypnotic_client.utils.font.FontManager;
import dev.hypnotic.hypnotic_client.utils.font.NahrFont;
import dev.hypnotic.hypnotic_client.utils.math.MathUtils;
import dev.hypnotic.hypnotic_client.utils.math.TPSUtils;
import net.minecraft.client.MinecraftClient;

public class HUD {
	
	public static HUD INSTANCE = new HUD();
	private MinecraftClient mc = MinecraftClient.getInstance();
	public static Timer animationTimer = new Timer();
	NahrFont fr = FontManager.robotoMed;
	
	public HUD() {
	}
	
	@EventTarget
	public void renderHUD(EventRenderGUI event) {
		if (mc.options.debugEnabled) return;
		if (TPSUtils.INSTANCE.getTimeSinceLastTick() >= 1) {
			String numColor = ColorUtils.green;
			if (TPSUtils.INSTANCE.getTimeSinceLastTick() >= 5 && TPSUtils.INSTANCE.getTimeSinceLastTick() < 10 ) numColor = ColorUtils.yellow;
			else if (TPSUtils.INSTANCE.getTimeSinceLastTick() >= 10) numColor = ColorUtils.red;
			fr.drawCenteredString(event.getMatrices(), "Server lagging for " + numColor + MathUtils.round(TPSUtils.INSTANCE.getTimeSinceLastTick(), 1) + " seconds", mc.getWindow().getScaledWidth() / 2, 50, -1, true);
		}
		for (HudModule element : HudManager.INSTANCE.hudModules) {
			if (element.isEnabled() && !(mc.currentScreen instanceof HudEditorScreen))
			element.render(event.getMatrices(), mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), event.getPartialTicks());
		}
	}
}
