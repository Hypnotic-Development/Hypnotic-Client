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
package dev.hypnotic.ui;

import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class Button {

	private String text;
	private int id;
	private float x, y, width, height;
	public boolean animation;
	public boolean enabled;
	
	public Button(String text, int id, float x, float y, float width, float height, boolean animation) {
		this.text = text;
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.animation = animation;
		this.enabled = true;
	}
	
	public Button(String text, float x, float y, float width, float height, boolean animation) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.animation = animation;
		this.enabled = true;
	}
	
	float hoverTicks = 80;
	public void render(MatrixStack matrices, double mouseX, double mouseY, float delta) {
		if (isHovered(mouseX, mouseY) && hoverTicks > 20) {
			hoverTicks-=5;
		} else if (!isHovered(mouseX, mouseY) && hoverTicks < 80) {
			hoverTicks+=5;
		}
		RenderUtils.drawBorderRect(matrices, x, y - 1, x + width, y + height + 1, ColorUtils.defaultClientColor, 1);
		RenderUtils.fill(matrices, x, y, x + width, y + height, ColorUtils.transparent((int)hoverTicks));
		HypnoticScreen.font.drawCenteredString(matrices, text, x + width / 2, y + 2, -1, true);
	}
	
	public boolean isHovered(double mouseX, double mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height && enabled;
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public int getId() {
		return id;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public double getWidth() {
		return width;
	}
	
	public double getHeight() {
		return height;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
}
