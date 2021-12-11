package dev.hypnotic.hypnotic_client.ui;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.hypnotic.hypnotic_client.Hypnotic;
import dev.hypnotic.hypnotic_client.ui.altmanager.altmanager2.AltManagerScreen;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.Utils;
import dev.hypnotic.hypnotic_client.utils.render.RenderUtils;
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
	
	private void drawMenuButtons(MatrixStack matrices, int mouseX, int mouseY) {
    	String menuDir = "textures/mainmenu/";
    	int x = this.width / 2 - 160, y = this.height / 2 + 50;
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
    	RenderUtils.drawTexture(matrices, x + 0, y - singleTicks, 35, 35, 35, 35, 35, 35);
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
    	RenderUtils.drawTexture(matrices, x + 75, y - multiTicks, 35, 35, 35, 35, 35, 35);
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
    	RenderUtils.drawTexture(matrices, x + 150, y - altTicks, 35, 35, 35, 35, 35, 35);
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
    	RenderUtils.drawTexture(matrices, x + 225, y - optTicks, 35, 35, 35, 35, 35, 35);
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
    	RenderUtils.drawTexture(matrices, x + 300, y - exitTicks, 35, 35, 35, 35, 35, 35);
    	if (hoveredQuit(mouseX, mouseY))
    		this.resetImgColor();
    }
	
	private void changeImgColor() {
		RenderSystem.setShaderColor(ColorUtils.defaultClientColor().getRed() / 255f, ColorUtils.defaultClientColor().getGreen() / 255f, ColorUtils.defaultClientColor().getBlue() / 255f, 1f);
	}
	
	private void resetImgColor() {
		RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
	}
    
    public boolean hoveredSinglePlayer(double mouseX, double mouseY) {
    	int x = this.width / 2- 160, y = this.height / 2 + 50, size = 35;
    	return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
    
    public boolean hoveredMultiplayer(double mouseX, double mouseY) {
    	int x = this.width / 2- 160 + 75, y = this.height / 2 + 50, size = 35;
    	return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
    
    public boolean hoveredAltManager(double mouseX, double mouseY) {
    	int x = this.width / 2- 160 + 150, y = this.height / 2 + 50, size = 35;
    	return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
    
    public boolean hoveredOptions(double mouseX, double mouseY) {
    	int x = this.width / 2- 160 + 225, y = this.height / 2 + 50, size = 35;
    	return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
    
    public boolean hoveredQuit(double mouseX, double mouseY) {
    	int x = this.width / 2- 160 + 300, y = this.height / 2 + 50, size = 35;
    	return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
    
    public boolean hoveredRealms(double mouseX, double mouseY) {
    	int x = this.width / 2- 160 + 300, y = this.height / 2 + 50, size = 35;
    	return mouseX >= x && mouseX <= x + size && mouseY >= y && mouseY <= y + size;
    }
	
    float linkTicks = 0;
    
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		DrawableHelper.fillGradient(matrices, 0, 0, width, height, Color.BLACK.getRGB(), ColorUtils.defaultClientColor, 0);
//		RenderUtils.fill(matrices, 0, 0, width, height, -1, ColorUtils.defaultClientColor, -0);
		RenderSystem.setShaderTexture(0, new Identifier("hypnotic", "textures/mainmenu/hypnotic.png"));
		float factor = 679 / 2;
        RenderUtils.drawTexture(matrices, (float) (width / 2) - factor / 2, (float) (height / 2) - 300 / 4, 679 / 2, 300 / 2, 0, 0, 679 / 2, 300, 679 / 2, 300);
		
		drawMenuButtons(matrices, mouseX, mouseY);
		font.draw(matrices, Hypnotic.fullName, 4, height - 19, -1);
		font.draw(matrices, TitleScreen.COPYRIGHT, width - font.getStringWidth(TitleScreen.COPYRIGHT) - 4, height - 19, -1);
		if (hovered(4, height - 4, (int) font.getStringWidth(Hypnotic.fullName) + 4, height - 14)) {
			if (linkTicks < font.getStringWidth(Hypnotic.fullName)) linkTicks+=2;
		} else {
			if (linkTicks > 0) linkTicks-=2;
		}
		RenderUtils.fill(matrices, 4, height - 5, 4 + linkTicks, height - 6, -1);
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
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
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
