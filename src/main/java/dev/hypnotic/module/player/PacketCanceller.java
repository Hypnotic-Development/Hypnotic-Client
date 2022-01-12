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
