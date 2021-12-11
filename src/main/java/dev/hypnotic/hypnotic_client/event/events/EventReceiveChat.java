package dev.hypnotic.hypnotic_client.event.events;

import dev.hypnotic.hypnotic_client.event.Event;
import net.minecraft.text.Text;

public class EventReceiveChat extends Event {

	private Text message;
	private int id;
	
	public EventReceiveChat(Text text, int id) {
		this.message = text;
		this.id = id;
	}
	
	public Text getMessage() {
		return message;
	}
	
	public int getId() {
		return id;
	}
}
