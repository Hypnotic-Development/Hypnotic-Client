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
*/
package dev.hypnotic.ui;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.command.CommandManager;
import dev.hypnotic.scripting.ScriptManager;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Timer;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.math.MathUtils;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

/**
* @author BadGamesInc
*/
public class LoadingScreen extends HypnoticScreen {

	public static LoadingScreen INSTANCE = new LoadingScreen();
	
	public int progress = 0;
	private double smoothProgress = 0;
	public String status = "Loading...";
	private boolean finishedLoading = false;
	public boolean isLoading = false;
	private Timer timer = new Timer();
	
	
	private LoadingScreen() {
		
	}
	
	@Override
	protected void init() {
		if (finishedLoading && waitTicks >= 285) {
			mc.setScreen(new HypnoticMainMenu(true));
		}
		start = System.currentTimeMillis();
		timer.reset();
		super.init();
	}
	
	private int fadeOut = 255;
	private int fadeOut2 = 255;
	private int fadeIn = 20;
	private int waitTicks = 0;
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		DrawableHelper.fillGradient(matrices, 0, 0, width, height, Color.BLACK.getRGB(), ColorUtils.defaultClientColor, 0);
		if (timer.hasTimeElapsed(3000, false) && !finishedLoading) {
			this.load();
			isLoading = true;
		}
		RenderSystem.enableBlend();
		RenderSystem.setShaderTexture(0, new Identifier("hypnotic", "textures/mainmenu/hypnotic.png"));
		float factor = 779 / 2;
        RenderUtils.drawTexture(matrices, (float) (width / 2) - factor / 2, (float) (height / 2) - 400 / 4, 779 / 2, 400 / 2, 0, 0, 779 / 2, 400, 779 / 2, 400);
        // TODO: fade progress bar
		RenderUtils.fill(matrices, (width / 2) - 150, (height / 2) + 60, (width / 2) + 150, (height / 2) + 80, new Color(255, 255, 255, fadeOut2).getRGB());
		
		if (smoothProgress < progress) {
			smoothProgress += RenderUtils.distanceTo(smoothProgress, progress) / 15;
		}
		double percent = smoothProgress / 100.0;
		float length = ((width / 2) + 150) - ((width / 2) - 150);
		RenderUtils.fill(matrices, (width / 2) - 150, (height / 2) + 60, ((width / 2) - 150) + (length * percent), (height / 2) + 80, new Color(ColorUtils.defaultClientColor().getRed(), ColorUtils.defaultClientColor().getGreen(), ColorUtils.defaultClientColor().getBlue(), fadeOut2).getRGB());
		if (fadeOut > 20) FontManager.robotoBig.drawCenteredString(matrices, status, width / 2, (height / 2) + 35, new Color(255, 255, 255, fadeOut).getRGB());
		else if (fadeIn > 20) {
			FontManager.robotoBig.drawCenteredString(matrices, "Welcome back, " + mc.getSession().getUsername(), width / 2, (height / 2) + 35, new Color(255, 255, 255, fadeIn).getRGB());
			if (fadeOut2 > 0) {
				fadeOut2 -= 5;
			}
			if (waitTicks < 285) {
				waitTicks += 5;
			}
		}
		if (waitTicks >= 285) mc.setScreen(new HypnoticMainMenu(true));
		if (finishedLoading) {
			if (fadeOut > 15) {
				fadeOut -= 5;
			}
			if (fadeIn < 255 && fadeOut < 20) {
				fadeIn += 5;
			}
		}
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	long start;
	// Loads all of the stuff in the client
	public void load() {
		switch(progress) {
			case 0:
				progress += 25;
				break;
			case 25:
				status = "Loading scritps...";
				ScriptManager.INSTANCE.loadScripts();
				System.out.println(System.currentTimeMillis() - start);
				progress += 25;
				break;
			case 50:
				status = "Loading commands...";
				CommandManager.INSTANCE.loadCommands();
				System.out.println(System.currentTimeMillis() - start);
				progress += 25;
				break;
			case 75:
				status = "Loading config files...";
				Hypnotic.INSTANCE.loadFiles();
				System.out.println(System.currentTimeMillis() - start);
				progress += 25;
				break;
			case 100:
				status = "Finished loading in " + MathUtils.round(((System.currentTimeMillis() - start) / 1000.0), 2) + "s!";
				finishedLoading = true;
				break;
		}
	}
}
