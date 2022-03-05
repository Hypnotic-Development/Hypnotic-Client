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

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.ui.altmanager.altmanager2.AltManagerScreen;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Utils;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class HypnoticMainMenu extends HypnoticScreen {

	float singleTicks = 0,
    		multiTicks = 0,
    		altTicks = 0,
    		optTicks = 0,
    		exitTicks = 0;
	
	private boolean justLoaded = false, 
			hasEntered = true;
	
	public HypnoticMainMenu(boolean justLoaded) {
		this.justLoaded = justLoaded;
		this.hasEntered = !justLoaded;
	}
	
	float linkTicks = 0;
    public int fadeOut = 255,
    		fadeIn = 0;
    public float upTicks = 0;
	
	private void drawMenuButtons(MatrixStack matrices, int mouseX, int mouseY) {
    	String menuDir = "textures/mainmenu/";
    	RenderSystem.setShaderColor(1f, 1f, 1f, fadeIn / 255f);
    	int size = 50;
    	int x = this.width / 2 - 160, y = this.height / 2 + 30;
    	RenderSystem.setShaderTexture(0, new Identifier("hypnotic", menuDir + "singleplayer.png"));
    	if (hoveredSinglePlayer(mouseX, mouseY)) {
    		this.changeImgColor();
    		if (this.singleTicks < 5) {
    			this.singleTicks+=0.5f;
    		}
    	} else {
    		if (this.singleTicks > 0) {
    			this.singleTicks-=0.5f;
    		}
    	}
    	RenderUtils.drawTexture(matrices, x + 0, y - singleTicks, size, size, size, size, size, size);
    	if (hoveredSinglePlayer(mouseX, mouseY))
    		this.resetImgColor();
    	RenderSystem.setShaderTexture(0, new Identifier("hypnotic", menuDir + "multiplayer.png"));
    	if (hoveredMultiplayer(mouseX, mouseY)) {
    		this.changeImgColor();
    		if (this.multiTicks < 5) {
    			this.multiTicks+=0.5f;
    		}
    	} else {
    		if (this.multiTicks > 0) {
    			this.multiTicks-=0.5f;
    		}
    	}
    	RenderUtils.drawTexture(matrices, x + 75, y - multiTicks, size, size, size, size, size, size);
    	if (hoveredMultiplayer(mouseX, mouseY))
    		this.resetImgColor();
    	RenderSystem.setShaderTexture(0, new Identifier("hypnotic", menuDir + "altmanager.png"));
    	if (hoveredAltManager(mouseX, mouseY)) {
    		this.changeImgColor();
    		if (this.altTicks < 5) {
    			this.altTicks+=0.5f;
    		}
    	} else {
    		if (this.altTicks > 0) {
    			this.altTicks-=0.5f;
    		}
    	}
    	RenderUtils.drawTexture(matrices, x + 150, y - altTicks, size, size, size, size, size, size);
    	if (hoveredAltManager(mouseX, mouseY))
    		this.resetImgColor();
    	RenderSystem.setShaderTexture(0, new Identifier("hypnotic", menuDir + "options.png"));
    	if (hoveredOptions(mouseX, mouseY)) {
    		this.changeImgColor();
    		if (this.optTicks < 5) {
    			this.optTicks+=0.5f;
    		}
    	} else {
    		if (this.optTicks > 0) {
    			this.optTicks-=0.5f;
    		}
    	}
    	RenderUtils.drawTexture(matrices, x + 225, y - optTicks, size, size, size, size, size, size);
    	if (hoveredOptions(mouseX, mouseY))
    		this.resetImgColor();
    	RenderSystem.setShaderTexture(0, new Identifier("hypnotic", menuDir + "exit.png"));
    	if (hoveredQuit(mouseX, mouseY)) {
    		this.changeImgColor();
    		if (this.exitTicks < 5) {
    			this.exitTicks+=0.5f;
    		}
    	} else {
    		if (this.exitTicks > 0) {
    			this.exitTicks-=0.5f;
    		}
    	}
    	RenderUtils.drawTexture(matrices, x + 300, y - exitTicks, size, size, size, size, size, size);
    	if (hoveredQuit(mouseX, mouseY))
    		this.resetImgColor();
    	RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
	
	private void changeImgColor() {
		RenderSystem.setShaderColor(ColorUtils.defaultClientColor().getRed() / 255f, ColorUtils.defaultClientColor().getGreen() / 255f, ColorUtils.defaultClientColor().getBlue() / 255f, 1f);
	}
	
	private void resetImgColor() {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
	}
    
    public boolean hoveredSinglePlayer(double mouseX, double mouseY) {
    	int x = this.width / 2- 160, y = this.height / 2 + 35, size = 35;
    	return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
    
    public boolean hoveredMultiplayer(double mouseX, double mouseY) {
    	int x = this.width / 2- 160 + 75, y = this.height / 2 + 35, size = 35;
    	return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
    
    public boolean hoveredAltManager(double mouseX, double mouseY) {
    	int x = this.width / 2- 160 + 150, y = this.height / 2 + 35, size = 35;
    	return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
    
    public boolean hoveredOptions(double mouseX, double mouseY) {
    	int x = this.width / 2- 160 + 225, y = this.height / 2 + 35, size = 35;
    	return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
    
    public boolean hoveredQuit(double mouseX, double mouseY) {
    	int x = this.width / 2- 160 + 300, y = this.height / 2 + 35, size = 35;
    	return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
    
    public boolean hoveredRealms(double mouseX, double mouseY) {
    	int x = this.width / 2- 160 + 300, y = this.height / 2 + 35, size = 35;
    	return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		DrawableHelper.fillGradient(matrices, 0, 0, width, height, Color.BLACK.getRGB(), ColorUtils.defaultClientColor, 0);
		RenderSystem.enableBlend();
		
		if (hasEntered) {
			drawMenuButtons(matrices, mouseX, mouseY);
			font.draw(matrices, Hypnotic.fullName, 4, height - 19, -1);
			font.draw(matrices, TitleScreen.COPYRIGHT, width - font.getStringWidth(TitleScreen.COPYRIGHT) - 4, height - 19, -1);
			if (hovered(4, height - 4, (int) font.getStringWidth(Hypnotic.fullName) + 4, height - 14)) {
				if (linkTicks < font.getStringWidth(Hypnotic.fullName)) linkTicks+=2;
			} else {
				if (linkTicks > 0) linkTicks-=2;
			}
			RenderUtils.fill(matrices, 4, height - 5, 4 + linkTicks, height - 6, -1);
		}
		if (hasEntered && fadeOut > 20) {
			fadeOut -= 5;
		} 
		
		if (hasEntered && fadeOut < 25 && fadeIn < 255) {
			fadeIn += 5;
		}
		
		if (hasEntered && upTicks < 25) {
			upTicks += RenderUtils.distanceTo(upTicks, 25) / 15;
		}
		
		if (justLoaded && fadeOut > 20) {
			DrawableHelper.fillGradient(matrices, 0, 0, width, height, hasEntered ? Color.BLACK.getRGB() : new Color(0, 0, 0, fadeOut).getRGB(), hasEntered ? ColorUtils.defaultClientColor : new Color(ColorUtils.defaultClientColor().getRed(), ColorUtils.defaultClientColor().getGreen(), ColorUtils.defaultClientColor().getBlue(), fadeOut).getRGB(), 0);
			FontManager.robotoBig.drawCenteredString(matrices, "Welcome back, " + mc.getSession().getUsername(), width / 2, (height / 2) + 35, new Color(255, 255, 255, fadeOut).getRGB());
			FontManager.robotoMed.drawCenteredString(matrices, "Click or press any key to enter", width / 2, (height / 2) + 55, hasEntered ? new Color(255, 255, 255, fadeOut).getRGB() : ColorUtils.fade(Color.WHITE, 0, 1).getRGB());
		}
		RenderSystem.enableBlend();
		RenderSystem.setShaderTexture(0, new Identifier("hypnotic", "textures/mainmenu/hypnotic.png"));
		float factor = 779 / 2;
        RenderUtils.drawTexture(matrices, (float) (width / 2) - factor / 2, ((float) (height / 2) - 400 / 4) - upTicks, 779 / 2, 400 / 2, 0, 0, 779 / 2, 400, 779 / 2, 400);
        RenderSystem.disableBlend();
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (hasEntered) {
			if (hoveredSinglePlayer(mouseX, mouseY) && button == 0)
	        	mc.setScreen(new SelectWorldScreen(this));
	        if (hoveredMultiplayer(mouseX, mouseY) && button == 0)
	        	mc.setScreen(new MultiplayerScreen(this));
	        if (hoveredAltManager(mouseX, mouseY) && button == 0)
	        	mc.setScreen(AltManagerScreen.INSTANCE);
	        if (hoveredOptions(mouseX, mouseY) && button == 0) 
	        	mc.setScreen(new OptionsScreen(this, mc.options));
	        if (hoveredQuit(mouseX, mouseY) && button == 0)
	        	mc.scheduleStop();
	        if (hovered(4, height - 4, (int) font.getStringWidth(Hypnotic.fullName) + 4, height - 14)) {
	        	Utils.openURL("hypnotic.dev");
	        }
		} else {
			hasEntered = true;
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (!hasEntered) hasEntered = true;
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
}
