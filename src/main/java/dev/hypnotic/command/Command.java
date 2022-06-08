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
package dev.hypnotic.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.utils.ChatUtils;
import dev.hypnotic.utils.ColorUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.registry.DynamicRegistryManager;

public abstract class Command {
    protected static MinecraftClient mc;

    private final String name;
    private final String description;
    private final List<String> aliases = new ArrayList<>();
    public int SINGLE_SUCCESS = com.mojang.brigadier.Command.SINGLE_SUCCESS;
    protected CommandRegistryAccess registryAccess = new CommandRegistryAccess(DynamicRegistryManager.BUILTIN.get());

    public Command(String name, String description, String... aliases) {
        this.name = name;
        this.description = description;
        Collections.addAll(this.aliases, aliases);
        mc = MinecraftClient.getInstance();
    }

    protected static <T> RequiredArgumentBuilder<CommandSource, T> argument(final String name, final ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    protected static LiteralArgumentBuilder<CommandSource> literal(final String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    public final void registerTo(CommandDispatcher<CommandSource> dispatcher) {
        register(dispatcher, name);
        for (String alias : aliases) register(dispatcher, alias);
    }

    public void register(CommandDispatcher<CommandSource> dispatcher, String name) {
        LiteralArgumentBuilder<CommandSource> builder = LiteralArgumentBuilder.literal(name);
        build(builder);
        dispatcher.register(builder);
    }

    public abstract void build(LiteralArgumentBuilder<CommandSource> builder);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String toString() {
        return CommandManager.INSTANCE.getPrefix() + name;
    }

    public String toString(String... args) {
        StringBuilder base = new StringBuilder(toString());
        for (String arg : args)
            base.append(' ').append(arg);

        return base.toString();
    }

    public void info(Text message) {
        ChatUtils.sendMsg(Hypnotic.chatPrefix, message);
    }

    public void info(String message) {
        ChatUtils.tellPlayer(message.replace("(highlight)", ColorUtils.white).replace("(default)", ColorUtils.gray));
    }

    public void warning(String message, Object... args) {
        ChatUtils.warning(Hypnotic.chatPrefix, message, args);
    }

    public void error(String message, Object... args) {
        ChatUtils.error(Hypnotic.chatPrefix, message, args);
    }
}
