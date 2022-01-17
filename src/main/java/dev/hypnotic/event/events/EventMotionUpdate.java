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
package dev.hypnotic.event.events;

import dev.hypnotic.event.Event;

public class EventMotionUpdate extends Event{
    double x, y, z;
    float yaw, pitch, lastYaw, lastPitch;
    Event.State state;
    boolean onGround, sneaking;
    public EventMotionUpdate(double x, double y, double z, float yaw, float pitch, float lastYaw, float lastPitch, boolean onGround, boolean sneaking, Event.State state){
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.lastYaw = lastYaw;
        this.lastPitch = lastPitch;
        this.state = state;
        this.onGround = onGround;
        this.sneaking = sneaking;
    }

    public boolean isPre(){
        return this.state == State.PRE;
    }

    public boolean onGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getLastYaw() {
        return lastYaw;
    }

    public void setLastYaw(float lastYaw) {
        this.lastYaw = lastYaw;
    }

    public float getLastPitch() {
        return lastPitch;
    }

    public void setLastPitch(float lastPitch) {
        this.lastPitch = lastPitch;
    }  

    public boolean isOnGround() {
		return onGround;
	}
    
	public boolean isSneaking() {
		return sneaking;
	}

	public void setSneaking(boolean sneaking) {
		this.sneaking = sneaking;
	}

	public Event.State getState() {
        return state;
    }

    public void setState(Event.State state) {
        this.state = state;
    }
}
