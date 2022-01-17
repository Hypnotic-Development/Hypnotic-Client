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
package dev.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.event.events.EventParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {

	@Inject(method = "addParticle(Lnet/minecraft/client/particle/Particle;)V", at = @At("HEAD"), cancellable = true)
	public void addParticle(Particle particle, CallbackInfo ci) {
		EventParticle.Normal event = new EventParticle.Normal(particle);
		event.call();

		if (event.isCancelled()) {
			ci.cancel();
		}
	}

	@Inject(method = "addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;)V", at = @At("HEAD"), cancellable = true)
	public void addEmitter(Entity entity, ParticleEffect particleEffect, CallbackInfo callback) {
		EventParticle.Emitter event = new EventParticle.Emitter(particleEffect);
		event.call();

		if (event.isCancelled()) {
			callback.cancel();
		}
	}

	@Inject(method = "addEmitter(Lnet/minecraft/entity/Entity;Lnet/minecraft/particle/ParticleEffect;I)V", at = @At("HEAD"), cancellable = true)
	public void addEmitter_(Entity entity, ParticleEffect particleEffect, int maxAge, CallbackInfo callback) {
		EventParticle.Emitter event = new EventParticle.Emitter(particleEffect);
		event.call();

		if (event.isCancelled()) {
			callback.cancel();
		}
	}
}
