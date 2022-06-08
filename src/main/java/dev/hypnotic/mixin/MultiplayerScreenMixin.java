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

import dev.hypnotic.ui.serverfinder.ServerFinder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {

	protected MultiplayerScreenMixin(Text title) {
		super(title);
	}

	@Inject(method = "init", at = @At("TAIL"), cancellable = true)
	private void addServerFinderButton(CallbackInfo ci) {
		this.addDrawableChild(new ButtonWidget(this.width - 120, this.height - 30, 100, 20, Text.literal("Server Finder"), (button) -> {
			MinecraftClient.getInstance().setScreen(new ServerFinder((MultiplayerScreen)(Object)this));
	    }));
	}
	
}
