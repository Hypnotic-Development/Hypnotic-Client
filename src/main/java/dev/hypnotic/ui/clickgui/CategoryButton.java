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
package dev.hypnotic.ui.clickgui;

import static dev.hypnotic.utils.MCUtils.mc;

import java.awt.Color;

import dev.hypnotic.module.Category;
import dev.hypnotic.ui.HypnoticScreen;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;

public class CategoryButton {

	int x, y, width, height, fadeIn;
	Category category;
	ClickGUI parent;
	
	public CategoryButton(int x, int y, Category category, ClickGUI parent) {
        this.x = x;
        this.y = y;
        this.category = category;
        this.parent = parent;
        this.width = 100;
        this.height = 30;
        this.fadeIn = 0;
    }
	
	public void init() {
		fadeIn = 0;
	}
	
	
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		this.height = 30;
		this.width = 100;
		if (fadeIn < 255)
			fadeIn+=5;
		if (hovered(mouseX, mouseY))
			RenderUtils.fill(matrices, x, y, x + width, y + height, new Color(0, 0, 0, 100).getRGB());
		if (parent.dragging && parent.currentCategory == this.category) {
			RenderUtils.fill(matrices, x, y, x + width, y + height, ColorUtils.defaultClientColor);
		}
		HypnoticScreen.font.drawWithShadow(matrices, category.name, x + width / 3, y + 11, -1);
		FontManager.icons.drawWithShadow(matrices, category.icon, x + width / 6, y + 11, -1);
		
	}
	
	public boolean hovered(double mouseX, double mouseY) {
		return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (hovered(mouseX, mouseY) && button == 0) {
			parent.currentCategory = category;
			mc.getSoundManager().play(PositionedSoundInstance.ambient(SoundEvents.UI_BUTTON_CLICK, 1, 1));
		}
	}
}
