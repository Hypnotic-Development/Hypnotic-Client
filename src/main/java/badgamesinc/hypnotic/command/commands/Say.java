package badgamesinc.hypnotic.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import net.minecraft.command.CommandSource;

public class Say extends Command {

	public Say() {
		super("Say", "Says what you type in chat", "say");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("message", StringArgumentType.greedyString()).executes(context -> {
			String msg = context.getArgument("message", String.class);
			mc.player.sendChatMessage(msg);
			return SINGLE_SUCCESS;
		})); 
		
	}
}
