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

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
import dev.hypnotic.command.argtypes.ConfigArgumentType;
import dev.hypnotic.config.Config;
import dev.hypnotic.config.ConfigManager;
import net.minecraft.command.CommandSource;

/**
* @author BadGamesInc
*/
public class ConfigCmd extends Command {

	public ConfigCmd() {
		super("config", "Loads/saves configs", "cfg");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		
		builder.then(literal("reload").executes(context -> {
			ConfigManager.INSTANCE.loadConfigs();
			info("Reloaded configs");
			return SINGLE_SUCCESS;
		}));
		
		builder.then(literal("load").then(argument("config", ConfigArgumentType.config()).executes(context -> {
			Config config = context.getArgument("config", Config.class);
			ConfigManager.INSTANCE.load(config.getName());
			info("Loaded " + config.getName());
			return SINGLE_SUCCESS;
		})));
		
		builder.then(literal("save-as").then(argument("config", StringArgumentType.string()).executes(context -> {
			String config = StringArgumentType.getString(context, "config");
			ConfigManager.INSTANCE.save(config);
			info("Saved new config as " + config);
			return SINGLE_SUCCESS;
		})));
		
		builder.then(literal("save").then(argument("config", ConfigArgumentType.config()).executes(context -> {
			Config config = context.getArgument("config", Config.class);
			ConfigManager.INSTANCE.save(config.getName());
			info("Saved " + config.getName());
			return SINGLE_SUCCESS;
		})));
		
		builder.then(literal("delete").then(argument("config", ConfigArgumentType.config()).executes(context -> {
			Config config = context.getArgument("config", Config.class);
			ConfigManager.INSTANCE.delete(config.getName());
			info("Deleted " + config.getName());
			return SINGLE_SUCCESS;
		})));
				
		builder.then(literal("list").executes(context -> {
			if (ConfigManager.INSTANCE.getConfigs().size() > 0) {
				info("Configs: " + ConfigManager.INSTANCE.getConfigs().size());
				ConfigManager.INSTANCE.list();
			} else {
				info("No configs, put some configs in your hypnotic/configs folder or use .cfg save <config name> to save a config");
			}
			return SINGLE_SUCCESS;
		}));
	}

}
