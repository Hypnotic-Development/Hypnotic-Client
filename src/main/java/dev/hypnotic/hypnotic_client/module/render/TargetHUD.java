package dev.hypnotic.hypnotic_client.module.render;

import java.awt.Color;

import dev.hypnotic.hypnotic_client.module.combat.Killaura;
import dev.hypnotic.hypnotic_client.module.hud.HudModule;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ColorSetting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ModeSetting;
import dev.hypnotic.hypnotic_client.ui.HudEditorScreen;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.font.FontManager;
import dev.hypnotic.hypnotic_client.utils.math.MathUtils;
import dev.hypnotic.hypnotic_client.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;

public class TargetHUD extends HudModule {

	public ModeSetting mode = new ModeSetting("Mode", "Compact", "Compact", "Normal");
	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	
	public TargetHUD() {
		super("TargetHUD", "Renders a display with information about your current target", 100, 100, 10, 10);
		addSettings(mode, color);
	}
	
	double animation;
	double animation2;
	double animation3;
	LivingEntity lastTarget = null;
	
	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		this.setDefaultX(100);
		this.setDefaultY(100);
		LivingEntity target = mc.currentScreen instanceof HudEditorScreen || Killaura.target == null ? mc.player : Killaura.target;
		if (lastTarget == null) lastTarget = target;
		if (mc.currentScreen instanceof HudEditorScreen) target = lastTarget;
		double dist1 = RenderUtils.distanceTo(animation2, this.getWidth() + 1);
		double dist2 = RenderUtils.distanceTo(animation3, this.getWidth() + 1);
		if (target != mc.player && dist1 != 0) {
			animation2+=dist1 / 8;
			animation3 = 0;
		} 
		if (target == mc.player) {
			animation2 = 0;
			animation3+=dist2 / 8;
		}
		if (mc.currentScreen instanceof HudEditorScreen) animation2 = this.getWidth();
		if (mode.is("Compact")) {
			this.setWidth(FontManager.robotoMed.getStringWidth(lastTarget.getName().getString()) > 110 - 38 ? 42 + FontManager.robotoMed.getStringWidth(lastTarget.getName().getString()) : 110);
			this.setHeight(36);
			if (lastTarget != null && animation3 <= 120) {
				RenderUtils.startScissor(target == mc.player ? this.getX() + (int)animation3 : this.getX(), this.getY(), target == mc.player ? (int)this.getWidth() : (int)animation2, (int) this.getHeight());
				RenderUtils.fill(matrices, this.getX(), this.getY(), this.getX() + (int)this.getWidth(), this.getY() + (int)this.getHeight(), new Color(35, 35, 35, 250).getRGB());
				RenderUtils.drawFace(matrices, this.getX() + 2, this.getY() + 2, 4, RenderUtils.getPlayerSkin(lastTarget != null ? lastTarget.getUuid() : mc.player.getUuid()));
				FontManager.robotoMed.drawWithShadow(matrices, lastTarget.getName().getString(), this.getX() + 38, this.getY() + 4, -1);
				float percent = target.getHealth() / target.getMaxHealth();
				float length = this.getWidth() - 42;
                float barLength = length * percent;
				double dist = RenderUtils.distanceTo(animation, barLength);
				if (dist != 0) {
					animation+=dist / 10;
				}
				RenderUtils.fill(matrices, this.getX() + 38, this.getY() + 28, this.getX() + 106, this.getY() + 18, color.getColor().darker().getRGB());
				RenderUtils.fill(matrices, this.getX() + 38, this.getY() + 28, this.getX() + 38 + animation, this.getY() + 18, color.getColor().getRGB());
				FontManager.roboto.drawWithShadow(matrices, MathUtils.round(percent * 100, 1) + "%", (this.getX() + 35 + length / 2) - FontManager.robotoSmaller.getStringWidth(MathUtils.round(percent * 100, 1) + "") / 2, this.getY() + 26 - (FontManager.roboto.mcFont ? 9 : 8f), -1);
				RenderUtils.endScissor();
				if (target != mc.player && target != null) lastTarget = target;
			}
		} else if (mode.is("Normal")) {
			this.setWidth(200);
			this.setHeight(80);
			if (Killaura.target != null) {
				
			}
		}
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
}
