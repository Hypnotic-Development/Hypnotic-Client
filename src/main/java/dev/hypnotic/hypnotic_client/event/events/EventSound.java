package dev.hypnotic.hypnotic_client.event.events;

import dev.hypnotic.hypnotic_client.event.Event;
import net.minecraft.client.sound.SoundInstance;

public class EventSound extends Event {

    public SoundInstance sound;

    public EventSound(SoundInstance sound) {
    	this.sound = sound;
    }
}
