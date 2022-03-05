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
package dev.hypnotic.scripting;

import org.graalvm.polyglot.Value;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import dev.hypnotic.command.Command;
import net.minecraft.command.CommandSource;

/**
* @author BadGamesInc
*/
public class ScriptCommand extends Command {

	public RequiredArgumentBuilder<CommandSource, ?>[] args;
	public Value commandFunc;
	
	public ScriptCommand(String name, String description, Value commandFunc, String[] aliases) {
		super(name, description, aliases);
		this.commandFunc = commandFunc;
	}
	
	private RequiredArgumentBuilder<CommandSource, ?> lastArg;

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		if (args != null && args.length > 0) {
			for (RequiredArgumentBuilder<CommandSource, ?> arg : args) {
				lastArg = arg;
				lastArg.then(arg);
			}
		}
		
		builder.then(lastArg.executes(context -> {
			commandFunc.execute(context);
			return SINGLE_SUCCESS;
		}));
	}

}
