package badgamesinc.hypnotic.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import badgamesinc.hypnotic.command.commands.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;

/*
 * commands.add(new Bind());
		commands.add(new VClip());
//		commands.add(new Ban());
		commands.add(new Friend());
		commands.add(new NBT());
		// .nbt set {EntityTag:{CustomName:":)",Offers:{Recipes:[{buy:{Count:1,Coutn1id:"dirt",id:"air"},buyB:{Count:1,id:"dirt"},maxUses:99,sell:{Count:1,id:"stick"}}]},id:"minecraft:wandering_trader"},display:{Name:'{"text":"Wandering Crasher","color":"dark_red"}'}}
 */
public class CommandManager {

	private static MinecraftClient mc = MinecraftClient.getInstance();
	private final CommandDispatcher<CommandSource> DISPATCHER = new CommandDispatcher<>();
    private final CommandSource COMMAND_SOURCE = new ChatCommandSource(mc);
    private final List<Command> commands = new ArrayList<>();
    private final Map<Class<? extends Command>, Command> commandInstances = new HashMap<>();

    private CommandManager() {
    	add(new VClip());
        add(new Ban());
        add(new FriendCmd());
        add(new NBT());
        add(new Enchant());
        add(new Give());
        add(new Bind());
        add(new Say());
        add(new Baritone());
        commands.sort(Comparator.comparing(Command::getName));
    }

    public static CommandManager get() {
        return new CommandManager();
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

    public List<Command> getAll() {
        return commands;
    }

    @SuppressWarnings("unchecked")
    public <T extends Command> T get(Class<T> klass) {
        return (T) commandInstances.get(klass);
    }

	public String getPrefix() {
		return ".";
	}
}
