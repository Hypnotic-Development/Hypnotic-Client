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
package dev.hypnotic.utils;

import static dev.hypnotic.utils.MCUtils.mc;

import java.io.IOException;
import java.lang.Thread.State;
import java.util.Arrays;
import java.util.UUID;

import org.pircbotx.Channel;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.pircbotx.output.OutputIRC;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.misc.IRC;

/**
* @author BadGamesInc
*/
public class IRCClient extends ListenerAdapter {

	public static final IRCClient INSTNACE = new IRCClient();

	private Thread ircThread;
	
	public PircBotX bot;
	public OutputIRC output;
	public Channel channel;
	
	public String username, mcName, displayName;
	
	public IRC ircMod;
	
	@Override
	public void onGenericMessage(GenericMessageEvent event) throws Exception {
		if (ircMod.isEnabled()) ircMod.onRecieveMessage(event);
	}
	
	public void init(String username) {
		// Calling this on the main thread kills the game
		ircThread = new Thread(() -> {
			ircMod = ModuleManager.INSTANCE.getModule(IRC.class);
			
			if (bot != null && output != null) {
				if (bot.isConnected()) output.quitServer();
				bot = null;
				output = null;
			}
			
			if (mc.getSession().getUuid() == "") {
				if (mc.inGameHud != null && mc.inGameHud.getChatHud() != null) ChatUtils.tellPlayer("You must be using a non-cracked account to use the IRC");
				else Hypnotic.LOGGER.error("You must be using a non-cracked account to use the IRC");
				
				ircMod.setEnabled(false);
				return;
			}
			
			mcName = mc.getSession().getUsername();
			this.username = username + "[" + mcName + "]";
			
			Configuration config = new Configuration.Builder()
	                .setName(this.username) //Set the nick of the bot. CHANGE IN YOUR CODE
	                .addServer("irc.freenode.net") //Join the freenode network
	                .addAutoJoinChannel("#hypnoticirc") //Join the hypnotic irc channel
	                .addListener(this) //Add our listener that will be called on Events
	                .setNickservNick(this.username)
	                .setAutoNickChange(false)
	                .buildConfiguration();
			
			bot = new PircBotX(config);
			output = new OutputIRC(bot);
			
			try {
				bot.startBot();
			} catch (IOException | IrcException e) {
				e.printStackTrace();
			}
			
			System.out.println("sldkfjsdlfj");
		});
		ircThread.setName("IRC Thread");
		
		if (ircThread.getState() == State.NEW) ircThread.start();
		else System.out.println("dflksf");
	}
	
	public void setUsername(String username) {
		try {
			this.username = username;
			init(username);
		} catch (Exception e) {
			Hypnotic.LOGGER.error("Failed to initialize irc bot");
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message) {
		if (!bot.isConnected()) return;
		
		final String ircTag = ColorUtils.darkRed + "\u00A7l[" + ColorUtils.red + "IRC" + ColorUtils.darkRed + "\u00A7l]" + ColorUtils.reset;
		String devTag = ColorUtils.black + "\u00A7l[" + ColorUtils.darkRed + "DEV" + ColorUtils.black + "\u00A7l]" + ColorUtils.reset;
		
		String username = ircTag + " " + bot.getNick();
		
		if (mc.getSession().getUuid() != "") {
			UUID uuid = Utils.getUUIDFromName(bot.getNick().replace("[", "").replace("]", ""));
			
			if (uuid != null) {
				String uuidString = uuid.toString();
				if (Arrays.asList(Hypnotic.INSTANCE.devUUIDs).contains(uuidString)) {
					if (uuidString.equalsIgnoreCase(Hypnotic.INSTANCE.devUUIDs[0])) devTag += " " + ColorUtils.black + "\u00A7l[" + ColorUtils.darkRed + "Big Man" + ColorUtils.black + "\u00A7l]" + ColorUtils.reset;
					System.out.println("id");
					username = ircTag + " " + devTag + " " + bot.getNick();
				}
			}
		}
		
		ChatUtils.tellPlayerRaw(username + ": " + message);
		
		bot.sendIRC().message("#hypnoticirc", message);
	}
}
