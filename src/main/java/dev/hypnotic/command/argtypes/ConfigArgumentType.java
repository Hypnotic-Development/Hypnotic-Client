/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or Configify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*/
package dev.hypnotic.command.argtypes;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import dev.hypnotic.config.Config;
import dev.hypnotic.config.ConfigManager;
import net.minecraft.command.CommandSource;
import net.minecraft.text.LiteralText;

/**
* @author BadGamesInc
*/
public class ConfigArgumentType implements ArgumentType<Config> {

	private static final Collection<String> EXAMPLES = ConfigManager.INSTANCE.getConfigs()
            .stream()
            .limit(3)
            .map(config -> config.getName())
            .collect(Collectors.toList());

    private static final DynamicCommandExceptionType NO_SUCH_CONFIG = new DynamicCommandExceptionType(o ->
            new LiteralText("Config with name " + o + " doesn't exist."));

    public static ConfigArgumentType config() {
        return new ConfigArgumentType();
    }

    public static Config getConfig(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Config.class);
    }

    @Override
    public Config parse(StringReader reader) throws CommandSyntaxException {
        String argument = reader.readString();
        Config config = ConfigManager.getConfigByName(argument);

        if (config == null) throw NO_SUCH_CONFIG.create(argument);

        return config;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(ConfigManager.INSTANCE.getConfigs().stream().map(config -> config.getName()), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

}
