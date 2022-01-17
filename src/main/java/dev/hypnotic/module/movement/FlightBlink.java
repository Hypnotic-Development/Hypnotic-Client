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
package dev.hypnotic.module.movement;

import java.util.ArrayList;

import dev.hypnotic.event.EventManager;
import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventSendPacket;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.utils.player.PlayerUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;

public class FlightBlink extends Mod {

	@SuppressWarnings("rawtypes")
	private ArrayList<Packet> packets = new ArrayList<>();
	public static PlayerEntity playerEntity;
	private boolean stopCatching;
	
    public FlightBlink() {
        super("FlightBlink", "Used for blink setting in flight", Category.MOVEMENT);
        this.visible.setEnabled(false);
    }
    
    @Override
    public void onEnable() {
    	stopCatching = false;

    	super.onEnable();
    }

    @Override
    public void onTick() {
    	this.visible.setEnabled(false);
        super.onTick();
    }
    
    @EventTarget
    private void onSendPacket(EventSendPacket event) {
    	if (mc.player == null || (packets.isEmpty() && stopCatching)) {
			packets.clear();
			this.setEnabled(false);
			return;
		}
		if (!stopCatching && !(event.getPacket() instanceof KeepAliveC2SPacket)) {
			if (PlayerUtils.isMoving()) {
				packets.add(event.getPacket());
			}
			event.setCancelled(true);
		}
    }
    
    @Override
    public void onDisable() {
    	stopCatching = true;
//		if (!buffer.isEnabled())
//			packets.forEach(packet -> {
//				mc.getNetworkHandler().sendPacket(packet);
//			});
		packets.clear();
    	super.onDisable();
    }
    
    @Override
    public void toggle() {
    	enabled = !enabled;
		if(enabled) {
			onEnable();
			EventManager.INSTANCE.register(this);
		} else {
			onDisable();
			EventManager.INSTANCE.unregister(this);
		}
    }
}

