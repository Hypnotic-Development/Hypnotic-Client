package dev.hypnotic.event.events;

import dev.hypnotic.event.Event;
import net.minecraft.client.sound.SoundInstance;

public class EventSound extends Event {

    public SoundInstance sound;

    public EventSound(SoundInstance sound) {
    	this.sound = sound;
    }
}
