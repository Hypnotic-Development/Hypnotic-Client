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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.network.packet.c2s.play.PlayerInputC2SPacket;

@Mixin(PlayerInputC2SPacket.class)
public interface PlayerInputC2SPacketAccessor {

	@Accessor
    @Mutable
    void setForward(float forward);
	
	@Accessor
    @Mutable
    void setSideways(float sideways);
	
	@Accessor
    @Mutable
    void setJumping(boolean jumping);
	
	@Accessor
    @Mutable
    void setSneaking(boolean sneaking);
}
