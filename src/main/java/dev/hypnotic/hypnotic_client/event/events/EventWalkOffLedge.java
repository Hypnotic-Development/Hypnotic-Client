package dev.hypnotic.hypnotic_client.event.events;

import dev.hypnotic.hypnotic_client.event.Event;

public class EventWalkOffLedge extends Event {

	public boolean isSneaking;

    public EventWalkOffLedge(boolean isSneaking) {
        this.isSneaking = isSneaking;
    }
}
