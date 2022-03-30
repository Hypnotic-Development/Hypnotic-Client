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
package dev.hypnotic.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
import dev.hypnotic.utils.ChatUtils;
import dev.hypnotic.utils.IRCClient;
import net.minecraft.command.CommandSource;

/**
* @author BadGamesInc
*/
public class IRCCommand extends Command {

	public IRCCommand() {
		super("irc", "Allows you to interact with the irc chat");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("message", StringArgumentType.greedyString()).executes(context -> {
			if (IRCClient.INSTNACE.ircMod.isEnabled()) {
				if (!IRCClient.INSTNACE.bot.isConnected()) {
					ChatUtils.tellPlayer("Not connected to the IRC!");
					return SINGLE_SUCCESS;
				}
				String msg = context.getArgument("message", String.class);
	
				IRCClient.INSTNACE.sendMessage(msg);
			} else {
				error("You must have the IRC module enabled in order to use the IRC!");
			}
			return SINGLE_SUCCESS;
		})); 
	}
}
