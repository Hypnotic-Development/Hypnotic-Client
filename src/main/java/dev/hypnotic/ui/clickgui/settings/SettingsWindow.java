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
package dev.hypnotic.ui.clickgui.settings;

import java.awt.Color;
import java.util.ArrayList;

import dev.hypnotic.config.PositionsConfig;
import dev.hypnotic.settings.Setting;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.ui.HypnoticScreen;
import dev.hypnotic.ui.clickgui.ModuleButton;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class SettingsWindow {

	public ModuleButton parent;
	private ArrayList<Setting> settings;
	private ArrayList<Component> components;
	public int x = 400, y = 100, dragX, dragY;
	public boolean dragging = false;
	
	double scroll;
	public SettingsWindow(ModuleButton parent) {
		this.parent = parent;
		
		if (parent.mod.getSettings() != null) {
        	settings = parent.mod.getSettings();
        }
		
		components = new ArrayList<Component>();
		
		if (settings != null) {
			int count = 0;
			for (Setting setting : settings) {
				if (setting instanceof BooleanSetting) {
					this.components.add(new CheckBox(x + 10, this.y + count, this, setting));
				} else if (setting instanceof ModeSetting) {
					this.components.add(new ComboBox(x + 10, y + count, this, setting));
				} else if (setting instanceof NumberSetting) {
					this.components.add(new Slider(x + 10, y + count, this, setting));
				} else if (setting instanceof ColorSetting) {
					this.components.add(new ColorBox(x + 10, y + count, this, setting));
				}
				count+=20;
			}
		}
	}
	
	int animScroll;
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if (dragging) {
			x = mouseX - dragX;
			y = mouseY - dragY;
		}
		RenderUtils.drawRoundedRect(matrices, x, y, x + 190, y + 250, 10, new Color(50, 50, 50));
		HypnoticScreen.fontBig.drawWithShadow(matrices, parent.mod.getName(), x, y - 6, -1);
		HypnoticScreen.fontSmall.drawWithShadow(matrices, parent.mod.getDescription(), x + 1, y + 12, ColorUtils.transparent(-1, 180));
		RenderUtils.gradientFill(matrices, x - 10, y + 25, x + 200, y + 30, new Color(50, 50, 50).darker().getRGB(), 0);
		RenderUtils.startScissor(x, y + 30, 200, 228);
		double distance = RenderUtils.distanceTo(animScroll, scroll);
		if (distance != 0) {
			animScroll+=distance / 10;
		}
		int count = 40;
		for (Component component : components) {
			if (!components.isEmpty() && component.setting.isVisible()) {
				component.setX(x + 10);
				component.setY(y + count - animScroll);
				component.render(matrices, mouseX, mouseY);
				if (component instanceof ComboBox) {
					count+=((ComboBox)component).anim;
				}
				if (component instanceof ColorBox) {
					if (((ColorBox)component).open)
					count+=80;
				}
				count+=20;
			}
		}
		RenderUtils.endScissor();
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (mouseX >= x - 10 && mouseX <= x + 200 && mouseY >= y - 10 && mouseY <= y + 30) {
			if (button == 0) {
				dragging = true;
				dragX = ((int) (mouseX - x));
				dragY = ((int) (mouseY - y));
			}
		}
		for (Component c : components) {
			c.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		for (Component c : components) {
			c.mouseReleased(mouseX, mouseY, button);
		}
		if (button == 0) {
			dragging = false;
			PositionsConfig.INSTANCE.save();
		}
	}
	
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
	}
	
	public void mouseScrolled(double amount) {
		if (amount < 0) {
            this.scroll += 25;
            if (this.scroll < 0) {
                this.scroll = 0;
            }
        } else if (amount > 0) {
            this.scroll -= 25;
            if (this.scroll < 0) {
                this.scroll = 0;
            }
        }
	}
}
