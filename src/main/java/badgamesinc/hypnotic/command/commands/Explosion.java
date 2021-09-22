package badgamesinc.hypnotic.command.commands;

import java.util.Random;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.command.CommandSource;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

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
			mc.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, mc.player.getX(), mc.player.getY() + 0.5D, mc.player.getZ(), 0.0D, 0.0D, 0.0D);
			mc.getSoundManager().play(PositionedSoundInstance.ambient(new SoundEvent(new Identifier("entity.generic.explode")), 1, 1));
			return SINGLE_SUCCESS;
		});
		
	}

}
