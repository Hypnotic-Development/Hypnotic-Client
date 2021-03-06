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
package dev.hypnotic.module.hud;

import java.awt.Color;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.ui.HudEditorScreen;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.font.NahrFont;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class HudModule extends Mod {

	private int defaultX, defaultY, dragX, dragY;
	@Expose
    @SerializedName("x")
	public int x;
	@Expose
    @SerializedName("y")
	public int y;
	private float width, height;
	private double scaleX, scaleY, scaleStartX, scaleStartY, startWidth, startHeight, prevScaleX, prevScaleY;
	private boolean dragging, scaling, draggable;
	private BooleanSetting dragSetting = new BooleanSetting("Locked", false);
	protected NahrFont font = FontManager.robotoMed; 
	
	public HudModule(String name, String description, int defaultX, int defaultY, int width, int height) {
		super(name, description, null);
		this.defaultX = defaultX;
		this.defaultY = defaultY;
		this.width = width;
		this.height = height;
		this.scaleX = 1;
		this.scaleY = 1;
		this.draggable = true;
		this.addSetting(dragSetting);
	}

	public int getDefaultX() {
		return defaultX;
	}

	public void setDefaultX(int defaultX) {
		this.defaultX = defaultX;
	}

	public int getDefaultY() {
		return defaultY;
	}

	public void setDefaultY(int defaultY) {
		this.defaultY = defaultY;
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

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	public double getPrevScaleX() {
		return prevScaleX;
	}
	
	public double getPrevScaleY() {
		return prevScaleY;
	}
	
	public void setPrevScaleX(double d) {
		this.prevScaleX = d;
	}
	
	public void setPrevScaleY(double d) {
		this.prevScaleY = d;
	}
	
	public boolean hovered(int mouseX, int mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}
	
	public boolean hovered(int mouseX, int mouseY, float f, float g, float h, float i) {
		return mouseX >= f && mouseX <= h && mouseY >= g && mouseY <= i;
	}
	
	public boolean isDragging() {
		return dragging;
	}
	
	public void setDragging(boolean dragging) {
		if (this.isDraggable()) this.dragging = dragging;
		else this.dragging = false;
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
	
	public double getScaleX() {
		return scaleX;
	}
	
	public double getScaleY() {
		return scaleY;
	}
	
	public void setScaleX(double scaleX) {
		this.scaleX = scaleX;
	}
	
	public void setScaleY(double scaleY) {
		this.scaleY = scaleY;
	}
	
	public double getScaleStartX() {
		return scaleStartX;
	}
	
	public double getScaleStartY() {
		return scaleStartY;
	}
	
	public void setScaleStartX(double d) {
		this.scaleStartX = d;
	}
	
	public void setScaleStartY(double d) {
		this.scaleStartY = d;
	}
	
	public double getStartWidth() {
		return startWidth;
	}
	
	public double getStartHeight() {
		return startHeight;
	}
	
	public void setStartWidth(float startWidth) {
		this.startWidth = startWidth;
	}
	
	public void setStartHeight(float startHeight) {
		this.startHeight = startHeight;
	}
	
	public boolean isScaling() {
		return scaling;
	}
	
	public boolean isDraggable() {
		return draggable;
	}
	
	public void setDraggable(boolean draggable) {
		this.draggable = draggable;
	}
	
	public void setScaling(boolean scaling, int mouseX, int mouseY) {
		this.setScaleStartX(mouseX - this.getPrevScaleX());
		this.setScaleStartY(mouseY + this.getPrevScaleY());
		this.setStartWidth(this.getWidth());
		this.setStartHeight(this.getHeight());
		this.scaling = scaling;
	}
	
	public void setScaling(boolean scaling) {
		this.setPrevScaleX(this.getScaleX());
		this.setPrevScaleY(this.getScaleY());
		this.scaling = scaling;
	}
	
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		font = FontManager.robotoMed2;
		this.setDraggable(!dragSetting.isEnabled());
		if (mc.currentScreen instanceof HudEditorScreen) {
			if (this.isDraggable()) RenderUtils.fillAndBorder(matrices, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), this.isEnabled() ? -1 : new Color(255, 255, 255, 20).getRGB(), 0, -1);
		}
	}
	
	public void updatePosition(int mouseX, int mouseY) {
		if (isDragging()) {
			setX((int)MathHelper.clamp(mouseX - dragX, 0, mc.getWindow().getScaledWidth() - width));
			setY((int)MathHelper.clamp(mouseY - dragY, 0, mc.getWindow().getScaledHeight() - height));
		}
		if (isScaling()) {
			if (1 - (getScaleStartX() - mouseX) * 0.1 > -1000000) {
				double scaleFactorX = 1 - (this.scaleStartX - mouseX) * 0.02;
				double scaleFactorY = 1 - (this.scaleStartY - mouseY) * 0.02;
				setScaleX(scaleFactorX);
				setScaleY(scaleFactorY);
				this.setWidth((int) (this.startWidth * scaleFactorX));
				this.setHeight((int) (this.startHeight * scaleFactorY));
			}
		}
	}
}
