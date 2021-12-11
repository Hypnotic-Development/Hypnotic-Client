package dev.hypnotic.hypnotic_client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.hypnotic.hypnotic_client.event.events.EventWalkOffLedge;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

	PlayerEntity playerEntity = (PlayerEntity) ((Object) this);
	
	@Inject(method = "clipAtLedge", at = @At("HEAD"), cancellable = true)
	public void atLedge(CallbackInfoReturnable<Boolean> cir) {
		EventWalkOffLedge event = new EventWalkOffLedge(playerEntity.isSneaking());
		event.call();
		cir.setReturnValue(event.isSneaking);
		cir.cancel();
	}
}
