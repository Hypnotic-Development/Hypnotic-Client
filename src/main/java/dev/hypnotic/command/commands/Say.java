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
package dev.hypnotic.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
import net.minecraft.command.CommandSource;

public class Say extends Command {

	public Say() {
		super("say", "Says what you type in chat", "say");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("message", StringArgumentType.greedyString()).executes(context -> {
			String msg = context.getArgument("message", String.class);
			mc.player.sendChatMessage(msg);
			return SINGLE_SUCCESS;
		})); 
		
	}
}
