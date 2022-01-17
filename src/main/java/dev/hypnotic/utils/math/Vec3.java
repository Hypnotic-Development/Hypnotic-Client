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
package dev.hypnotic.utils.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Objects;

public class Vec3 {
    public double x, y, z;

    public Vec3() {}

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3 set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;

        return this;
    }

    public Vec3 set(Vec3 vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;

        return this;
    }

    public Vec3 set(Vec3d vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;

        return this;
    }

    public Vec3 set(Entity entity, double tickDelta) {
        x = MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
        y = MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
        z = MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());

        return this;
    }

    public Vec3 add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;

        return this;
    }

    public Vec3 add(Vec3 vec) {
        return add(vec.x, vec.y, vec.z);
    }

    public Vec3 subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;

        return this;
    }

    public Vec3 subtract(Vec3d vec) {
        return subtract(vec.x, vec.y, vec.z);
    }

    public Vec3 multiply(double x, double y, double z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;

        return this;
    }

    public Vec3 multiply(double v) {
        return multiply(v, v, v);
    }

    public Vec3 divide(double v) {
        x /= v;
        y /= v;
        z /= v;

        return this;
    }

    public void negate() {
        x = -x;
        y = -y;
        z = -z;
    }

    public double distanceTo(Vec3 vec) {
        double d = vec.x - x;
        double e = vec.y - y;
        double f = vec.z - z;

        return Math.sqrt(d * d + e * e + f * f);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Vec3 normalize() {
        return divide(length());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vec3 vec3 = (Vec3) o;
        return Double.compare(vec3.x, x) == 0 && Double.compare(vec3.y, y) == 0 && Double.compare(vec3.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return String.format("[%.3f, %.3f, %.3f]", x, y, z);
    }
}
