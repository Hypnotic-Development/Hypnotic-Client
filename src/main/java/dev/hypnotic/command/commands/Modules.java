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
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Wrapper;
import net.minecraft.command.CommandSource;

public class Modules extends Command {

	public Modules() {
		super("modules", "Gives you a list of all of the modules");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.executes(context -> {
			for (Mod mod : ModuleManager.INSTANCE.modules) {
				Wrapper.tellPlayerRaw(ColorUtils.red + "Module" + ColorUtils.gray + ": " + mod.getName());
				Wrapper.tellPlayerRaw(ColorUtils.gray +  mod.getDescription());
			}
			return SINGLE_SUCCESS;
		});
	}
}