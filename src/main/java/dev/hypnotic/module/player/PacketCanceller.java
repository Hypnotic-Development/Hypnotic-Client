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
package dev.hypnotic.module.player;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventEntity;
import dev.hypnotic.event.events.EventSendPacket;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;

public class PacketCanceller extends Mod {

	public PacketCanceller() {
		super("PacketCanceller", "stop packet", Category.PLAYER);
	}
	
	@EventTarget
	public void sendPackets(EventSendPacket event) {
		event.setCancelled(true);
	}

	@EventTarget
	public void leaveGame(EventEntity.Remove event) {
		if (event.getEntity() == mc.player) this.toggle();
	}
}
