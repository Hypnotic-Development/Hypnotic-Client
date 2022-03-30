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

import static dev.hypnotic.utils.MCUtils.mc;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.event.events.EventTick;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.misc.DiscordRPCModule;
import dev.hypnotic.ui.HypnoticMainMenu;
import dev.hypnotic.utils.Timer;
import dev.hypnotic.utils.world.BlockIterator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow @Final private RenderTickCounter renderTickCounter;
	
	@Inject(at = @At("TAIL"), method = "updateWindowTitle()V")
	public void updateWindowTitle(CallbackInfo info) {
		MinecraftClient.getInstance().getWindow().setTitle(Hypnotic.fullName);
	}
	
	Timer refreshTimer = new Timer();
	
	@Inject(at = @At("HEAD"), method = "tick")
    private void onPreTick(CallbackInfo info) {
		BlockIterator.INSTANCE.onTick();
		EventTick event = new EventTick();
		event.call();
		
		if (ModuleManager.INSTANCE.getModule(DiscordRPCModule.class).isEnabled()) {
			if (mc.player == null) {
				if (mc.currentScreen instanceof HypnoticMainMenu) DiscordRPCModule.status = "In main menu";
				if (mc.currentScreen instanceof SelectWorldScreen) DiscordRPCModule.status = "Browsing worlds";
				if (mc.currentScreen instanceof MultiplayerScreen) DiscordRPCModule.status = "Browsing servers";
			} else {
				if (mc.isInSingleplayer()) DiscordRPCModule.status = "Playing singlelplayer";
				if (mc.getCurrentServerEntry() != null && !mc.isInSingleplayer()) DiscordRPCModule.status = ModuleManager.INSTANCE.getModule(DiscordRPCModule.class).serverpriv.isEnabled() ? "Playing multiplayer." : "Playing multiplayer on " + mc.getCurrentServerEntry().address;
			}
		}
		
		if (refreshTimer.hasTimeElapsed(2500, true)) {
			try {
				Hypnotic.refreshUsers();
			} catch (Exception e) {
				// Not printing stack trace because it is just a stupid message if you aren't fully connected
			}
		}
	}
	
	@Inject(at = @At("TAIL"), method = "scheduleStop")
	public void onShutdown(CallbackInfo ci) {
		Hypnotic.INSTANCE.shutdown();
	}
}
