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

import java.util.Random;

import dev.hypnotic.utils.render.Vector3D;
import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
* @author BadGamesInc
*/
public class Packets {

	public PlayerMoveC2SPacket.Full playerMove(double x, double y, double z, float yaw, float pitch, boolean onGround) {
		return new PlayerMoveC2SPacket.Full(x, y, z, yaw, pitch, onGround);
	}
	
	public PlayerInputC2SPacket playerInput(float sideways, float forward, boolean jumping, boolean sneaking) {
		return new PlayerInputC2SPacket(sideways, forward, jumping, sneaking);
	}
	
	public PlayerInteractItemC2SPacket playerInteractItem(String handName) {
		Hand hand = Enum.valueOf(Hand.class, handName.toUpperCase());
		return new PlayerInteractItemC2SPacket(hand);
	}
	
	public PlayerInteractBlockC2SPacket playerInteractBlock(String handName, Vector3D pos, Vector3D blockPos, String directionName) {
		// Check if it is main or off hand, if neither just return the main hand
		Hand hand = handName.equalsIgnoreCase("main") ? Hand.MAIN_HAND : (handName.equalsIgnoreCase("off") ? Hand.OFF_HAND : Hand.MAIN_HAND);
		Direction direction = Direction.DOWN;
		
		switch (directionName.toUpperCase()) {
			case "DOWN": direction = Direction.DOWN; break;
			case "UP": direction = Direction.UP; break;
			case "EAST": direction = Direction.EAST; break;
			case "WEST": direction = Direction.WEST; break;
			case "NORTH": direction = Direction.NORTH; break;
			case "SOUTH": direction = Direction.SOUTH; break;
			case "RANDOM": direction = Direction.random(new Random()); break;
		}
		
		BlockHitResult hit = new BlockHitResult(pos.toMinecraft(), direction, new BlockPos(blockPos.toMinecraft()), false);
		return new PlayerInteractBlockC2SPacket(hand, hit);
	}
}
