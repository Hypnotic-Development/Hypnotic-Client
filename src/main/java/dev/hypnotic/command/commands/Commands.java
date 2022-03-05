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

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
import dev.hypnotic.command.CommandManager;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.ChatUtils;
import net.minecraft.command.CommandSource;

public class Commands extends Command {

	public Commands() {
		super("commands", "Gives you a list of all of the commands");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.executes(context -> {
			for (Command cmd : CommandManager.INSTANCE.getCommands()) {
				ChatUtils.tellPlayerRaw(ColorUtils.red + "Command" + ColorUtils.gray + ": " + cmd.getName());
				ChatUtils.tellPlayerRaw(ColorUtils.gray + cmd.getDescription());
			}
			return SINGLE_SUCCESS;
		});
	}
}
