package badgamesinc.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
	
//	if (packet instanceof PlayerListS2CPacket) {
//		for (PlayerListEntry playerListEntry : mc.getNetworkHandler().getPlayerList()) {
//			System.out.println("e");
////			try {
////				Hypnotic.setHypnoticUser(playerListEntry.getProfile().getName(), playerListEntry.getProfile().getName());
////			} catch (IOException | InterruptedException e) {
////				e.printStackTrace();
////			}
//		}
//	}

	@Inject(method = "addPlayer", at = @At("HEAD"))
	private void addPlayer(ServerPlayerEntity player, CallbackInfo ci) {
		System.out.println("he");
//		try {
//			Hypnotic.setHypnoticUser(entity.getName().asString(), Hypnotic.INSTANCE.api.checkOnline(entity.getName().asString()));
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//		}
	}
	
	@Inject(method = "removePlayer", at = @At("HEAD"))
	private void removePlayer(ServerPlayerEntity player, Entity.RemovalReason reason, CallbackInfo ci) {
		System.out.println("he");
//		try {
//			Hypnotic.setHypnoticUser(entity.getName().asString(), Hypnotic.INSTANCE.api.checkOnline(entity.getName().asString()));
//		} catch (IOException | InterruptedException e) {
//			e.printStackTrace();
//		}
	}
}
