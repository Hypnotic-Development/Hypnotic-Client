package dev.hypnotic.hypnotic_client.module.player;

import dev.hypnotic.hypnotic_client.event.EventTarget;
import dev.hypnotic.hypnotic_client.event.events.EventEntity;
import dev.hypnotic.hypnotic_client.event.events.EventSendPacket;
import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;

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
