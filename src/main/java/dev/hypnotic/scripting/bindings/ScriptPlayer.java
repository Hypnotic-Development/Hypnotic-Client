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
package dev.hypnotic.scripting.bindings;

import static dev.hypnotic.utils.MCUtils.mc;

import dev.hypnotic.utils.render.Vector3D;
import net.minecraft.network.Packet;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * This class is used because Minecraft is obfuscated and GraalVM
 * can't find these methods without using their obfuscated names.
 * For example: mc.field_1724 would be equal to mc.player
 * but that is very obnoxious to write scripts with
 * 
 * @author BadGamesInc
*/
public class ScriptPlayer {

	public void sendPacket(Packet<?> packet) {
		mc.player.networkHandler.sendPacket(packet);
	}
	
	public BlockPos getBlockPos() {
		return mc.player.getBlockPos();
	}
	
	
	// Use our Vector3d because minecraft's gets obfuscated
	public Vector3D getPos() {
		return new Vector3D(mc.player.getPos());
	}
	
	public double getX() {
		return mc.player.getX();
	}
	
	public double getY() {
		return mc.player.getY();
	}
	
	public double getZ() {
		return mc.player.getZ();
	}
	
	public float getYaw() {
		return mc.player.getYaw();
	}
	
	public float getPitch() {
		return mc.player.getPitch();
	}
	
	public void setYaw(float yaw) {
		mc.player.setYaw(yaw);
	}
	
	public void setPitch(float pitch) {
		mc.player.setPitch(pitch);
	}
	
	public void setFlying(boolean flying) {
		mc.player.getAbilities().allowFlying = flying;
		mc.player.getAbilities().flying = flying;
	}
	
	public void setFlySpeed(float speed) {
		mc.player.getAbilities().setFlySpeed(speed);
	}
	
	public boolean isCollidedHorizontally() {
		return mc.player.horizontalCollision;
	}
	
	public boolean isCollidedVertically() {
		return mc.player.verticalCollision;
	}
	
	public float getFallDistance() {
		return mc.player.fallDistance;
	}
	
	public boolean isUsingElytra() {
		return mc.player.isFallFlying();
	}
	
	public void setUsingElytra(boolean using) {
		if (using && !isUsingElytra()) mc.player.startFallFlying();
		else mc.player.stopFallFlying();
	}
	
	public void stopUsingItem() {
		mc.player.stopUsingItem();
	}
	
	public void useItem() {
		mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
	}
	
	public float getHurtTime() {
		return mc.player.hurtTime;
	}
	
	public int getGamemode() {
		return mc.interactionManager.getCurrentGameMode().getId();
	}
	
	public Vector3D getVelocity() {
		return new Vector3D(mc.player.getVelocity());
	}
	
	public void setVelocity(Vector3D velocity) {
		mc.player.setVelocity(new Vec3d(velocity.getX(), velocity.getY(), velocity.getZ()));
	}
	
	public void setVelocity(double x, double y, double z) {
		mc.player.setVelocity(x, y, z);
	}
	
	public float getAirStrafingSpeed() {
		return mc.player.airStrafingSpeed;
	}
	
	public void setAirStrafingSpeed(float speed) {
		mc.player.airStrafingSpeed = speed;
	}
	
	public String getName() {
		return mc.player.getName().asString();
	}
}
