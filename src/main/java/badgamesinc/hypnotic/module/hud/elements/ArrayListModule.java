package badgamesinc.hypnotic.module.hud.elements;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mojang.blaze3d.systems.RenderSystem;

import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.Timer;
import net.minecraft.client.util.math.MatrixStack;

public class ArrayListModule extends HudModule {

	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	public static Timer animationTimer = new Timer();
	
	public ArrayListModule() {
		super("ArrayList", "Renders all of the enabled modules", -10000, -10000, -100, -100);
		this.setEnabled(true);
		addSettings(color);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		int off = 0;
		int off2 = 0;
		boolean shouldmove = animationTimer.hasTimeElapsed(1000 / 75, true);
		this.setDefaultX(scaledWidth);
		this.setDefaultY(0);
		if (this.getX() < -1000) {
			this.setX(this.getDefaultX());
			this.setY(this.getDefaultY());
		}
		CopyOnWriteArrayList<Mod> modules = new CopyOnWriteArrayList<Mod>(ModuleManager.INSTANCE.modules);
		List<String> names = new ArrayList<>();
		modules.sort(Comparator.comparingInt(m -> (int)font.getStringWidth(((Mod)m).getDisplayName())).reversed());
		for (Mod mod : modules) {
			if (!mod.visible.isEnabled()) continue;
			
			if (mod.animation > 0 && mod.visible.isEnabled()) {
				names.add(mod.getDisplayName());
				font.drawWithShadow(matrices, mod.getDisplayName(), this.getX() - 27 + this.getWidth() - font.getStringWidth(mod.getDisplayName()) + 50 - mod.animation / 4, this.getY() + font.getStringHeight(mod.getDisplayName()) + (mod.isEnabled() ? off : off2) - 15, /*ColorUtils.fade(ColorUtils.getClientColor(), count, ModuleManager.INSTANCE.getEnabledModules().size()).getRGB()*/ color.getRGB());
				off+=mod.animation / 9;
				off2+=11;
			} else {
				if (names.contains(mod.getDisplayName())) names.remove(mod.getDisplayName());
			}
			if (shouldmove) {
				if (mod.isEnabled()) {
					if (mod.animation < 100) {
						mod.animation+=10;
					}
				} else {
					if (mod.animation > 0) {
						mod.animation-=10;
					}
				}
			}
		}
		String longest = names.stream().max(Comparator.comparingInt((name)-> (int)font.getStringWidth((String)name))).get();
		this.setHeight(off);
		this.setWidth((int)font.getStringWidth(longest) + 0);
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
