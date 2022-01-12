package dev.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.network.PlayerListEntry;

@Mixin(PlayerListEntry.class)
public class PlayerListEntryMixin {

	// shitty way to check if someone is using the client that doesnt work
	
	@Inject(method = "setLatency", at = @At("HEAD"), cancellable = true)
	protected void onSetLatency(int latency, CallbackInfo ci) {
//		if (mc.getNetworkHandler() != null) {
//			if (mc.getNetworkHandler().getPlayerListEntry(((PlayerListEntry) (Object) this).getProfile().getId()).getProfile() != null) {
//				if (((PlayerListEntry) (Object) this).getProfile().getName().contains(mc.player.getName().getString())) {
//					ci.cancel();
//				}
//			}
//		}
	}
	
	@Inject(method = "getLatency", at = @At("RETURN"), cancellable = true)
	public void onGetLatency(CallbackInfoReturnable<Integer> cir) {
//		if (mc.getNetworkHandler() != null) {
//			if (((PlayerListEntry) (Object) this).getProfile() != null) {
//				if (((PlayerListEntry) (Object) this).getProfile().getName().contains(mc.player.getName().getString())) {
//					cir.setReturnValue(-1000);
//				}
//			}
//		}
	}
}
