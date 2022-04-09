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
package dev.hypnotic.module.misc;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DiscordRPCModule extends Mod {

	private final String ID = "899900778519601223";
	public DiscordRichPresence presence = new DiscordRichPresence();
	public Thread updateThread;
	public static String status = "";
	
	public BooleanSetting serverpriv = new BooleanSetting("Server Privacy", false);
	
	public DiscordRPCModule() {
		super("DiscordRPC", "Displays that you are playing Hypnotic on Discord", Category.MISC);
		addSettings(serverpriv);
		this.setEnabled(true);
	}
	
	@Override
	public void onEnable() {
		DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().build();
		handlers.ready = (user) -> Hypnotic.LOGGER.info("Discord handlers ready");
		DiscordRPC.discordInitialize(ID, handlers, true);
		presence = new DiscordRichPresence();
		presence.startTimestamp = System.currentTimeMillis() / 1000;
		presence.largeImageKey = "hypnotic_icon";
        presence.largeImageText = Hypnotic.fullName;
        presence.details = Hypnotic.fullName;
		presence.state = status;
		DiscordRPC.discordUpdatePresence(presence);
		
		updateThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				DiscordRPC.discordRunCallbacks();
				presence.details = Hypnotic.fullName;
				presence.state = status;
				DiscordRPC.discordUpdatePresence(presence);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					DiscordRPC.discordShutdown();
					break;
				}
			}
		}, "RPC-Callback-Handler");
		updateThread.start();
		super.onEnable();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		if (enabled) {
			onEnable();
		} else {
			
		}
		super.setEnabled(enabled);
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		DiscordRPC.discordShutdown();
		super.onDisable();
	}
}
