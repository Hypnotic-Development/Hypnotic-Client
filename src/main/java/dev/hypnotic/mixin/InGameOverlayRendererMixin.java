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
package dev.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.render.NoRender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

	@Inject(method = "renderUnderwaterOverlay", at = @At("HEAD"), cancellable = true)
	private static void renderUnderwaterOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
		if (ModuleManager.INSTANCE.getModule(NoRender.class).isEnabled() && ModuleManager.INSTANCE.getModule(NoRender.class).liquid.isEnabled()) ci.cancel(); 
	}
	
	@Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
	private static void renderFireOverlay(MinecraftClient client, MatrixStack matrices, CallbackInfo ci) {
		if (ModuleManager.INSTANCE.getModule(NoRender.class).isEnabled() && ModuleManager.INSTANCE.getModule(NoRender.class).fire.isEnabled()) ci.cancel(); 
	}
}
