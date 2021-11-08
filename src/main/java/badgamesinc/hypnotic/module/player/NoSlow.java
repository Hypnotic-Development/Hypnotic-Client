package badgamesinc.hypnotic.module.player;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventMotionUpdate;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.combat.Killaura;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class NoSlow extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "NCP");
	boolean blocking = false;
	
	public NoSlow() {
        super("NoSlow", "Prevents items from slowing you down", Category.PLAYER);
        addSettings(mode);
    }

    @Override
    public void onTick() {
    	
    	super.onTick();
    }
    
    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event) {
    	if (mc.player.isOnGround()) {
	    	if (event.isPre()) {
	        	if (mc.player.isBlocking() && mode.is("NCP") && Killaura.target == null) {
	        		mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
	        		blocking = false;
	        	}
	        } else {
	            if (mc.player.isBlocking() && mode.is("NCP") && Killaura.target == null && !blocking) {
	                mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(new Vec3d(0.0f, 0.0f, 0.0f), Direction.DOWN, new BlockPos(-1, -1, -1), false)));
	                mc.interactionManager.interactItem(mc.player, mc.world, Hand.OFF_HAND);
					mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
					blocking = true;
	            }
	        }
    	}
    	this.setDisplayName("NoSlow " + ColorUtils.gray + mode.getSelected());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
