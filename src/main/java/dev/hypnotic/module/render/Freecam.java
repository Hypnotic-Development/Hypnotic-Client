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
package dev.hypnotic.module.render;

import org.lwjgl.glfw.GLFW;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventKeyPress;
import dev.hypnotic.event.events.EventPushOutOfBlocks;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.RotationUtils;
import dev.hypnotic.utils.input.KeyUtils;
import dev.hypnotic.utils.math.Vec3;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Freecam extends Mod {

	public NumberSetting speed = new NumberSetting("Speed", 1, 0, 10, 0.1);
	
	public final Vec3 pos = new Vec3();
    public final Vec3 prevPos = new Vec3();

    private Perspective perspective;

    public float yaw, pitch;
    public float prevYaw, prevPitch;

    private boolean forward, backward, right, left, up, down;
    public static PlayerEntity playerEntity;
    
	public Freecam() {
		super("Freecam", "Leave your body and explore", Category.RENDER);
		addSettings(speed);
	}
    
    @EventTarget
    public void pushOutOfBlocks(EventPushOutOfBlocks event) {
    	event.setCancelled(true);
    }
    
    @Override
    public void onTick() {
    	perspective = mc.options.getPerspective();
    	if (mc.cameraEntity.isInsideWall()) mc.getCameraEntity().noClip = true;
        if (!perspective.isFirstPerson()) mc.options.setPerspective(Perspective.FIRST_PERSON);

        if (mc.currentScreen != null) return;

        yaw = mc.cameraEntity.getYaw();
        Vec3d forward = Vec3d.fromPolar(0, yaw);
        Vec3d right = Vec3d.fromPolar(0, yaw + 90);
        double velX = 0;
        double velY = 0;
        double velZ = 0;


        BlockPos crossHairPos;
        Vec3d crossHairPosition;

        if (mc.crosshairTarget instanceof EntityHitResult) {
            crossHairPos = ((EntityHitResult) mc.crosshairTarget).getEntity().getBlockPos();
            RotationUtils.rotate(RotationUtils.getYaw(crossHairPos), RotationUtils.getPitch(crossHairPos), 0, null);
        } else {
            crossHairPosition = mc.crosshairTarget.getPos();
            crossHairPos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();

            if (!mc.world.getBlockState(crossHairPos).isAir()) {
                RotationUtils.rotate(RotationUtils.getYaw(crossHairPosition), RotationUtils.getPitch(crossHairPosition), 0, null);
            }
        }

        double s = 0.5;
        if (mc.options.sprintKey.isPressed()) s = 1;

        boolean a = false;
        if (this.forward) {
            velX += forward.x * s * speed.getValue();
            velZ += forward.z * s * speed.getValue();
            a = true;
        }
        if (this.backward) {
            velX -= forward.x * s * speed.getValue();
            velZ -= forward.z * s * speed.getValue();
            a = true;
        }

        boolean b = false;
        if (this.right) {
            velX += right.x * s * speed.getValue();
            velZ += right.z * s * speed.getValue();
            b = true;
        }
        if (this.left) {
            velX -= right.x * s * speed.getValue();
            velZ -= right.z * s * speed.getValue();
            b = true;
        }

        if (a && b) {
            double diagonal = 1 / Math.sqrt(2);
            velX *= diagonal;
            velZ *= diagonal;
        }

        if (this.up) {
            velY += s * speed.getValue();
        }
        if (this.down) {
            velY -= s * speed.getValue();
        }

        prevPos.set(pos);
        pos.set(pos.x + velX, pos.y + velY, pos.z + velZ);
    	super.onTick();
    }

    @Override
    public void onEnable() {
    	yaw = mc.player.getYaw();
        pitch = mc.player.getPitch();

        perspective = mc.options.getPerspective();

        pos.set(mc.gameRenderer.getCamera().getPos());
        prevPos.set(mc.gameRenderer.getCamera().getPos());

        prevYaw = yaw;
        prevPitch = pitch;

        forward = false;
        backward = false;
        right = false;
        left = false;
        up = false;
        down = false;

        unpress();
        super.onEnable();
    }

    @EventTarget
    private void onKey(EventKeyPress event) {
        if (KeyUtils.isKeyPressed(GLFW.GLFW_KEY_F3)) return;

        boolean cancel = true;

        if (mc.options.forwardKey.matchesKey(event.getKey(), 0) || mc.options.forwardKey.matchesMouse(event.getKey())) {
            forward = event.getAction() != GLFW.GLFW_RELEASE;
        } else if (mc.options.backKey.matchesKey(event.getKey(), 0) || mc.options.backKey.matchesMouse(event.getKey())) {
            backward = event.getAction() != GLFW.GLFW_RELEASE;
        } else if (mc.options.rightKey.matchesKey(event.getKey(), 0) || mc.options.rightKey.matchesMouse(event.getKey())) {
            right = event.getAction() != GLFW.GLFW_RELEASE;
        } else if (mc.options.leftKey.matchesKey(event.getKey(), 0) || mc.options.leftKey.matchesMouse(event.getKey())) {
            left = event.getAction() != GLFW.GLFW_RELEASE;
        } else if (mc.options.jumpKey.matchesKey(event.getKey(), 0) || mc.options.jumpKey.matchesMouse(event.getKey())) {
            up = event.getAction() != GLFW.GLFW_RELEASE;
        } else if (mc.options.sneakKey.matchesKey(event.getKey(), 0) || mc.options.sneakKey.matchesMouse(event.getKey())) {
            down = event.getAction() != GLFW.GLFW_RELEASE;
        } else {
            cancel = false;
        }

        if (cancel) event.setCancelled(true);;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        mc.options.setPerspective(perspective);
    }

    private void unpress() {
        mc.options.forwardKey.setPressed(false);
        mc.options.backKey.setPressed(false);
        mc.options.rightKey.setPressed(false);
        mc.options.leftKey.setPressed(false);
        mc.options.jumpKey.setPressed(false);
        mc.options.sneakKey.setPressed(false);
    }
    
    public double getX(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPos.x, pos.x);
    }
    public double getY(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPos.y, pos.y);
    }
    public double getZ(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPos.z, pos.z);
    }

    public double getYaw(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevYaw, yaw);
    }
    public double getPitch(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPitch, pitch);
    }
}
