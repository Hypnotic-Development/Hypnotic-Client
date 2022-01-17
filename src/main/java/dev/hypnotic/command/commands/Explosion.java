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
package dev.hypnotic.command.commands;

import java.util.Random;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
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
