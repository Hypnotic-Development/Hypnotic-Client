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
	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		this.setDefaultX(100);
		this.setDefaultY(100);
		LivingEntity target = mc.currentScreen instanceof HudEditorScreen ? mc.player : Killaura.target;
		if (mode.is("Compact")) {
			this.setWidth(120);
			this.setHeight(40);
			if (target != null) {
				RenderUtils.fill(matrices, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), new Color(45, 45, 45).getRGB());
				RenderUtils.drawFace(matrices, this.getX() + 4, this.getY() + 4, 4, RenderUtils.getPlayerSkin(target.getUuid()));
				FontManager.roboto.drawWithShadow(matrices, target.getName().getString(), this.getX() + 45, this.getY() + 2, -1);
				FontManager.robotoSmall.drawWithShadow(matrices, "Health: " + MathUtils.round(target.getHealth(), 2), this.getX() + 45, this.getY() + 13.5f, -1);
				float percent = target.getHealth() / target.getMaxHealth();
                float barLength = (int) ((60) * percent);
				double dist = RenderUtils.distanceTo(animation, barLength);
				if (dist != 0) {
					animation+=dist / 10;
				}
				RenderUtils.fill(matrices, this.getX() + 45, this.getY() + 25, this.getX() + 105, this.getY() + 31, color.getColor().darker().getRGB());
				RenderUtils.fill(matrices, this.getX() + 45, this.getY() + 25, this.getX() + 45 + animation, this.getY() + 31, color.getColor().getRGB());
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
