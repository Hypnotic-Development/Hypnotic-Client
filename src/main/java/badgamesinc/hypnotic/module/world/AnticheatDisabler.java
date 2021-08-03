package badgamesinc.hypnotic.module.world;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventReceivePacket;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;

public class AnticheatDisabler extends Mod {
    public AnticheatDisabler() {
        super("AnticheatDisabler", "Disables crappy anticheats", Category.WORLD);
    }

    @EventTarget
    public void onReceivePacket(EventReceivePacket event) {
        if (mc.player != null && mc.world != null && mc.player.age <= 40) {
            if (event.getPacket() instanceof PlayerPositionLookS2CPacket) {
                event.setCancelled(true);
            }

        }
    }
}
