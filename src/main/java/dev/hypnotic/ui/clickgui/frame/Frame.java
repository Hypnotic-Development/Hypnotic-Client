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
package dev.hypnotic.ui.clickgui.frame;

import dev.hypnotic.module.Category;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class Frame {

	private int x, y, width, height, centerX, centerY;
	
	public Frame(int scaledWidth, int scaledHeight) {
		this.x = 200;
		this.y = 100;
		this.width = 200;
		this.height = 100;
		this.centerX = scaledWidth / 2;
		this.centerY = scaledHeight / 2;
		System.out.println(scaledWidth + "height" + scaledHeight);
	}
	
	public void draw(MatrixStack matrices, int mouseX, int mouseY) {
		this.drawCategories(matrices, mouseX, mouseY);
	}
	
	public void drawCategories(MatrixStack matrices, int mouseX, int mouseY) {
		Screen.fill(matrices, centerX - x, centerY - y, centerX + width, centerY + height, -1);
		
	}
	
	public boolean hoveredCat(Category category, int mouseX, int mouseY) {
		return false;
	}
	
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		
	}
}
