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

import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

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
}
