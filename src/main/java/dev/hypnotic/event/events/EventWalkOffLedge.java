package dev.hypnotic.event.events;

import dev.hypnotic.event.Event;

public class EventWalkOffLedge extends Event {

	public boolean isSneaking;

    public EventWalkOffLedge(boolean isSneaking) {
        this.isSneaking = isSneaking;
    }
}
