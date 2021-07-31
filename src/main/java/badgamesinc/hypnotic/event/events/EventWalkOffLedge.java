package badgamesinc.hypnotic.event.events;

import badgamesinc.hypnotic.event.Event;

public class EventWalkOffLedge extends Event {

	public boolean isSneaking;

    public EventWalkOffLedge(boolean isSneaking) {
        this.isSneaking = isSneaking;
    }
}
