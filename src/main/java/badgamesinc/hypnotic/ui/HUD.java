package badgamesinc.hypnotic.ui;

import java.awt.Color;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import badgamesinc.hypnotic.event.EventManager;
import badgamesinc.hypnotic.event.events.EventRenderGUI;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.utils.Timer;
import badgamesinc.hypnotic.utils.font.CustomFontRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class HUD {
	
	public static HUD INSTANCE = new HUD();
	private MinecraftClient mc = MinecraftClient.getInstance();
	public static Timer animationTimer = new Timer();
	
	public HUD() {
	}
	
	public void renderHUD(MatrixStack matrix, int scaledWidth, int scaledHeight) {
		renderArrayList(matrix, scaledWidth, scaledHeight);
	}
	
	
	public void renderArrayList(MatrixStack matrix, int width, int height) {	
		int off = 0;
		int count = 0;
//		CustomFontRenderer fr = new CustomFontRenderer("/assets/hypnotic/fonts/Roboto-Regular.ttf", 25);
//		fr.drawString(matrix, "JEsfsFE", 10, 10);
		boolean shouldmove = animationTimer.hasTimeElapsed(1000 / 75, true);
		CopyOnWriteArrayList<Mod> modules = new CopyOnWriteArrayList<Mod>(ModuleManager.INSTANCE.modules);
		TextRenderer tr = mc.textRenderer;
		modules.sort(Comparator.comparingInt(m -> tr.getWidth(((Mod)m).getDisplayName())).reversed());
		for (Mod mod : modules) {
			if (mod.animation > 0) {
				DrawableHelper.fill(matrix, (int) (width - tr.getWidth(mod.getDisplayName()) - 10 + 100 - mod.animation), (int) (-100 + mod.animation + tr.fontHeight + off - 2), width - 7, (int) (-100 + mod.animation + tr.fontHeight * 2 + off), new Color(0, 0, 0, 180).getRGB());
				tr.draw(matrix, mod.getDisplayName(), width - tr.getWidth(mod.getDisplayName()) - 8 + 100 - mod.animation, -100 + mod.animation + tr.fontHeight + off, new Color(255, 20, 100).getRGB());
				off+=11;
				
				count++;
			}
			
			if (shouldmove) {
				if (mod.isEnabled()) {
					if (mod.animation < 100) {
						mod.animation += 10;
						
					}
				} else {
					if (mod.animation > 0) {
						mod.animation -= 10;
					}
				}
			}
		}
	}
}
