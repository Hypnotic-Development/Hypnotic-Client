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

import org.lwjgl.glfw.GLFW;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
import dev.hypnotic.command.argtypes.ModuleArgumentType;
import dev.hypnotic.module.Mod;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Wrapper;
import dev.hypnotic.utils.input.KeyUtils;
import net.minecraft.command.CommandSource;

public class Bind extends Command {

	public Bind() {
		super("bind", "Binds a specified module");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("mod", ModuleArgumentType.module()).then(argument("key", StringArgumentType.string()).executes(context -> {
			Mod mod = context.getArgument("mod", Mod.class);
			String key = context.getArgument("key", String.class);
			mod.setKey(KeyUtils.getKey(key));
			Wrapper.tellPlayer("Bound " + ColorUtils.white + mod.getName() + ColorUtils.gray + " to " + ColorUtils.white + (KeyUtils.getKey(key) != -1 ? GLFW.glfwGetKeyName(KeyUtils.getKey(key), KeyUtils.getKeyScanCode(key)).toUpperCase() : "NONE"));
			return SINGLE_SUCCESS;
		}))); 
	}
	
}
