package dev.hypnotic.hypnotic_client.event.events;

import dev.hypnotic.hypnotic_client.event.Event;
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
