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

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventRenderGUI;
import dev.hypnotic.module.hud.HudManager;
import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.utils.Timer;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.font.NahrFont;
import ladysnake.satin.api.managed.ManagedShaderEffect;
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
	
	ManagedShaderEffect test = ShaderEffectManager.getInstance().manage(new Identifier("hypnotic", "shaders/post/test.json"));

	@EventTarget
	public void renderHUD(EventRenderGUI event) {
		if (mc.options.debugEnabled) return;
		
		for (HudModule element : HudManager.INSTANCE.hudModules) {
			if (element.isEnabled() && !(mc.currentScreen instanceof HudEditorScreen))
			element.render(event.getMatrices(), mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), event.getTickDelta());
		}
	}
}
