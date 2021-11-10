package badgamesinc.hypnotic.command.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import badgamesinc.hypnotic.utils.player.PlayerUtils;
import net.minecraft.command.CommandSource;
import net.minecraft.util.math.BlockPos;

public class Teleport extends Command {

	public Teleport() {
		super("teleport", "Teleports you to your desired location", "tp");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("x", IntegerArgumentType.integer()).then(argument("y", IntegerArgumentType.integer()).then(argument("z", IntegerArgumentType.integer()).executes(context -> {
			int x = context.getArgument("x", Integer.class);
			int y = context.getArgument("y", Integer.class);
			int z = context.getArgument("z", Integer.class);
			PlayerUtils.teleport(new BlockPos(x, y, z));
			info("Teleported you to " + x + " " + y + " " + z);
			return SINGLE_SUCCESS;
		}))));
	}
}
