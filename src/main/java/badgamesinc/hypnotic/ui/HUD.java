package badgamesinc.hypnotic.ui;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
<<<<<<< Updated upstream
import badgamesinc.hypnotic.module.render.ClickGUIModule;
=======
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.TPSUtils;
>>>>>>> Stashed changes
import badgamesinc.hypnotic.utils.Timer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class HUD {
	
	public static HUD INSTANCE = new HUD();
	private MinecraftClient mc = MinecraftClient.getInstance();
	public static Timer animationTimer = new Timer();
	TextRenderer tr = mc.textRenderer;
	
	public HUD() {
	}
	
	public void renderHUD(MatrixStack matrix, int scaledWidth, int scaledHeight) {
		if (mc.options.debugEnabled) return;
		renderArrayList(matrix, scaledWidth, scaledHeight);
		renderSideHUD(matrix, scaledWidth, scaledHeight);
	}
	
	public void renderSideHUD(MatrixStack matrices, int width, int height) {
		tr.drawWithShadow(matrices, Hypnotic.name + ColorUtils.gray + " " + Hypnotic.version, 5, 5, ColorUtils.clientColor);
		tr.drawWithShadow(matrices, "FPS " + ColorUtils.gray + mc.fpsDebugString.split(" ")[0], 5, 15, ColorUtils.clientColor);
		tr.drawWithShadow(matrices, "TPS " + ColorUtils.gray + round(TPSUtils.INSTANCE.getAverageTPS(), 2), 5, 25, ColorUtils.clientColor);
		tr.drawWithShadow(matrices, "Ping " + ColorUtils.gray + (mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()) == null ? 0 : mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()).getLatency()), 5, 35, ColorUtils.clientColor);
		tr.drawWithShadow(matrices, "Blocks/s " + ColorUtils.gray + round(moveSpeed(), 2), 5, 45, ColorUtils.clientColor);
		tr.drawWithShadow(matrices, "X " + ColorUtils.gray + round(mc.player.getX(), 1) + ColorUtils.reset + " Y " + ColorUtils.gray + round(mc.player.getY(), 1) + ColorUtils.reset + " Z " + ColorUtils.gray + round(mc.player.getZ(), 1), 5, 55, ColorUtils.clientColor);
	}
	
	
	public void renderArrayList(MatrixStack matrix, int width, int height) {	
		int off = 0;
		int count = 0;
//		CustomFontRenderer fr = new CustomFontRenderer("/assets/hypnotic/fonts/Roboto-Regular.ttf", 25);
//		fr.drawString(matrix, "JEsfsFE", 10, 10);
		boolean shouldmove = animationTimer.hasTimeElapsed(1000 / 75, true);
		CopyOnWriteArrayList<Mod> modules = new CopyOnWriteArrayList<Mod>(ModuleManager.INSTANCE.modules);
		modules.sort(Comparator.comparingInt(m -> tr.getWidth(((Mod)m).getDisplayName())).reversed());
		for (Mod mod : modules) {
			if (mod.animation > 0) {
<<<<<<< Updated upstream
				DrawableHelper.fill(matrix, (int) (width - tr.getWidth(mod.getDisplayName()) - 10 + 100 - mod.animation), (int) (-100 + mod.animation + tr.fontHeight + off - 2), width - 7, (int) (-100 + mod.animation + tr.fontHeight * 2 + off), new Color(0, 0, 0, 180).getRGB());
				tr.draw(matrix, mod.getDisplayName(), width - tr.getWidth(mod.getDisplayName()) - 8 + 100 - mod.animation, -100 + mod.animation + tr.fontHeight + off, new Color(
						(int) ModuleManager.INSTANCE.getModule(ClickGUIModule.class).red.getValue(), 
						(int) ModuleManager.INSTANCE.getModule(ClickGUIModule.class).green.getValue(), 
						(int) ModuleManager.INSTANCE.getModule(ClickGUIModule.class).blue.getValue()
						).getRGB());
=======
//				DrawableHelper.fill(matrix, (int) (width - tr.getWidth(mod.getDisplayName()) - 10 + 100 - mod.animation), (int) (-100 + mod.animation + tr.fontHeight + off - 2), width - 7, (int) (-100 + mod.animation + tr.fontHeight * 2 + off), new Color(0, 0, 0, 180).getRGB());
				tr.drawWithShadow(matrix, mod.getDisplayName(), width - tr.getWidth(mod.getDisplayName()) - 8 + 100 - mod.animation, tr.fontHeight + off, new Color(255, 20, 100).getRGB());
>>>>>>> Stashed changes
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
	
	public double round(double num, double increment) {
        if (increment < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(num);
        bd = bd.setScale((int) increment, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
	
	private double moveSpeed() {
        Vec3d move = new Vec3d(mc.player.getX() - mc.player.prevX, 0, mc.player.getZ() - mc.player.prevZ).multiply(20);

        return Math.abs(length2D(move)) ;
    }
	
	public double length2D(Vec3d vec3d) {
        return MathHelper.sqrt((float)(vec3d.x * vec3d.x + vec3d.z * vec3d.z));
    }
}
