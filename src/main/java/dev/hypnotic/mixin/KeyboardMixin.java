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

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.command.CommandManager;
import dev.hypnotic.event.events.EventKeyPress;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.utils.input.KeyUtils;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow @Final private MinecraftClient client;

	@Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        if (key != GLFW.GLFW_KEY_UNKNOWN) {
        	/*
        	 * action == GLFW.GLFW_PRESS is important
        	 * so the module does not toggle twice
        	 */
        	KeyUtils.setKeyState(key, action != GLFW.GLFW_RELEASE);
            for (Mod mod : ModuleManager.INSTANCE.modules) {
                if (mod.getKey() == key && action == GLFW.GLFW_PRESS && MinecraftClient.getInstance().currentScreen == null)
                    mod.toggle();
            }
            
            EventKeyPress event = new EventKeyPress(key, scancode, action);
            event.call();
            if (event.isCancelled()) info.cancel();
            if (client.currentScreen == null && key == KeyUtils.getKey(CommandManager.INSTANCE.getPrefix()) && action == GLFW.GLFW_PRESS) client.setScreen(new ChatScreen(""));
        }
    }
}
