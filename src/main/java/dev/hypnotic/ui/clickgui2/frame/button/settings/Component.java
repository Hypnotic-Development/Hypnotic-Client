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
package dev.hypnotic.ui.clickgui2.frame.button.settings;

import dev.hypnotic.settings.Setting;
import dev.hypnotic.ui.clickgui2.frame.button.Button;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class Component {

	MinecraftClient mc = MinecraftClient.getInstance();
	protected TextRenderer tr = mc.textRenderer;
	public int offset;
	private int x, y;
	public Setting setting;
	private boolean extended;
	
	public Component(int x, int y, Setting setting, Button parent) {
		
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY, int offset) {
		this.offset = offset;
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 1) this.extended = true;
	}
	
	public void mouseReleased(int button) {
		
	}
	
	public boolean hovered(int mouseX, int mouseY) {
		return false;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public boolean isExtended() {
		return extended;
	}
	
	public void setExtended(boolean extended) {
		this.extended = extended;
	}
}
