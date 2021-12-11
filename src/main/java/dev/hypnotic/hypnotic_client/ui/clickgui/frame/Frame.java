package dev.hypnotic.hypnotic_client.ui.clickgui.frame;

import dev.hypnotic.hypnotic_client.module.Category;
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
