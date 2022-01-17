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
package dev.hypnotic.module.movement;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventMotionUpdate;
import dev.hypnotic.event.events.EventReceivePacket;
import dev.hypnotic.event.events.EventRender3D;
import dev.hypnotic.event.events.EventSendPacket;
import dev.hypnotic.mixin.PlayerMoveC2SPacketAccessor;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.combat.Killaura;
import dev.hypnotic.module.combat.TargetStrafe;
import dev.hypnotic.module.exploit.AntiHunger;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Timer;
import dev.hypnotic.utils.player.PlayerUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;

public class Flight extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Velocity", "Velocity", "Vanilla", "Verus");
	public NumberSetting speed = new NumberSetting("Speed", 1, 0, 10, 0.1);
	public BooleanSetting damage = new BooleanSetting("Damage", false);
	public BooleanSetting blink = new BooleanSetting("Blink", false);
	public BooleanSetting viewBobbing = new BooleanSetting("View Bobbing", false);
	boolean hasDamaged = false;
	private int wallTicks = 0;
	private boolean direction = false;
	private static Timer blinkTimer = new Timer();
	
    public Flight() {
        super("Flight", "Allows you to fly", Category.MOVEMENT);
        addSettings(mode, speed, damage, blink, viewBobbing);
    }
    
    @Override
    public void onEnable() {
    	super.onEnable();
    }
    
    @EventTarget
    public void eventRender3d(EventRender3D event) {
    	if (viewBobbing.isEnabled()) {
    		if (mc.getCameraEntity() instanceof PlayerEntity) {
    			PlayerEntity playerEntity = (PlayerEntity)mc.getCameraEntity();
    			MatrixStack matrices = event.getMatrices();
    			matrices.push();
    			float g = playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed;
    			float h = -(playerEntity.horizontalSpeed + g * event.getTickDelta());
    			float i = MathHelper.lerp(event.getTickDelta(), wallTicks, wallTicks);
    			matrices.translate((double)(MathHelper.sin(h * 3.1415927F) * i * 0.5F), (double)(-Math.abs(MathHelper.cos(h * 3.1415927F) * i)), 0.0D);
    			matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.sin(h * 3.1415927F) * i * 3.0F));
    			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(Math.abs(MathHelper.cos(h * 3.1415927F - 0.2F) * i) * 5.0F));
    			matrices.pop();
    		}
    	}
    }

    @Override
    public void onTick() {
    	
        super.onTick();
    }
    
    @EventTarget
    public void onMotion(EventMotionUpdate event) {
    	this.setDisplayName("Flight " + ColorUtils.gray + mode.getSelected());
    	if (mc.player == null)
    		return;
    	if (!mc.player.isOnGround()) {
			wallTicks++;
			if (wallTicks > 7 && mc.player.horizontalCollision) {
				direction = !direction;
				wallTicks = 0;
			}
		} else wallTicks = 0;
    	if (damage.isEnabled() && !hasDamaged) {
    		boolean antiHunger = ModuleManager.INSTANCE.getModule(AntiHunger.class).isEnabled();
    		if (antiHunger) ModuleManager.INSTANCE.getModule(AntiHunger.class).toggleSilent();
    		mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 3.1, mc.player.getZ(), true));
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), true));
            hasDamaged = true;
            if (antiHunger) ModuleManager.INSTANCE.getModule(AntiHunger.class).toggleSilent();
    	}
    	if (mode.is("Vanilla")) {
    		mc.player.getAbilities().flying = true;
    	} else {
    		mc.player.getAbilities().flying = false;
    		mc.player.airStrafingSpeed = (float) speed.getValue();
    		
        	mc.player.setVelocity(0, 0, 0);
        		
    		Vec3d velocity = mc.player.getVelocity();
    		
    		if (TargetStrafe.canStrafe()) {
    			mc.player.getAbilities().flying = true;
    			TargetStrafe.strafe(speed.getValue(), Killaura.target, direction, true);
            }
    		
    		if (mode.is("Verus")) {
    			mc.player.setVelocity(mc.player.getVelocity().x, 0, mc.player.getVelocity().z);
    			mc.player.setOnGround(true);
    			mc.player.verticalCollision = true;
    			mc.player.getAbilities().flying = false;
    			if (event.isPre()) event.setOnGround(true);
    			PlayerUtils.setMotion(speed.getValue());
    		} else {
	    		if(mc.options.keyJump.isPressed() && !mc.options.keySneak.isPressed()) {
	    			mc.player.setVelocity(velocity.add(0, speed.getValue(), 0));
	    		}
	    		
	    		if(mc.options.keySneak.isPressed() && !mc.options.keyJump.isPressed()) {
	    			mc.player.setVelocity(velocity.subtract(0, speed.getValue(), 0));
	    		}
    		}
    		
    	}
    }
    
    @EventTarget
    public void eventReceivePacket(EventReceivePacket event) {
    	if (event.getPacket() instanceof PlayerMoveC2SPacket && mode.is("Verus") && !damage.isEnabled()) {
    		((PlayerMoveC2SPacketAccessor) event.getPacket()).setOnGround(true);
    	}
    }
    
    @EventTarget 
    public void eventSendPacket(EventSendPacket event) {
    	if (blink.isEnabled()) {
    		if (blinkTimer.hasTimeElapsed(50, true) && (damage.isEnabled() ? hasDamaged : true)) {
    			ModuleManager.INSTANCE.getModule(FlightBlink.class).toggle();
    		}
    	}
    }

    @Override
    public void onDisable() {
    	mc.player.getAbilities().flying = false;
    	hasDamaged = false;
    	if (ModuleManager.INSTANCE.getModule(FlightBlink.class).isEnabled()) ModuleManager.INSTANCE.getModule(FlightBlink.class).toggle();
        super.onDisable();
    }
}
