package badgamesinc.hypnotic.ui;

import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventRenderGUI;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.hud.HudManager;
import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.Timer;
import badgamesinc.hypnotic.utils.font.FontManager;
import badgamesinc.hypnotic.utils.font.NahrFont;
import badgamesinc.hypnotic.utils.math.MathUtils;
import badgamesinc.hypnotic.utils.math.TPSUtils;
import badgamesinc.hypnotic.utils.player.PlayerUtils;
import badgamesinc.hypnotic.utils.world.Dimension;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

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
		renderArrayList(event.getMatrices(), mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), event);
		renderSideHUD(event.getMatrices(), mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight());
		
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
	
	public void renderSideHUD(MatrixStack matrices, int width, int height) {
		fr = FontManager.robotoMed2;
	}
	
	
	public void renderArrayList(MatrixStack matrix, int width, int height, EventRenderGUI event) {	
		int off = 0;
		boolean shouldmove = animationTimer.hasTimeElapsed(1000 / 75, true);
		CopyOnWriteArrayList<Mod> modules = new CopyOnWriteArrayList<Mod>(ModuleManager.INSTANCE.modules);
		modules.sort(Comparator.comparingInt(m -> (int)fr.getWidth(((Mod)m).getDisplayName())).reversed());
		for (Mod mod : modules) {
			if (!mod.visible.isEnabled()) continue;
			
			if (mod.animation > 0 && mod.visible.isEnabled()) {
//				DrawableHelper.fill(matrix, (int) (width - fr.getWidth(mod.getDisplayName(), true) - 9.5f + 100 - (mod.animation)), (int) (fr.getStringHeight(mod.getDisplayName(), true) + off + 1), width - 6 + (int)(100 - mod.animation), (int) (fr.getStringHeight(mod.getDisplayName(), true) * 2 + off), new Color(0, 0, 0, 180).getRGB());
				fr.drawWithShadow(matrix, mod.getDisplayName(), width - fr.getWidth(mod.getDisplayName()) - 2 + 100 - mod.animation, fr.getStringHeight(mod.getDisplayName()) + off - 3, /*ColorUtils.fade(ColorUtils.getClientColor(), count, ModuleManager.INSTANCE.getEnabledModules().size()).getRGB()*/ ColorUtils.getClientColorInt());
				off+=11;
			}
			
			if (shouldmove) {
				if (mod.isEnabled()) {
					if (mod.animation < 100) {
						mod.animation += Math.max(event.getPartialTicks() * 10, 10);
						
					}
				} else {
					if (mod.animation > 0) {
						mod.animation -= Math.max(event.getPartialTicks() * 10, 10);
					}
				}
			}
		}
	}
}
