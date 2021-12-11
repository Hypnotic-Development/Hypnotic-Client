package dev.hypnotic.hypnotic_client.mixin;

import static dev.hypnotic.hypnotic_client.utils.MCUtils.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.hypnotic_client.event.events.EventCollide;
import net.minecraft.entity.Entity;

@Mixin(Entity.class)
public class EntityMixin {

	@Inject(method = "pushAwayFrom", at = @At("HEAD"), cancellable = true)
	public void onPushAwayFrom(Entity entity, CallbackInfo ci) {
		if (entity == mc.player) {
			EventCollide.Entity event = new EventCollide.Entity();
			event.call();
			if (event.isCancelled()) ci.cancel();
		}
	}
}
