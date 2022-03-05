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

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
import dev.hypnotic.scripting.ScriptManager;
import dev.hypnotic.utils.ChatUtils;
import dev.hypnotic.utils.ColorUtils;
import net.minecraft.command.CommandSource;

/**
* @author BadGamesInc
*/
public class Script extends Command {

	public Script() {
		super("script", "Preform various actions with the scripting api");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(literal("refresh").executes(context -> {
			ScriptManager.INSTANCE.refreshScripts();
			info("Refreshed " + ScriptManager.INSTANCE.getScripts().size() + " scripts");
			return SINGLE_SUCCESS;
		}));
				
		builder.then(literal("list").executes(context -> {
			info("Scripts: " + ScriptManager.INSTANCE.getScripts().size());
			ScriptManager.INSTANCE.getScripts().forEach(script -> ChatUtils.tellPlayerRaw(ColorUtils.red + "Script" + ColorUtils.gray + ": " + ColorUtils.white + script.getName() + ColorUtils.gray + " by " + ColorUtils.white + script.getAuthor()));
			return SINGLE_SUCCESS;
		}));
	}
}
