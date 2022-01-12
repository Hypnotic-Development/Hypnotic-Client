package dev.hypnotic.utils;

import static dev.hypnotic.utils.MCUtils.mc;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;

public class RotationVector {

    private float yaw, pitch;

    public RotationVector(LivingEntity entity) {
        this.yaw = entity.getYaw();
        this.pitch = entity.getPitch();
    }

    public RotationVector(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public void normalize() {
        this.yaw = MathHelper.wrapDegrees(yaw);
        this.pitch = MathHelper.wrapDegrees(pitch);
    }

    public void add(float yaw, float pitch) {
        this.yaw += yaw;
        this.pitch += pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

	public static RotationVector fromPlayer() {
        return new RotationVector(mc.player);
    }
}
