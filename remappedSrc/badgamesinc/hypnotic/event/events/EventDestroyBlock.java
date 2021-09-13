package badgamesinc.hypnotic.event.events;

import badgamesinc.hypnotic.event.Event;
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
