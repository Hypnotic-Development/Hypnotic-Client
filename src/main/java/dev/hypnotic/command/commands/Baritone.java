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

import baritone.api.BaritoneAPI;
import dev.hypnotic.command.Command;
import net.minecraft.command.CommandSource;

public class Baritone extends Command {

	public Baritone() {
		super("baritone", "Run the funy bot", new String[] {"baritone", "b"});
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("command", StringArgumentType.greedyString())
                .executes(context -> {
                    String command = context.getArgument("command", String.class);
                    BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute(command);
                    return SINGLE_SUCCESS;
                }));
	}

}
