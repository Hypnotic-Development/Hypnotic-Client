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

import static dev.hypnotic.utils.MCUtils.mc;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.math.MathUtils;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

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
			y += RenderUtils.distanceTo(y, offset) / 10;
		}
		
		if (percent >= 1.0f) {
			x += RenderUtils.distanceTo(x, -5) / 8;
			if (x <= 0) hasLeft = true;
		} else if (percent <= 1.0f && Math.round(x) < width) {
			x += RenderUtils.distanceTo(x, width) / 8;
		}
		
		RenderUtils.fill(matrices, sw - (x + width) + width,  sh - (y + height), sw - (x + 5) + width, sh - (y), ColorUtils.transparent(200));
		int barColor = switch (type) {
			case INFO -> -1;
			case WARNING -> Color.YELLOW.getRGB();
			case ERROR -> Color.RED.getRGB();
		};
		float barWidth = width * percent;
		RenderUtils.fill(matrices, sw - (x + width) + width,  sh - (y + 2), sw - (x + barWidth) + width, sh - (y), barColor);
		FontManager.robotoBig.drawWithShadow(matrices, title, sw - (x + width - 50) + width, sh - (y + height - 7.5f), -1);
		FontManager.roboto.drawWithShadow(matrices, description + " (" + MathUtils.round((lifespan - elapsedTime) / 1000f, 1) + "s)", sw - (x + width - 51) + width, sh - (y + height - 25), -1);
		
		RenderUtils.bindTexture(new Identifier("hypnotic", "textures/" + type.toString().toLowerCase() + ".png"));
		RenderSystem.enableBlend();
		int size = 30;
		RenderUtils.drawTexture(matrices, sw - (x - 10), sh - (y + (height / 2 + size / 2) + 2.5f), 0, 0, size, size, size, size);
		RenderSystem.disableBlend();
	}
	
	public enum Type {
		INFO,
		WARNING,
		ERROR
	}
}
