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
package dev.hypnotic.ui.clickgui2.frame.button;

import java.awt.Color;
import java.util.ArrayList;

import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.Setting;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.ui.BindingScreen;
import dev.hypnotic.ui.HypnoticScreen;
import dev.hypnotic.ui.WaypointScreen;
import dev.hypnotic.ui.clickgui2.ClickGUI;
import dev.hypnotic.ui.clickgui2.frame.Frame;
import dev.hypnotic.ui.clickgui2.frame.button.settings.CheckBox;
import dev.hypnotic.ui.clickgui2.frame.button.settings.ColorBox;
import dev.hypnotic.ui.clickgui2.frame.button.settings.ComboBox;
import dev.hypnotic.ui.clickgui2.frame.button.settings.Component;
import dev.hypnotic.ui.clickgui2.frame.button.settings.Slider;
import dev.hypnotic.waypoint.Waypoint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class Button {

	public Mod mod;
	public Frame parent;
	private int x, y, width, height, offset;
	private boolean extended;
	public ArrayList<Component> components;
	public ArrayList<Component> subComponents;
	public float animation = 0;
	
	public Button(Mod mod, int x, int y, int offset, Frame parent) {
		this.mod = mod;
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.width = parent.getWidth();
		this.height = parent.getHeight();
		this.offset = offset;
		this.extended = false;
		this.components = new ArrayList<Component>();
		
		int count = height;
		for (Setting setting : mod.getSettings()) {
			 if (setting instanceof ColorSetting) {
				components.add(new ColorBox(x, y + count, setting, this));
			} else if (setting instanceof BooleanSetting) {
				components.add(new CheckBox(x, y + count, this, setting));
			} else if (setting instanceof ModeSetting) {
				components.add(new ComboBox(x, y + count, setting, this));
			} else if (setting instanceof NumberSetting) {
				components.add(new Slider(x, y + count, this, setting));
			}
			count+=(setting instanceof ColorSetting ? height * 10 : height);
		}
	}
	
	int animTicks = 0;
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		int color = parent.color.getRGB();
		if (mod.isEnabled()) {
			if (animTicks < 255) animTicks+=15;
		} else if (!mod.isEnabled()) {
			if (animTicks > 0) animTicks-=15;
		}
		Screen.fill(matrices, x, y, x + width, y + height, new Color(40, 40, 40, 255).getRGB());
		Screen.fill(matrices, x, y, x + width, y + height, new Color(parent.color.getRed(), parent.color.getGreen(), parent.color.getBlue(), animTicks).darker().getRGB());
			
		if (hovered(mouseX, mouseY)) Screen.fill(matrices, x, y, x + width, y + height, new Color(50, 50, 50, 100).getRGB());
		
		int nameColor = -1;
		
		HypnoticScreen.font.drawWithShadow(matrices, mod.getName(), x + (height / 3), y + (height / 6), nameColor);
		Screen.fill(matrices, x, y, x + 1, y + height, color);
		Screen.fill(matrices, x + width, y, x + width - 1, y + height, color);
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (hovered(mouseX, mouseY)) {
			if (button == 0) {
				mod.toggle();
			} else if (button == 1) {
				this.extended = !this.extended;
				parent.updateButtons();
			} else if (button == 2) {
				if (!(mod instanceof Waypoint)) {
					mod.setBinding(true);
					MinecraftClient.getInstance().setScreen(new BindingScreen(mod, ClickGUI.INSTANCE));
				} else {
					MinecraftClient.getInstance().setScreen(new WaypointScreen((Waypoint)mod));
				}
			}
		}
		
		for (Component component : components) {
			component.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	public void mouseReleased(int button) {
		for (Component component : components) {
			component.mouseReleased(button);
		}
	}
	
	public boolean hovered(double mouseX, double mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isExtended() {
		return extended;
	}

	public void setExtended(boolean extended) {
		this.extended = extended;
	}
}
