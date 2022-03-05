/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.module.hud.elements;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Timer;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class ArrayListModule extends HudModule {

	public ModeSetting location = new ModeSetting("Location", "Top", "Top", "Bottom");
	public BooleanSetting background = new BooleanSetting("Background", true);
	public NumberSetting backgroundOpacity = new NumberSetting("Background Opacity", 150, 10, 255, 1);
	public BooleanSetting colorBar = new BooleanSetting("Color Bar", true);
	public ModeSetting colorBarMode = new ModeSetting("Color Bar Mode", "Side", "Side", "Top", "Outline");
	public ModeSetting colorMode = new ModeSetting("Color Mode", "Custom", "Custom", "Category", "Rainbow", "Fade", "Fade Between");
	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	public ColorSetting color2 = new ColorSetting("Color 2", ColorUtils.defaultClientColor().brighter());
	public NumberSetting speed = new NumberSetting("Speed", 4, 0.1, 10, 0.1);
	public NumberSetting sat = new NumberSetting("Saturation", 0.6, 0, 1, 0.1);
	public NumberSetting spread = new NumberSetting("Spread", 120, 0, 200, 10);
	public static Timer animationTimer = new Timer();
	
	public ArrayListModule() {
		super("ArrayList", "Renders all of the enabled modules", -10000, -10000, -100, -100);
		this.setEnabled(true);
		addSettings(location, background, backgroundOpacity, colorBar, colorBarMode, colorMode, color, color2, speed, sat, spread);
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
		CopyOnWriteArrayList<Mod> modules = new CopyOnWriteArrayList<Mod>(ModuleManager.INSTANCE.modules);
		CopyOnWriteArrayList<Mod> enabledModules = new CopyOnWriteArrayList<Mod>(ModuleManager.INSTANCE.getEnabledModules());
		boolean shouldmove = animationTimer.hasTimeElapsed(1000 / 75, true);
		List<String> names = new ArrayList<>();
		modules.sort(Comparator.comparingInt(m -> (int)font.getStringWidth(((Mod)m).getDisplayName())).reversed());
		enabledModules.sort(Comparator.comparingInt(m -> (int)font.getStringWidth(((Mod)m).getDisplayName())).reversed());
		int count = 1;
		String longestName = "";
		for (Mod mod : modules) {
			if (!mod.visible.isEnabled()) continue;
			
			switch(colorMode.getSelected()) {
				case "Custom":
					color = this.color.getColor().getRGB();
					break;
				case "Rainbow":
					color = ColorUtils.rainbow((float)speed.getValue(), (float)sat.getValue(), 1, count * (long)spread.getValue());
					break;
				case "Fade":
					color = ColorUtils.fade(this.color.getColor(), (int) (count * (speed.getValue())), ModuleManager.INSTANCE.getEnabledModules().size() * (int)(spread.getValue() / 5)).getRGB();
					break;
				case "Fade Between":
					color = ColorUtils.fadeBetween(this.color.getColor().getRGB(), this.color2.getColor().getRGB(), count * (long)(spread.getValue()));
					break;
				case "Category":
					color = ColorUtils.getCategoryColor(mod).getRGB();
					break;
			}
			if ((mod.animation > 0 || mod.offset > 0) && mod.visible.isEnabled()) {
				longestName = font.getStringWidth(longestName) < font.getStringWidth(mod.getDisplayName()) ? mod.getDisplayName() : longestName;
				names.add(mod.getDisplayName());
				float yPos = location.is("Top") ? -5 + font.getStringHeight(mod.getDisplayName()) + off : scaledHeight - font.getStringHeight(mod.getDisplayName()) - off - 10;
				RenderUtils.startScissor(scaledWidth - 13 - (int)font.getStringWidth(mod.getDisplayName()) - 2, (int)yPos - 1, (int)font.getStringWidth(mod.getDisplayName()) + 8, 16);
				Mod mod2 = enabledModules.get(enabledModules.indexOf(mod) + 1 < enabledModules.size() ? enabledModules.indexOf(mod) + 1 : enabledModules.indexOf(mod));
				if (mod.animation > 0) {
					if (background.isEnabled()) RenderUtils.fill(matrices, (float) (scaledWidth - 10 - mod.animation) - 2, yPos + 1, (float) (scaledWidth - 10 - mod.animation) + font.getStringWidth(mod.getDisplayName()) + 2, yPos + 12, ColorUtils.transparent((int)backgroundOpacity.getValue()));
					font.drawWithShadow(matrices, mod.getDisplayName(), (float) (scaledWidth - 10 - mod.animation), yPos, color);
				}
				if (colorBar.isEnabled()) {
					switch(colorBarMode.getSelected()) {
						case "Side":
							RenderUtils.fill(matrices, (float) (scaledWidth - 8), yPos + 1, (float) (scaledWidth - 6), yPos + 12, color);
							break;
						case "Top":
							if (location.is("Top") ? enabledModules.indexOf(mod) == 0 : enabledModules.indexOf(mod) == enabledModules.size() - 1) RenderUtils.fill(matrices, (float) (scaledWidth - 12) - mod.animation, yPos + 1, (float) (scaledWidth - 8) - mod.animation + font.getStringWidth(mod.getDisplayName()), yPos - 1, color);
							break;
						case "Outline":
							RenderUtils.fill(matrices, (float) (scaledWidth - 8), yPos + 1, (float) (scaledWidth - 7), yPos + 12, color);
							if (location.is("Top") ? enabledModules.indexOf(mod) == 0 : enabledModules.indexOf(mod) == enabledModules.size() - 1) RenderUtils.fill(matrices, (float) (scaledWidth - 13) - mod.animation, yPos + 1, (float) (scaledWidth - 7) - mod.animation + font.getStringWidth(mod.getDisplayName()), yPos, color);
							if (!location.is("Top") ? enabledModules.indexOf(mod) == 0 : enabledModules.indexOf(mod) == enabledModules.size() - 1) RenderUtils.fill(matrices, (float) (scaledWidth - 13) - mod.animation, yPos + 12, (float) (scaledWidth - 7) - mod.animation + font.getStringWidth(mod.getDisplayName()), yPos + 13, color);
							if (mod.animation >= font.getStringWidth(mod.getDisplayName()) - 1) RenderUtils.fill(matrices, (float) (scaledWidth - 12) - font.getStringWidth(mod.getDisplayName()), yPos + 13, (float) (scaledWidth - 12) - (mod2.animation), yPos + 12, color);
							if (mod.animation > 1) RenderUtils.fill(matrices, scaledWidth - 12 - mod.animation, yPos + 1, (scaledWidth - 13) - mod.animation, yPos + 13, color);
							break;
					}
				}
				RenderUtils.endScissor();
				off+=mod.offset;
			} else {
				if (names.contains(mod.getDisplayName())) names.remove(mod.getDisplayName());
			}
			count++;
			
			double distance = RenderUtils.distanceTo(mod.animation, font.getStringWidth(mod.getDisplayName()));
			double distance2 = RenderUtils.distanceTo(mod.animation, -1);
			double distanceOffset = RenderUtils.distanceTo(mod.offset, 12);
			double distanceOffset2 = RenderUtils.distanceTo(mod.offset, 0);
			if (shouldmove) {
				if (mod.isEnabled()) {
					if (distanceOffset != 0) {
						mod.offset+=distanceOffset / 4;
					}
					if (mod.offset > 12 || mod.offset >= 11.7) mod.offset = 11;
					if (distance != 0) {
						mod.animation+=distance / 6;
					}
					if (mod.animation > font.getStringWidth(mod.getDisplayName())) {
						mod.animation = font.getStringWidth(mod.getDisplayName());
					}
				} else {
					if (distanceOffset2 != 0 && mod.animation <= 0) {
						mod.offset+=distanceOffset2 / 6;
					}
					if (mod.offset < 0 || mod.offset <= 0.5) mod.offset = 0;
					if (distance != font.getStringWidth(mod.getDisplayName())) {
						mod.animation+=distance2 / 6;
					}
					if (mod.animation < 0) {
						mod.animation = 0;
					}
				}
			}
		}
		this.setHeight(count * 3);
		this.setWidth(font.getStringWidth(longestName));
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}
	
	@Override
	public void onTickDisabled() {
		colorBarMode.setVisible(colorBar.isEnabled());
		color.setVisible(colorMode.is("Custom") || colorMode.is("Fade") || colorMode.is("Fade Between"));
		speed.setVisible(colorMode.is("Rainbow") || colorMode.is("Fade"));
		sat.setVisible(colorMode.is("Rainbow"));
		spread.setVisible(colorMode.is("Rainbow") || colorMode.is("Fade") || colorMode.is("Fade Between"));
		color2.setVisible(colorMode.is("Fade Between"));
		super.onTickDisabled();
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
