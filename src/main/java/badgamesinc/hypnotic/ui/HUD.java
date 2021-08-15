package badgamesinc.hypnotic.ui;

import java.awt.Color;
import java.util.Comparator;
import java.util.concurrent.CopyOnWriteArrayList;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventRenderGUI;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.MathUtils;
import badgamesinc.hypnotic.utils.TPSUtils;
import badgamesinc.hypnotic.utils.Timer;
import badgamesinc.hypnotic.utils.font.FontManager;
import badgamesinc.hypnotic.utils.font.NahrFont;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
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
			fr.drawCenteredString(event.getMatrices(), "Server lagging for " + numColor + MathUtils.round(TPSUtils.INSTANCE.getTimeSinceLastTick(), 1) + " seconds", mc.getWindow().getScaledWidth() / 2, 50, -1, true, true);
		}
	}
	
	public void renderSideHUD(MatrixStack matrices, int width, int height) {
		fr.drawWithShadow(matrices, Hypnotic.name + ColorUtils.gray + " " + Hypnotic.version, 5, 5, ColorUtils.getClientColorInt(), true);
		fr.drawWithShadow(matrices, "FPS " + ColorUtils.gray + mc.fpsDebugString.split(" ")[0], 5, 15, ColorUtils.getClientColorInt(), true);
		fr.drawWithShadow(matrices, "TPS " + ColorUtils.gray + MathUtils.round(TPSUtils.INSTANCE.getAverageTPS(), 2), 5, 25, ColorUtils.getClientColorInt(), true);
		fr.drawWithShadow(matrices, "Ping " + ColorUtils.gray + (mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()) == null ? 0 : mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()).getLatency()), 5, 35, ColorUtils.getClientColorInt(), true);
		fr.drawWithShadow(matrices, "Blocks/s " + ColorUtils.gray + MathUtils.round(ModuleManager.INSTANCE.getModule(badgamesinc.hypnotic.module.world.Timer.class).isEnabled() ? moveSpeed() * ModuleManager.INSTANCE.getModule(badgamesinc.hypnotic.module.world.Timer.class).speed.getValue() : moveSpeed(), 2), 5, 45, ColorUtils.getClientColorInt(), true);
		fr.drawWithShadow(matrices, "X " + ColorUtils.gray + MathUtils.round(mc.player.getX(), 1) + ColorUtils.reset + " Y " + ColorUtils.gray + MathUtils.round(mc.player.getY(), 1) + ColorUtils.reset + " Z " + ColorUtils.gray + MathUtils.round(mc.player.getZ(), 1), 5, 55, ColorUtils.getClientColorInt(), true);
	}
	
	
	public void renderArrayList(MatrixStack matrix, int width, int height, EventRenderGUI event) {	
		int off = 0;
		boolean shouldmove = animationTimer.hasTimeElapsed(1000 / 75, true);
		CopyOnWriteArrayList<Mod> modules = new CopyOnWriteArrayList<Mod>(ModuleManager.INSTANCE.modules);
		modules.sort(Comparator.comparingInt(m -> (int)fr.getStringWidth(((Mod)m).getDisplayName(), true)).reversed());
		for (Mod mod : modules) {
			if (!mod.visible.isEnabled()) continue;
			
			if (mod.animation > 0 && mod.visible.isEnabled()) {
//				DrawableHelper.fill(matrix, (int) (width - fr.getStringWidth(mod.getDisplayName(), true) - 9.5f + 100 - (mod.animation)), (int) (fr.getStringHeight(mod.getDisplayName(), true) + off + 1), width - 6 + (int)(100 - mod.animation), (int) (fr.getStringHeight(mod.getDisplayName(), true) * 2 + off), new Color(0, 0, 0, 180).getRGB());
				fr.drawWithShadow(matrix, mod.getDisplayName(), width - fr.getStringWidth(mod.getDisplayName(), true) - 8 + 100 - mod.animation, fr.getStringHeight(mod.getDisplayName(), true) + off - 3, /*ColorUtils.fade(ColorUtils.getClientColor(), count, ModuleManager.INSTANCE.getEnabledModules().size()).getRGB()*/ ColorUtils.getClientColorInt(), true);
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
	
	private double moveSpeed() {
        Vec3d move = new Vec3d(mc.player.getX() - mc.player.prevX, 0, mc.player.getZ() - mc.player.prevZ).multiply(20);

        return Math.abs(length2D(move)) ;
    }
	
	public double length2D(Vec3d vec3d) {
        return MathHelper.sqrt((float)(vec3d.x * vec3d.x + vec3d.z * vec3d.z));
    }
}
