package badgamesinc.hypnotic.event.events;

import badgamesinc.hypnotic.event.Event;
import net.minecraft.client.sound.SoundInstance;

public class EventSound extends Event {

    public SoundInstance sound;

    public EventSound(SoundInstance sound) {
    	this.sound = sound;
    }
}
