package badgamesinc.hypnotic.module.hud.elements;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.Timer;
import net.minecraft.client.util.math.MatrixStack;

public class ArrayListModule extends HudModule {

	public ModeSetting location = new ModeSetting("Location", "Top", "Top", "Bottom");
	public ModeSetting colorMode = new ModeSetting("Color Mode", "Custom", "Custom", "Category", "Rainbow", "Fade");
	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	public NumberSetting speed = new NumberSetting("Speed", 4, 0.1, 10, 0.1);
	public NumberSetting sat = new NumberSetting("Saturation", 0.6, 0, 1, 0.1);
	public NumberSetting spread = new NumberSetting("Spread", 120, 0, 200, 10);
	public static Timer animationTimer = new Timer();
	
	public ArrayListModule() {
		super("ArrayList", "Renders all of the enabled modules", -10000, -10000, -100, -100);
		this.setEnabled(true);
		addSettings(location, colorMode, color, speed, sat, spread);
		this.setDraggable(false);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		int off = 0;
		int color = ColorUtils.defaultClientColor;
		this.setDefaultX(scaledWidth);
		this.setDefaultY(0);
		if (this.getX() < -1000) {
			this.setX(this.getDefaultX());
			this.setY(this.getDefaultY());
		}
		CopyOnWriteArrayList<Mod> modules = new CopyOnWriteArrayList<Mod>(ModuleManager.INSTANCE.modules);
		List<String> names = new ArrayList<>();
		modules.sort(Comparator.comparingInt(m -> (int)font.getStringWidth(((Mod)m).getDisplayName())).reversed());
		int count = 1;
		for (Mod mod : modules) {
			if (!mod.visible.isEnabled()) continue;
			
			switch(colorMode.getSelected()) {
				case "Custom":
					color = this.color.getRGB();
					break;
				case "Rainbow":
					color = ColorUtils.rainbow((float)speed.getValue(), (float)sat.getValue(), 1, count * (long)spread.getValue());
					break;
				case "Fade":
					color = ColorUtils.fade(this.color.getColor(), (int) (count * (speed.getValue())), ModuleManager.INSTANCE.getEnabledModules().size() * (int)(spread.getValue() / 5)).getRGB();
					break;
				case "Category":
					color = ColorUtils.getCategoryColor(mod).getRGB();
					break;
			}
			if ((mod.animation > 0 || mod.offset > 0) && mod.visible.isEnabled()) {
				names.add(mod.getDisplayName());
				if (mod.animation > 0) {
					if (location.is("Top")) font.drawWithShadow(matrices, mod.getDisplayName(), scaledWidth - 10 - mod.animation, -5 + font.getStringHeight(mod.getDisplayName()) + off, color);
					else  font.drawWithShadow(matrices, mod.getDisplayName(), scaledWidth - 10 - mod.animation, scaledHeight - font.getStringHeight(mod.getDisplayName()) - off - 10, color);
				}
				off+=mod.offset;
			} else {
				if (names.contains(mod.getDisplayName())) names.remove(mod.getDisplayName());
			}
			count++;
		}
//		String longest = names.stream().max(Comparator.comparingInt((name)-> (int)font.getStringWidth((String)name))).get();
		this.setHeight(0);
		this.setWidth(0);
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
	
	@Override
	public void onTick() {
		CopyOnWriteArrayList<Mod> modules = new CopyOnWriteArrayList<Mod>(ModuleManager.INSTANCE.modules);
		boolean shouldmove = animationTimer.hasTimeElapsed(1000 / 75, true);
		// This is in the onTick method to fix speed variations on different frame rates (i hope)
		for (Mod mod : modules) {
			if (shouldmove) {
				if (mod.isEnabled()) {
					if (mod.offset < 11) {
						mod.offset+=1.5;
					}
					if (mod.animation < font.getStringWidth(mod.getDisplayName())) {
						mod.animation+=4;
					}
					if (mod.animation > font.getStringWidth(mod.getDisplayName())) {
						mod.animation = font.getStringWidth(mod.getDisplayName());
					}
				} else {
					if (mod.offset > 0 && mod.animation <= 0) {
						mod.offset-=1.5;
					}
					if (mod.animation > 0) {
						mod.animation-=4;
					}
					if (mod.animation < 0) {
						mod.animation = 0;
					}
				}
			}
		}
		super.onTick();
	}
	
	@Override
	public void onTickDisabled() {
		color.setVisible(colorMode.is("Custom") || colorMode.is("Fade"));
		speed.setVisible(colorMode.is("Rainbow") || colorMode.is("Fade"));
		sat.setVisible(colorMode.is("Rainbow"));
		spread.setVisible(colorMode.is("Rainbow") || colorMode.is("Fade"));
		super.onTickDisabled();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
