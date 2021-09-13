package badgamesinc.hypnotic.event.events;

import badgamesinc.hypnotic.event.Event;
import net.minecraft.util.Hand;

public class EventSwingHand extends Event {

	private Hand hand;
	
	public EventSwingHand(Hand hand) {
		this.hand = hand;
	}
	
	public Hand getHand() {
		return hand;
	}
}
