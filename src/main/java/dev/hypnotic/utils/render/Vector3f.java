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
*/
package dev.hypnotic.utils.render;

/**
* @author BadGamesInc
*/
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;

public class Vector3f {
    
    public float x,y,z;

    public Vector3f() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3f(Vec3d vec3d) {
        this.x = (float) vec3d.x;
        this.y = (float) vec3d.y;
        this.z = (float) vec3d.z;
    }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Vector3f multiply(double mulX, double mulY, double mulZ) {
        this.x *= mulX;
        this.y *= mulY;
        this.z *= mulZ;
        return this;
    }

    public Vector3f divide(double divX, double divY, double divZ) {
        this.x /= divX;
        this.y /= divY;
        this.z /= divZ;
        return this;
    }

    public Vector3f add(double addX, double addY, double addZ) {
        this.x += addX;
        this.y += addY;
        this.z += addZ;
        return this;
    }

    public Vector3f subtract(double subX, double subY, double subZ) {
        this.x -= subX;
        this.y -= subY;
        this.z -= subZ;
        return this;
    }

    public Vector3f transform(Matrix4f matrix4f) {
        return transform(Matrix4x4.copyFromRowMajor(matrix4f));
    }

    public Vector3f transform(Matrix4x4 matrix4x4) {
        double f = this.x;
        double g = this.y;
        double h = this.z;
        this.x = (float) (matrix4x4.a00 * f + matrix4x4.a01 * g + matrix4x4.a02 * h + matrix4x4.a03);
        this.y = (float) (matrix4x4.a10 * f + matrix4x4.a11 * g + matrix4x4.a12 * h + matrix4x4.a13);
        this.z = (float) (matrix4x4.a20 * f + matrix4x4.a21 * g + matrix4x4.a22 * h + matrix4x4.a23);
        return this;
    }

    public Vector3f multiply(Vector3f vector3f) {
        return multiply(vector3f.getX(), vector3f.getY(), vector3f.getZ());
    }

    public Vector3f divide(Vector3f vector3f) {
        return divide(vector3f.getX(), vector3f.getY(), vector3f.getZ());
    }

    public Vector3f add(Vector3f vector3f) {
        return add(vector3f.getX(), vector3f.getY(), vector3f.getZ());
    }

    public Vector3f subtract(Vector3f vector3f) {
        return subtract(vector3f.getX(), vector3f.getY(), vector3f.getZ());
    }

    public Vector3f multiply(double mul) {
        this.x *= mul;
        this.y *= mul;
        this.z *= mul;
        return this;
    }

    public Vector3f divide(double div) {
        this.x /= div;
        this.y /= div;
        this.z /= div;
        return this;
    }

    public Vector3f add(double add) {
        this.x += add;
        this.y += add;
        this.z += add;
        return this;
    }

    public Vector3f subtract(double sub) {
        this.x -= sub;
        this.y -= sub;
        this.z -= sub;
        return this;
    }

    public Vec3d toMinecraft() {
        return new Vec3d(x, y, z);
    }

    public float length() {
        return (float) Math.sqrt(Math.fma(x, x, Math.fma(y, y, z * z)));
    }
}

