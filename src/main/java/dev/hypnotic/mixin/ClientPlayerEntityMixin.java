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

import static dev.hypnotic.utils.MCUtils.mc;

import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import baritone.api.BaritoneAPI;
import dev.hypnotic.event.Event;
import dev.hypnotic.event.events.EventMotionUpdate;
import dev.hypnotic.event.events.EventMove;
import dev.hypnotic.event.events.EventPlayerJump;
import dev.hypnotic.event.events.EventPushOutOfBlocks;
import dev.hypnotic.event.events.EventSendMessage;
import dev.hypnotic.event.events.EventSwingHand;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.exploit.PortalGui;
import dev.hypnotic.module.hud.HudManager;
import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.module.player.NoSlow;
import dev.hypnotic.module.player.Scaffold;
import dev.hypnotic.module.render.ChatImprovements;
import dev.hypnotic.module.render.Freecam;
import dev.hypnotic.ui.BindingScreen;
import dev.hypnotic.ui.OptionsScreen;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.ClientPlayerTickable;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

	@Shadow protected abstract void autoJump(float dx, float dz);
	@Shadow public abstract void sendChatMessage(String string);
	@Shadow protected abstract boolean isCamera();
	@Unique private boolean lastSneaking;
	@Unique private boolean lastSprinting;
	@Unique private double lastX;
	@Unique private double lastBaseY;
	@Unique private double lastZ;
	@Unique private float lastYaw;
	@Unique private float lastPitch;
	@Unique private boolean lastOnGround;
	@Unique private int ticksSinceLastPositionPacketSent;
	@Shadow private boolean autoJumpEnabled = true;
	@Shadow @Final private List<ClientPlayerTickable> tickables;
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
	
	@Inject(method = "tick", at = @At("HEAD"), cancellable = true)
	public void tick(CallbackInfo ci) {
		for (Mod mod : ModuleManager.INSTANCE.modules) {
			if (mc.player != null) {
				if (mod.isBinding() && mc.currentScreen == null) {
					try {
						mc.setScreen(new BindingScreen(mod, null));
					} catch(Exception e) {
						
					}
				}
				if (mod.isEnabled()) mod.onTick();
				mod.onTickDisabled();
			}
		}
		for (HudModule mod : HudManager.INSTANCE.hudModules) {
			if (mc.player != null) {
				if (mod.isEnabled()) mod.onTick();
				mod.onTickDisabled();
			}
		}
		RenderUtils.INSTANCE.onTick();
		if (mc.world != null) BaritoneAPI.getSettings().chatControl.value = false;
		
		OptionsScreen options = OptionsScreen.INSTANCE;
		
		BaritoneAPI.getSettings().allowBreak.value = options.allowBreak.isEnabled();
		BaritoneAPI.getSettings().allowParkour.value = options.allowParkour.isEnabled();
		BaritoneAPI.getSettings().allowParkourAscend.value = options.allowParkour.isEnabled();
		BaritoneAPI.getSettings().allowParkourPlace.value = options.allowParkour.isEnabled();
		BaritoneAPI.getSettings().allowDownward.value = options.allowParkour.isEnabled();
		BaritoneAPI.getSettings().allowDiagonalAscend.value = options.allowParkour.isEnabled();
		BaritoneAPI.getSettings().allowDiagonalDescend.value = options.allowParkour.isEnabled();
		BaritoneAPI.getSettings().chatControl.value = options.chatControl.isEnabled();
		BaritoneAPI.getSettings().allowPlace.value = options.allowPlace.isEnabled();
		BaritoneAPI.getSettings().allowInventory.value = options.allowInventory.isEnabled();
		BaritoneAPI.getSettings().assumeWalkOnWater.value = options.assumeJesus.isEnabled();
		BaritoneAPI.getSettings().assumeWalkOnLava.value = options.assumeJesus.isEnabled();
		BaritoneAPI.getSettings().assumeStep.value = options.assumeStep.isEnabled();
		BaritoneAPI.getSettings().assumeSafeWalk.value = options.assumeSafewalk.isEnabled();
	}
	
	@Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
	private void sendMovementPackets(CallbackInfo ci) {
		if (!ModuleManager.INSTANCE.getModule(Freecam.class).isEnabled()) {
			this.sendMovementPacketsWithEvent();
			EventMotionUpdate event = new EventMotionUpdate(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), this.lastYaw, this.lastPitch, mc.player.isOnGround(), this.isSneaking(), Event.State.POST);
			event.call();
			ci.cancel();
		}
	}
	
	private void sendMovementPacketsWithEvent() {
		EventMotionUpdate event = new EventMotionUpdate(mc.player.getX(), mc.player.getY(), mc.player.getZ(), mc.player.getYaw(), mc.player.getPitch(), this.lastYaw, this.lastPitch, mc.player.isOnGround(), this.isSneaking(), Event.State.PRE);
		event.call();
		boolean bl = this.isSprinting();
		if (bl != this.lastSprinting) {
			ClientCommandC2SPacket.Mode mode = bl ? ClientCommandC2SPacket.Mode.START_SPRINTING : ClientCommandC2SPacket.Mode.STOP_SPRINTING;
			mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(this, mode));
			this.lastSprinting = bl;
		}

		boolean bl2 = this.isSneaking();
		if (bl2 != this.lastSneaking) {
			ClientCommandC2SPacket.Mode mode2 = bl2 ? ClientCommandC2SPacket.Mode.PRESS_SHIFT_KEY : ClientCommandC2SPacket.Mode.RELEASE_SHIFT_KEY;
			mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(this, mode2));
			this.lastSneaking = bl2;
		}

		if (this.isCamera()) {
			double d = event.getX() - this.lastX;
			double e = this.getY() - this.lastBaseY;
			double f = event.getZ() - this.lastZ;
			double g = (double)(event.getYaw() - this.lastYaw);
			double h = (double)(event.getPitch() - this.lastPitch);
			++this.ticksSinceLastPositionPacketSent;
			boolean bl3 = d * d + e * e + f * f > 9.0E-4D || this.ticksSinceLastPositionPacketSent >= 20;
			boolean bl4 = g != 0.0D || h != 0.0D;
			if (this.hasVehicle()) {
				Vec3d vec3d = this.getVelocity();
				mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(vec3d.x, -999.0D, vec3d.z, this.getYaw(), this.getPitch(), this.onGround));
				bl3 = false;
			} else if (bl3 && bl4) {
				mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.Full(event.getX(), this.getY(), event.getZ(), event.getYaw(), event.getPitch(), event.isOnGround()));
			} else if (bl3) {
				mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(event.getX(), this.getY(), event.getZ(), event.isOnGround()));
			} else if (bl4) {
				mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(event.getYaw(), event.getPitch(), event.isOnGround()));
			} else if (this.lastOnGround != this.onGround) {
				mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(event.isOnGround()));
			}

			if (bl3) {
				this.lastX = event.getX();
				this.lastBaseY = this.getY();
				this.lastZ = event.getZ();
				this.ticksSinceLastPositionPacketSent = 0;
			}

			if (bl4) {
				this.lastYaw = event.getYaw();
				this.lastPitch = event.getPitch();
			}

			this.lastOnGround = event.isOnGround();
			this.autoJumpEnabled = mc.options.autoJump;
		}
	}
	
	@Override
    public void jump() {
        EventPlayerJump event = new EventPlayerJump();
        event.call();
        if(!event.isCancelled()) super.jump();
    }
}
