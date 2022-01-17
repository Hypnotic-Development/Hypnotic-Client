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

import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import net.minecraft.command.CommandSource;
import net.minecraft.text.LiteralText;

public class ModuleArgumentType implements ArgumentType<Mod> {
    
	private static final Collection<String> EXAMPLES = ModuleManager.INSTANCE.modules
            .stream()
            .limit(3)
            .map(module -> module.name)
            .collect(Collectors.toList());

    private static final DynamicCommandExceptionType NO_SUCH_MODULE = new DynamicCommandExceptionType(o ->
            new LiteralText("Module with name " + o + " doesn't exist."));

    public static ModuleArgumentType module() {
        return new ModuleArgumentType();
    }

    public static Mod getModule(final CommandContext<?> context, final String name) {
        return context.getArgument(name, Mod.class);
    }

    @Override
    public Mod parse(StringReader reader) throws CommandSyntaxException {
        String argument = reader.readString();
        Mod module = ModuleManager.INSTANCE.getModuleByName(argument);

        if (module == null) throw NO_SUCH_MODULE.create(argument);

        return module;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(ModuleManager.INSTANCE.modules.stream().map(module -> module.name), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
