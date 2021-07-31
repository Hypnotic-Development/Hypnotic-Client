package badgamesinc.hypnotic.command;

import java.util.concurrent.CopyOnWriteArrayList;

import badgamesinc.hypnotic.command.commands.*;

public class CommandManager {

	public static CommandManager INSTANCE = new CommandManager();
	public CopyOnWriteArrayList<Command> commands = new CopyOnWriteArrayList<>();
	
	public CommandManager() 
	{
		commands.add(new Bind());
		commands.add(new VClip());
	}
	
	public CopyOnWriteArrayList<Command> getCommands() 
	{
		return commands;
	}
	
	public void callCommand(String input)
	{
		String[] split = input.split(" ");
		String command = split[0];
		String args = input.substring(command.length()).trim();
		for(Command c: getCommands()){
			if(c.getAlias().equalsIgnoreCase(command))
			{
				try
				{
					c.onCommand(args, args.split(" "));
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				return;
			}
		}

	}
}
