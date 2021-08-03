package badgamesinc.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import badgamesinc.hypnotic.command.CommandManager;
import badgamesinc.hypnotic.event.events.EventPlayerJump;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.utils.Wrapper;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(at = @At("HEAD"), method = "sendChatMessage()V", cancellable = true)
	private void onSendChatMessage(String message, CallbackInfo callbackInfo) {
		if (message.startsWith(CommandManager.get().getPrefix())) {
			try {
				CommandManager.get().dispatch(message.substring(CommandManager.get().getPrefix().length()));
            } catch (CommandSyntaxException e) {
                Wrapper.tellPlayer(e.getMessage());
            }
			callbackInfo.cancel();
		}
	}
	
	@Inject(method = "move", at = @At(value = "HEAD"))
    public void onMotion(CallbackInfo ci) {
        for (Mod mod : ModuleManager.INSTANCE.getEnabledModules()) {
        	mod.onMotion();
        }
    }
	
	@Override
    public void jump() {
        EventPlayerJump event = new EventPlayerJump();
        event.call();
        if(!event.isCancelled()) super.jump();
    }
}
