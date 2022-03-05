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

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;

import dev.hypnotic.command.argtypes.ModuleArgumentType;
import dev.hypnotic.command.argtypes.PlayerArgumentType;

/**
* @author BadGamesInc
*/
public class ScriptArgumentTypes {

	public StringArgumentType string() {
		return StringArgumentType.greedyString();
	}
	
	public DoubleArgumentType doubleArg() {
		return DoubleArgumentType.doubleArg();
	}
	
	public ModuleArgumentType module() {
		return ModuleArgumentType.module();
	}
	
	public PlayerArgumentType player() {
		return PlayerArgumentType.player();
	}
}
