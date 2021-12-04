package badgamesinc.hypnotic.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import badgamesinc.hypnotic.utils.Wrapper;
import baritone.api.BaritoneAPI;
import net.minecraft.command.CommandSource;

public class Baritone extends Command {

	public Baritone() {
		super("baritone", "Run the funy bot", new String[] {"baritone", "b"});
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("command", StringArgumentType.greedyString())
                .executes(context -> {
                    String command = context.getArgument("command", String.class);
                    BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute(command);
                    return SINGLE_SUCCESS;
                }));
	}

}
