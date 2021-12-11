package dev.hypnotic.hypnotic_client.ui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;

import com.google.common.collect.Lists;

import dev.hypnotic.hypnotic_client.config.SaveLoad;
import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.ModuleManager;
import dev.hypnotic.hypnotic_client.module.render.ClickGUIModule;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.font.FontManager;
import dev.hypnotic.hypnotic_client.utils.render.RenderUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class Frame {

	public Category category;
	public String title;
	private int x, y, width, height, dragX, dragY;
	private boolean extended, dragging;
	public ArrayList<Button> buttons;
	public Color color = Color.decode(ColorUtils.pingle);
	
	public Frame(String title, int x, int y, int width, int height, Button...b) {
		this.buttons = Lists.newArrayList();
		this.title = title;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		for (Button b1 : b) {
			this.buttons.add(b1);
		}
	}
	
	float animTicks = 0;
	int length = 0;
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//		RenderUtils.startScissor(this.x, this.y, this.width, (int)animTicks);
		this.color = category != null && !ModuleManager.INSTANCE.getModule(ClickGUIModule.class).customColor.is("Custom") ? category.color : ModuleManager.INSTANCE.getModule(ClickGUIModule.class).color.getColor();
		Screen.fill(matrices, x, y, x + width, y + height, color.getRGB());
		Screen.fill(matrices, x + 1, y + 1, x + width - 1, y + height - (this.extended ? 0 : 0), new Color(25, 25, 25).getRGB());
		FontManager.roboto.drawWithShadow(matrices, title, x + (height / 3), y + (height / 6), -1);
		FontManager.roboto.drawWithShadow(matrices, extended ? "-" : "+", x + width - (height / 1.5f), y + (height / 6), -1);
		if (this.extended) {
			buttons.sort(Comparator.comparingInt(b -> (int)FontManager.roboto.getWidth(((Button)b).getText())).reversed());
			for (Button button : buttons) {
				button.setWidth(this.width);
				button.render(matrices, mouseX, mouseY, delta);
				length = buttons.size() * height;
				if (this.animTicks > length + height + 1) animTicks--;
				if (this.extended) if (animTicks < length + height + 1) {
					animTicks++;
				}
			}
			
		} if (!this.extended) {

			length = height;
			RenderUtils.drawBorderRect(matrices, this.x + 1, this.y, this.x + this.width - 1, this.y + this.height, color.getRGB(), 1);
		}
		if (this.extended) {
//			if (animTicks <= 201) animTicks+=5;
		} else {
			
		}
//		System.out.println(animTicks + this.title);
		RenderUtils.endScissor();
//		Screen.fill(matrices, this.x, this.y, this.x + this.width, this.y + 100, ColorUtils.transparent(-1, 50));
		
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (hovered(mouseX, mouseY)) {
			if (button == 0) {
				setDragging(true);
				setDragX((int) (mouseX - getX()));
				setDragY((int) (mouseY - getY()));
			}
			if (button == 1) {
				this.extended = !this.extended;
			}
		}
	}
	
	public void buttonClicked(Button button) {
	}
	
	public boolean hovered(double mouseX, double mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
	
	public void updateButtons() {
		int offset = (int) (height * 1);
		for (Button button : buttons) {
			button.setY(this.y + offset);
			button.setX(this.getX());
			offset+=height;
		}
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if (isDragging()) {
			setX(mouseX - dragX);
			setY(mouseY - dragY);
		}
	}
	
	public void mouseReleased(int button) {
		if (button == 0) setDragging(false);
//		for (Button funnyButton : buttons) {
//			funnyButton.mouseReleased(button);
//		}
	}
	
	public boolean isDragging() {
		return dragging;
	}
	
	public void setDragging(boolean dragging) {
		if (dragging)
		SaveLoad.INSTANCE.save();
		this.dragging = dragging;
	}
	
	public int getDragX() {
		return dragX;
	}
	
	public int getDragY() {
		return dragY;
	}
	
	public void setDragX(int dragX) {
		this.dragX = dragX;
	}
	
	public void setDragY(int dragY) {
		this.dragY = dragY;
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

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public boolean isExtended() {
		return extended;
	}

	public void setExtended(boolean extended) {
		this.extended = extended;
	}
}

