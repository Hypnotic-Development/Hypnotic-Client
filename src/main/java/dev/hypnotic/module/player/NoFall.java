package dev.hypnotic.module.player;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventSendPacket;
import dev.hypnotic.mixin.PlayerMoveC2SPacketAccessor;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.utils.ColorUtils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;

public class NoFall extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Packet", "Packet");
	
    public NoFall() {
        super("NoFall", "Prevents you from taking fall damage", Category.PLAYER);
        addSetting(mode);
    }

    @Override
    public void onTick() {
    	this.setDisplayName("NoFall " + ColorUtils.gray + mode.getSelected());
        super.onTick();
    }
    
    @EventTarget
    private void onSendPacket(EventSendPacket event) {
    	if (mc.player == null) return;
        if (mc.player.getAbilities().creativeMode
            || !(event.getPacket() instanceof PlayerMoveC2SPacket) || mc.player.isOnGround() || mc.player.fallDistance < 2.5) return;


        if ((mc.player.isFallFlying()) && mc.player.getVelocity().y < 1) {
            BlockHitResult result = mc.world.raycast(new RaycastContext(
                mc.player.getPos(),
                mc.player.getPos().subtract(0, 0.5, 0),
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                mc.player)
            );

            if (result != null && result.getType() == HitResult.Type.BLOCK) {
                ((PlayerMoveC2SPacketAccessor) event.getPacket()).setOnGround(true);
            }
        }
        else {
            ((PlayerMoveC2SPacketAccessor) event.getPacket()).setOnGround(true);
        }
    }
}
