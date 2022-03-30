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

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventRenderGUI;
import dev.hypnotic.module.hud.HudManager;
import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Timer;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.font.NahrFont;
import dev.hypnotic.utils.math.MathUtils;
import dev.hypnotic.utils.math.TPSUtils;
import dev.hypnotic.utils.render.RenderUtils;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class HUD {
	
	public static HUD INSTANCE = new HUD();
	private MinecraftClient mc = MinecraftClient.getInstance();
	public static Timer animationTimer = new Timer();
	NahrFont fr = FontManager.robotoMed;
	
	public HUD() {
	}
	
	@EventTarget
	public void renderHUD(EventRenderGUI event) {
		if (mc.options.debugEnabled) return;
		
//		RenderUtils.preStencil();
		
//		GL11.glDepthFunc(GL11.GL_LESS);
//		GL11.glEnable(GL11.GL_DEPTH_TEST);
//		GL11.glDepthMask(true);
		
//		RenderUtils.drawFilledCircle(event.getMatrices(), 100, 100, 50, 100, Color.WHITE);
		
//		GL11.glColorMask(true, true, true, true);
//		GL11.glDepthMask(true);
//		GL11.glDepthFunc(GL11.GL_EQUAL);
		
//		RenderUtils.postStencil();
		
//		RenderUtils.fill(event.getMatrices(), 0, 0, 1000, 1000, Color.WHITE);
//		RenderUtils.blur = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/spider.json"));
		RenderUtils.blur.render(event.getTickDelta());
		
//		GL11.glDisable(GL11.GL_DEPTH_TEST);
		
//		RenderUtils.disableStencil();
		
		if (TPSUtils.INSTANCE.getTimeSinceLastTick() >= 1) {
			String numColor = ColorUtils.green;
			if (TPSUtils.INSTANCE.getTimeSinceLastTick() >= 5 && TPSUtils.INSTANCE.getTimeSinceLastTick() < 10 ) numColor = ColorUtils.yellow;
			else if (TPSUtils.INSTANCE.getTimeSinceLastTick() >= 10) numColor = ColorUtils.red;
			fr.drawCenteredString(event.getMatrices(), "Server lagging for " + numColor + MathUtils.round(TPSUtils.INSTANCE.getTimeSinceLastTick(), 1) + " seconds", mc.getWindow().getScaledWidth() / 2, 50, -1, true);
		}
		for (HudModule element : HudManager.INSTANCE.hudModules) {
			if (element.isEnabled() && !(mc.currentScreen instanceof HudEditorScreen))
			element.render(event.getMatrices(), mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), event.getTickDelta());
		}
	}
}
