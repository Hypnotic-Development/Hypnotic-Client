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
package dev.hypnotic.module.hud.elements;

import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.ui.HudEditorScreen;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.math.MathUtils;
import dev.hypnotic.utils.math.TPSUtils;
import net.minecraft.client.util.math.MatrixStack;

/**
* @author BadGamesInc
*/
public class LagIndicator extends HudModule {

	public LagIndicator() {
		super("Lag Indicator", "Shows when the server is lagging", -100, 50, 0, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		this.setDefaultX(scaledWidth / 2);
		String lagString = "Server lagging for " + MathUtils.round(TPSUtils.INSTANCE.getTimeSinceLastTick(), 1) + " seconds";
		this.setWidth(font.getStringWidth(lagString));
		this.setHeight(font.getStringHeight(lagString));
		if (getX() == -100) setX(getDefaultX());
		if (TPSUtils.INSTANCE.getTimeSinceLastTick() >= 1 || mc.currentScreen instanceof HudEditorScreen) {
			String numColor = ColorUtils.green;
			if (TPSUtils.INSTANCE.getTimeSinceLastTick() >= 5 && TPSUtils.INSTANCE.getTimeSinceLastTick() < 10 ) numColor = ColorUtils.yellow;
			else if (TPSUtils.INSTANCE.getTimeSinceLastTick() >= 10) numColor = ColorUtils.red;
			lagString = "Server lagging for " + numColor + MathUtils.round(TPSUtils.INSTANCE.getTimeSinceLastTick(), 1) + " seconds";
			font.drawCenteredString(matrices, lagString, x + (font.getStringWidth(lagString) / 2), y - (font.getStringHeight(lagString) / 4), -1, true);
		}
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
}
