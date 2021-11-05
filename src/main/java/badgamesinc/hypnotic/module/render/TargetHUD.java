package badgamesinc.hypnotic.module.render;

import java.awt.Color;

import badgamesinc.hypnotic.module.combat.Killaura;
import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.ui.HudEditorScreen;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import badgamesinc.hypnotic.utils.math.MathUtils;
import badgamesinc.hypnotic.utils.player.PlayerUtils;
import badgamesinc.hypnotic.utils.render.RenderUtils;
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
			this.setWidth(120);
			this.setHeight(45);
			if (lastTarget != null) {
				RenderUtils.startScissor(target == mc.player ? this.getX() + (int)animation3 : this.getX(), this.getY(), target == mc.player ? (int)this.getWidth() : (int)animation2, (int) this.getHeight());
				RenderUtils.drawRoundedRect(matrices, this.getX() + 4, this.getY() + 4, this.getX() + (int)this.getWidth() - 4, this.getY() + (int)this.getHeight() - 4, 4, new Color(45, 45, 45));
				RenderUtils.drawFace(matrices, this.getX() + 4, this.getY() + 4, 4, RenderUtils.getPlayerSkin(lastTarget != null ? lastTarget.getUuid() : mc.player.getUuid()));
				FontManager.robotoMed2.drawWithShadow(matrices, lastTarget.getName().getString(), this.getX() + 45, this.getY() + 4, -1);
				int ping = mc.getNetworkHandler().getPlayerListEntry(lastTarget.getUuid()) != null ? mc.getNetworkHandler().getPlayerListEntry(lastTarget.getUuid()).getLatency() : 0;
				FontManager.roboto.drawWithShadow(matrices, "Ping: " + ping, this.getX() + 45, this.getY() + 18f, -1);
				FontManager.roboto.drawWithShadow(matrices, "Distance: " + MathUtils.round(PlayerUtils.distanceTo(lastTarget), 2), this.getX() + 45, this.getY() + 28f, -1);
				float percent = target.getHealth() / target.getMaxHealth();
                float barLength = (int) ((this.getWidth() - 5) * percent);
				double dist = RenderUtils.distanceTo(animation, barLength);
				if (dist != 0) {
					animation+=dist / 10;
				}
				RenderUtils.fill(matrices, this.getX() + 2, this.getY() + this.getHeight() - 1, this.getX() + this.getWidth() - 2, this.getY() + this.getHeight() - 4, color.getColor().darker().getRGB());
				RenderUtils.drawFilledCircle(matrices, this.getX() + this.getWidth() - 4, this.getY() + this.getHeight() - 4, 3, color.getColor().darker());
				RenderUtils.fill(matrices, this.getX() + 2, this.getY() + this.getHeight() - 1, this.getX() + 2 + animation, this.getY() + this.getHeight() - 4, color.getColor().getRGB());
				RenderUtils.drawFilledCircle(matrices, this.getX() + animation, this.getY() + this.getHeight() - 4, 3, color.getColor());
				RenderUtils.drawFilledCircle(matrices, this.getX() + 1, this.getY() + this.getHeight() - 4, 3, color.getColor());
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
