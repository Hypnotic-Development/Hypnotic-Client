package badgamesinc.hypnotic.command.commands;

import java.util.Random;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import net.minecraft.command.CommandSource;

public class Explosion extends Command {

	public Explosion() {
		super("explosion", "Throws you in the air like an explosion would");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.executes(context -> {
			int min = -5;
			int max = 5;
			mc.player.addVelocity(new Random().nextInt(max - min) + min, new Random().nextInt(3 - 1) + 1, new Random().nextInt(max - min) + min);
			info("BOOM... exploded");
			return SINGLE_SUCCESS;
		});
		
	}

}
