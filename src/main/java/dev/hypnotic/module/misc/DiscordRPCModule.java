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

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import dev.hypnotic.Hypnotic;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;

public class DiscordRPCModule extends Mod {

	private final String ID = "899900778519601223";
	public DiscordRPC discordRPC;
	public DiscordRichPresence presence;
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
		discordRPC = DiscordRPC.INSTANCE;
		DiscordEventHandlers handlers = new DiscordEventHandlers();
		handlers.ready = (user) -> Hypnotic.LOGGER.info("Discord handlers ready");
		discordRPC.Discord_Initialize(ID, handlers, true, "");
		presence = new DiscordRichPresence();
		presence.startTimestamp = System.currentTimeMillis() / 1000;
		presence.largeImageKey = "hypnotic_icon";
        presence.largeImageText = Hypnotic.fullName;
        presence.details = Hypnotic.fullName;
		presence.state = status;
		discordRPC.Discord_UpdatePresence(presence);
		
		updateThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				discordRPC.Discord_RunCallbacks();
				presence.details = Hypnotic.fullName;
				presence.state = status;
				discordRPC.Discord_UpdatePresence(presence);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					discordRPC.Discord_Shutdown();
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
			discordRPC = DiscordRPC.INSTANCE;
			DiscordEventHandlers handlers = new DiscordEventHandlers();
			handlers.ready = (user) -> Hypnotic.LOGGER.info("Discord handlers ready");
			discordRPC.Discord_Initialize(ID, handlers, true, "");
			presence = new DiscordRichPresence();
			presence.startTimestamp = System.currentTimeMillis() / 1000;
			presence.largeImageKey = "hypnotic_icon";
	        presence.largeImageText = Hypnotic.fullName;
	        presence.details = Hypnotic.fullName;
			presence.state = status;
			discordRPC.Discord_UpdatePresence(presence);
			
			updateThread = new Thread(() -> {
				while (!updateThread.isInterrupted()) {
					discordRPC.Discord_RunCallbacks();
					presence.details = Hypnotic.fullName;
					presence.state = status;
					discordRPC.Discord_UpdatePresence(presence);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
//						discordRPC.Discord_Shutdown();
						break;
					}
				}
			}, "RPC-Callback-Handler");
			updateThread.start();
		} else {
			try {
				if (updateThread != null && !updateThread.isInterrupted()) {
					updateThread.interrupt();
				}
				if (discordRPC != null) {
//					discordRPC.Discord_Shutdown();
//					discordRPC.Discord_ClearPresence();
					discordRPC = null;
					presence = null;
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		super.setEnabled(enabled);
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		if (updateThread != null && !updateThread.isInterrupted()) {
			updateThread.interrupt();
		}
		if (discordRPC != null) {
			try {
				discordRPC.Discord_Shutdown();
				discordRPC = null;
				presence = null;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		super.onDisable();
	}
}
