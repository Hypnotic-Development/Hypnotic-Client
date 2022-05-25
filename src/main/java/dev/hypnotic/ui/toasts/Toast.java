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
package dev.hypnotic.ui.toasts;

import java.awt.Color;

import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.math.MathUtils;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

import static dev.hypnotic.utils.MCUtils.mc;

/**
* @author BadGamesInc
*/
public class Toast {

	public String title, description;
	public Type type;
	public long lifespan;
	public boolean hasLeft = false;
	
	private long startTime;
	private float x, y;
	
	public Toast(Type type, String title, String description, long lifespan) {
		this.title = title;
		this.description = description;
		this.type = type;
		this.lifespan = lifespan;
		
		startTime = System.currentTimeMillis();
	}
	
	public void init() {
		y = (ToastManager.INSTANCE.toasts.lastIndexOf(this)) * (50 + 5) + 10;
	}
	
	public void render(MatrixStack matrices, int index) {
		long elapsedTime = System.currentTimeMillis() - startTime;
		float percent = MathUtils.percent(elapsedTime, lifespan);
		
		int width = 205, height = 50, offset = index * (height + 5) + 10, sw = mc.getWindow().getScaledWidth(), sh = mc.getWindow().getScaledHeight();

		if (y > offset) {
			y -= 2.5;
		}
		
		if (percent >= 1.0f) {
			x -= 5;
			if (x <= 0) hasLeft = true;
		} else if (percent <= 1.0f && x < width) {
			x += 5;
		}
		
		RenderUtils.renderRoundedQuad(matrices, new Color(0, 0, 0, 200), sw - (x + width) + width,  sh - (y + height), sw - (x + 5) + width, sh - (y), 5, 10);
		float barWidth = percent < 0.98f ? width * percent : width - 5;
		if (percent < 0.98f) RenderUtils.renderRoundedQuad(matrices, Color.RED, sw - (x + width) + width,  sh - (y + 4), sw - (x + barWidth) + width, sh - (y), 2, 10);
		FontManager.robotoBig.drawWithShadow(matrices, title, sw - (x + width - 10) + width, sh - (y + height - 5), -1);
		FontManager.roboto.drawWithShadow(matrices, description, sw - (x + width - 11) + width, sh - (y + height - 25), -1);
	}
	
	public enum Type {
		INFO,
		WARNING,
		ERROR
	}
}
