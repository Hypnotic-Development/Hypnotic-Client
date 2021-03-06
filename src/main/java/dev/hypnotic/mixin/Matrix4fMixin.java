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

import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import dev.hypnotic.utils.math.Vec4;
import dev.hypnotic.utils.mixin.IMatrix4f;

@Mixin(Matrix4f.class)
public class Matrix4fMixin implements IMatrix4f {
    @Shadow protected float a00;
    @Shadow protected float a10;
    @Shadow protected float a20;
    @Shadow protected float a30;

    @Shadow protected float a01;
    @Shadow protected float a11;
    @Shadow protected float a21;
    @Shadow protected float a31;

    @Shadow protected float a02;
    @Shadow protected float a12;
    @Shadow protected float a22;
    @Shadow protected float a32;

    @Shadow protected float a03;
    @Shadow protected float a13;
    @Shadow protected float a23;
    @Shadow protected float a33;

    @Override
    public void multiplyMatrix(Vec4 v, Vec4 out) {
        out.set(
                a00 * v.x + a01 * v.y + a02 * v.z + a03 * v.w,
                a10 * v.x + a11 * v.y + a12 * v.z + a13 * v.w,
                a20 * v.x + a21 * v.y + a22 * v.z + a23 * v.w,
                a30 * v.x + a31 * v.y + a32 * v.z + a33 * v.w
        );
    }

    @Override
    public Vec3d mul(Vec3d vec) {
        return new Vec3d(
            vec.x * a00 + vec.y * a01 + vec.z * a02 + a03,
            vec.x * a10 + vec.y * a11 + vec.z * a12 + a13,
            vec.x * a20 + vec.y * a21 + vec.z * a22 + a23
        );
    }
}
