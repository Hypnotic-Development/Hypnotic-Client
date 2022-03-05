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
import dev.hypnotic.command.argtypes.ModuleArgumentType;
import dev.hypnotic.module.Mod;
import dev.hypnotic.utils.ColorUtils;
import net.minecraft.command.CommandSource;

public class Toggle extends Command {

	public Toggle() {
		super("toggle", "Toggles a specified module", "t", "toggle");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("message", ModuleArgumentType.module()).executes(context -> {
			Mod mod = context.getArgument("message", Mod.class);
			mod.toggle();
			info("Toggled " + ColorUtils.gray + mod.getName() + (mod.isEnabled() ? ColorUtils.green + " on" : ColorUtils.red + " off"));
			return SINGLE_SUCCESS;
		})); 
	}
	
}
