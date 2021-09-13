package badgamesinc.hypnotic.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import badgamesinc.hypnotic.command.argtypes.PlayerArgumentType;
import badgamesinc.hypnotic.utils.Wrapper;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

public class Coords extends Command {

	public Coords() {
		super("coords", "steal someones location", "coords");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("target", PlayerArgumentType.player()).executes(context -> {
			PlayerEntity target = PlayerArgumentType.getPlayer(context, "target");
			if (target instanceof PlayerEntity) {
				Wrapper.tellPlayer(target.getX() + "");
				Wrapper.tellPlayer(target.getY() + "");
				Wrapper.tellPlayer(target.getZ() + "");
			}
			return SINGLE_SUCCESS;
		}));
	}

}
