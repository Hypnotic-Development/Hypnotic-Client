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
package dev.hypnotic.module.misc;

import java.util.Arrays;
import java.util.UUID;

import org.pircbotx.hooks.types.GenericMessageEvent;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.utils.ChatUtils;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.IRCClient;
import dev.hypnotic.utils.Utils;

/**
* @author BadGamesInc
*/
public class IRC extends Mod {

	public IRC() {
		super("IRC", "Allows you to communicate with other people online using the client", Category.MISC);
	}
	
	@Override
	public void onEnable() {
		try {
			IRCClient.INSTNACE.init("");
		} catch (Exception e) {
			Hypnotic.LOGGER.error("Couldn't initialize the IRC connection");
			e.printStackTrace();
			this.toggle();
			
			return;
		}
		super.onEnable();
	}

	public void onRecieveMessage(GenericMessageEvent event) {
		
		final String ircTag = ColorUtils.darkRed + "\u00A7l[" + ColorUtils.red + "IRC" + ColorUtils.darkRed + "\u00A7l]" + ColorUtils.reset;
		String devTag = ColorUtils.black + "\u00A7l[" + ColorUtils.darkRed + "DEV" + ColorUtils.black + "\u00A7l]" + ColorUtils.reset;
		
		String username = ircTag + " " + event.getUser().getNick();
		
		if (mc.getSession().getUuid() != "") {
			UUID uuid = Utils.getUUIDFromName(event.getUser().getNick().replace("[", "").replace("]", ""));
			
			if (uuid != null) {
				String uuidString = uuid.toString();
				if (Arrays.asList(Hypnotic.INSTANCE.devUUIDs).contains(uuidString)) {
					if (uuidString.equalsIgnoreCase(Hypnotic.INSTANCE.devUUIDs[0])) devTag += " " + ColorUtils.black + "\u00A7l[" + ColorUtils.darkRed + "Big Man" + ColorUtils.black + "\u00A7l]" + ColorUtils.reset;
					System.out.println("id");
					username = ircTag + " " + devTag + " " + event.getUser().getNick();
				}
			}
		}
		
		ChatUtils.tellPlayerRaw(username + ": " + event.getMessage());
	}
	
	@Override
	public void onDisable() {
		if (IRCClient.INSTNACE.output != null) IRCClient.INSTNACE.output.quitServer();
		super.onDisable();
	}
}
