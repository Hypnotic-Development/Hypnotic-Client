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
