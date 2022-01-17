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

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
import dev.hypnotic.utils.player.PlayerUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;

public class Teleport extends Command {

	public Teleport() {
		super("teleport", "Teleports you to your desired location", "tp");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("x", IntegerArgumentType.integer()).then(argument("y", IntegerArgumentType.integer()).then(argument("z", IntegerArgumentType.integer()).executes(context -> {
			int x = context.getArgument("x", Integer.class);
			int y = context.getArgument("y", Integer.class);
			int z = context.getArgument("z", Integer.class);
			PlayerUtils.teleport(new BlockPos(x, y, z));
			info("Teleported you to " + x + " " + y + " " + z);
			return SINGLE_SUCCESS;
		}))));
	}
}
