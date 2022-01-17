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

import net.minecraft.client.Mouse;

import static dev.hypnotic.utils.MCUtils.mc;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.event.events.EventMouseButton;

@Mixin(Mouse.class)
public class MouseMixin {

	@Inject(method = "onMouseButton", at = @At("HEAD"), cancellable = true)
    public void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        if (window == mc.getWindow().getHandle()) {
        	
            boolean bl = action == GLFW.GLFW_PRESS;
            if (bl) {
				EventMouseButton event = new EventMouseButton(button, mc.player == null ? EventMouseButton.ClickType.IN_MENU : EventMouseButton.ClickType.IN_GAME);
                event.call();
            	if (event.isCancelled())
                    ci.cancel();
            }
        }
    }
}
