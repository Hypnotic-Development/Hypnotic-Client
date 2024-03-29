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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import dev.hypnotic.command.commands.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;

public class CommandManager {

	public static final CommandManager INSTANCE = new CommandManager();
	private static MinecraftClient mc = MinecraftClient.getInstance();
	private final CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();
    private final CommandSource COMMAND_SOURCE = new ChatCommandSource(mc);
    private final List<Command> commands = new ArrayList<>();
    private final Map<Class<? extends Command>, Command> commandInstances = new HashMap<>();

    private CommandManager() {
    	
    }
    
    public void loadCommands() {
    	add(new VClip());
        add(new FriendCmd());
        add(new NBT());
        add(new Enchant());
        add(new Give());
        add(new Say());
        add(new Baritone());
        add(new Search());
        add(new Explosion());
        add(new About());
        add(new Commands());
        add(new Modules());
        add(new Toggle());
        add(new ModuleInfo());
        add(new Bind());
        add(new Teleport());
        add(new Script());
        add(new ConfigCmd());
        add(new IRCCommand());
        add(new FightBotCommand());
        commands.sort(Comparator.comparing(Command::getName));
    }

    public void dispatch(String message) throws CommandSyntaxException {
        dispatch(message, new ChatCommandSource(mc));
    }

    public void dispatch(String message, CommandSource source) throws CommandSyntaxException {
        ParseResults<CommandSource> results = DISPATCHER.parse(message, source);
        DISPATCHER.execute(results);
    }

    public CommandDispatcher<CommandSource> getDispatcher() {
        return DISPATCHER;
    }

    public CommandSource getCommandSource() {
        return COMMAND_SOURCE;
    }

    private final static class ChatCommandSource extends ClientCommandSource {
        public ChatCommandSource(MinecraftClient client) {
            super(null, client);
        }
    }

    public void add(Command command) {
        commands.removeIf(command1 -> command1.getName().equals(command.getName()));
        commandInstances.values().removeIf(command1 -> command1.getName().equals(command.getName()));

        command.registerTo(DISPATCHER);
        commands.add(command);
        commandInstances.put(command.getClass(), command);
    }

    public int getCount() {
        return commands.size();
    }

    public List<Command> getCommands() {
        return commands;
    }

    @SuppressWarnings("unchecked")
    public <T extends Command> T get(Class<T> klass) {
        return (T) commandInstances.get(klass);
    }

	public String getPrefix() {
		return ".";
	}
	
	public Command getCommandByName(String commandName) {
		for(Command command : commands) {
			if ((command.getName().trim().equalsIgnoreCase(commandName)) || (command.toString().trim().equalsIgnoreCase(commandName.trim()))) {
				return command;
			}
		}
		return null;
	}
}
