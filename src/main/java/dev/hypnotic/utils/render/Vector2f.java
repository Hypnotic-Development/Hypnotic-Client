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
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Vector2f {
    
    public float x,y;

    public Vector2f() {
        this.x = 0;
        this.y = 0;
    }

    public Vector2f(Vec3d vec3d) {
        this.x = (float) vec3d.x;
        this.y = (float) vec3d.y;
    }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Vector2f multiply(float mulX, float mulY) {
        this.x *= mulX;
        this.y *= mulY;
        return this;
    }

    public Vector2f divide(float divX, float divY) {
        this.x /= divX;
        this.y /= divY;
        return this;
    }

    public Vector2f add(float addX, float addY) {
        this.x += addX;
        this.y += addY;
        return this;
    }

    public Vector2f subtract(float subX, float subY) {
        this.x -= subX;
        this.y -= subY;
        return this;
    }

    public Vector2f multiply(Vector2f vector3f) {
        return multiply(vector3f.getX(), vector3f.getY());
    }

    public Vector2f divide(Vector2f vector3f) {
        return divide(vector3f.getX(), vector3f.getY());
    }

    public Vector2f add(Vector2f vector3f) {
        return add(vector3f.getX(), vector3f.getY());
    }

    public Vector2f subtract(Vector2f vector3f) {
        return subtract(vector3f.getX(), vector3f.getY());
    }

    public Vector2f multiply(float mul) {
        this.x *= mul;
        this.y *= mul;
        return this;
    }

    public Vector2f divide(float div) {
        this.x /= div;
        this.y /= div;
        return this;
    }

    public Vector2f add(float add) {
        this.x += add;
        this.y += add;
        return this;
    }

    public Vector2f subtract(float sub) {
        this.x -= sub;
        this.y -= sub;
        return this;
    }

    public Vec2f toMinecraft() {
        return new Vec2f(x, y);
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }
}

