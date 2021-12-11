package dev.hypnotic.hypnotic_client.event.events;

import dev.hypnotic.hypnotic_client.event.Event;
import net.minecraft.util.math.BlockPos;

public class EventDestroyBlock extends Event {
    private final BlockPos pos;

    public EventDestroyBlock(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }
}
