package badgamesinc.hypnotic.event.events;

import badgamesinc.hypnotic.event.Event;

public class EventKeyPress extends Event {

	private int key;
	private int scancode;
	private int action;

	public EventKeyPress(int key, int scancode, int action) {
		this.key = key;
		this.scancode = scancode;
		this.action = action;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public int getScancode() {
		return scancode;
	}

	public void setScancode(int scancode) {
		this.scancode = scancode;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
}
