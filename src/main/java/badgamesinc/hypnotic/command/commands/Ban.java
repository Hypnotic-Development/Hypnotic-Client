package badgamesinc.hypnotic.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import net.minecraft.command.CommandSource;

public class Ban extends Command {
    
	public Ban() {
		super("ban", "Bans everyone but you and your friends if you have op", new String[] {"ban"});
		// TODO Auto-generated constructor stub
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		// TODO Auto-generated method stub
		
	}
}
