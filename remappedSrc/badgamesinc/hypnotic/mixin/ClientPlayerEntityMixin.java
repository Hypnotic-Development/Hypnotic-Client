package badgamesinc.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import badgamesinc.hypnotic.event.events.EventMove;
import badgamesinc.hypnotic.event.events.EventPlayerJump;
import badgamesinc.hypnotic.event.events.EventPushOutOfBlocks;
import badgamesinc.hypnotic.event.events.EventSendMessage;
import badgamesinc.hypnotic.event.events.EventSwingHand;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.player.NoSlow;
import badgamesinc.hypnotic.module.player.PortalGui;
import badgamesinc.hypnotic.module.player.Scaffold;
import badgamesinc.hypnotic.module.render.ChatImprovements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

	@Shadow protected abstract void autoJump(float dx, float dz);
	@Shadow public abstract void sendChatMessage(String string);
	private boolean ignoreMessage = false;
	
	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}
	
	@Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo info) {
		if (ignoreMessage) return;
		if (!message.startsWith(".") && !message.startsWith("/")) {
			EventSendMessage event = new EventSendMessage(message);
			event.call();
			if (!event.isCancelled()) {
				ignoreMessage = true;
				sendChatMessage(event.getMessage() + ModuleManager.INSTANCE.getModule(ChatImprovements.class).getSuffix());
				ignoreMessage = false;
			}
			info.cancel();
			if (event.isCancelled()) info.cancel();
		}
	}
	
	@Inject(method = "move", at = @At(value = "HEAD"), cancellable = true)
    public void onMotion(MovementType type, Vec3d movement, CallbackInfo ci) {
        for (Mod mod : ModuleManager.INSTANCE.getEnabledModules()) {
        	mod.onMotion();
        }
        
        if (type == MovementType.SELF) {
            EventMove eventMove = new EventMove(movement.x, movement.y, movement.z);
            eventMove.call();
            movement = new Vec3d(eventMove.getX(), eventMove.getY(), eventMove.getZ());
            double d = this.getX();
            double e = this.getZ();
            super.move(type, movement);
            this.autoJump((float) (this.getX() - d), (float) (this.getZ() - e));
            ci.cancel();
        }
    }
	
	@Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    public void pushOut(double x, double y, CallbackInfo ci) {
        EventPushOutOfBlocks eventPushOutOfBlocks = new EventPushOutOfBlocks();
        eventPushOutOfBlocks.call();
        if (eventPushOutOfBlocks.isCancelled())
            ci.cancel();
    }
	
	@Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean redirectUsingItem(ClientPlayerEntity player) {
        if (ModuleManager.INSTANCE.getModule(NoSlow.class).isEnabled()) return false;
        return player.isUsingItem();
    }

    @Inject(method = "isSneaking", at = @At("HEAD"), cancellable = true)
    private void onIsSneaking(CallbackInfoReturnable<Boolean> info) {
        if (ModuleManager.INSTANCE.getModule(Scaffold.class).isEnabled() && ModuleManager.INSTANCE.getModule(Scaffold.class).down.isEnabled()) info.setReturnValue(false);
    }

    @Inject(method = "shouldSlowDown", at = @At("HEAD"), cancellable = true)
    private void onShouldSlowDown(CallbackInfoReturnable<Boolean> info) {
        if (ModuleManager.INSTANCE.getModule(NoSlow.class).isEnabled() && !this.isSneaking()) {
            info.setReturnValue(shouldLeaveSwimmingPose());
        }
    }
    
    @Inject(method = "swingHand", at = @At("HEAD"), cancellable = true)
    public void onSwingHand(Hand hand, CallbackInfo ci) {
    	EventSwingHand event = new EventSwingHand(hand);
    	event.call();
    	if (event.isCancelled()) ci.cancel();
    }
    
    @Redirect(method = "updateNausea", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;closeHandledScreen()V", ordinal = 0), require = 0)
	private void updateNausea_closeHandledScreen(ClientPlayerEntity player) {
		if (!ModuleManager.INSTANCE.getModule(PortalGui.class).isEnabled()) {
			closeHandledScreen();
		}
	}

	@Redirect(method = "updateNausea", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 0), require = 0)
	private void updateNausea_setScreen(MinecraftClient client, Screen screen) {
		if (!ModuleManager.INSTANCE.getModule(PortalGui.class).isEnabled()) {
			client.setScreen(screen);
		}
	}
	
	@Override
    public void jump() {
        EventPlayerJump event = new EventPlayerJump();
        event.call();
        if(!event.isCancelled()) super.jump();
    }
}
