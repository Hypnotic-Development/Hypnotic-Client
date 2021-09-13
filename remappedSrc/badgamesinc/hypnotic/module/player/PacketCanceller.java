package badgamesinc.hypnotic.module.player;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventEntity;
import badgamesinc.hypnotic.event.events.EventSendPacket;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;

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
