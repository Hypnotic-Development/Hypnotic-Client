package badgamesinc.hypnotic.module.render;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventMove;
import badgamesinc.hypnotic.event.events.EventPushOutOfBlocks;
import badgamesinc.hypnotic.event.events.EventSendPacket;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.RotationVector;
import badgamesinc.hypnotic.utils.player.FakePlayerEntity;
import badgamesinc.hypnotic.utils.player.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Freecam extends Mod {

	public NumberSetting speed = new NumberSetting("Speed", 1, 0, 10, 0.1);
	public BooleanSetting stealth = new BooleanSetting("No Interact", true);
	
	private Vec3d savedCoords = Vec3d.ZERO;
    private RotationVector lookVec = new RotationVector(0, 0);
    public static PlayerEntity playerEntity;
    
	public Freecam() {
		super("Freecam", "Leave your body and explore", Category.RENDER);
		addSettings(speed, stealth);
	}
    
    @EventTarget
    public void sendPacket(EventSendPacket event) {
    	if (stealth.isEnabled() && mc.world != null && mc.player.age > 100) {
            if (!(event.getPacket() instanceof KeepAliveC2SPacket || event.getPacket() instanceof ChatMessageC2SPacket || event.getPacket() instanceof PlayerInteractBlockC2SPacket || event.getPacket() instanceof PlayerInteractEntityC2SPacket || event.getPacket() instanceof PlayerActionC2SPacket))
            	event.setCancelled(true);
        } else if (event.getPacket() instanceof PlayerMoveC2SPacket) {
            PlayerMoveC2SPacket playerMoveC2SPacket = new PlayerMoveC2SPacket.Full(savedCoords.getX(), savedCoords.getY(), savedCoords.getZ(), lookVec.getYaw(), lookVec.getPitch(), true);
            event.setPacket(playerMoveC2SPacket);
        }
    }
    
    @EventTarget
    public void move(EventMove event) {
    	if (!PlayerUtils.isMoving()) {
    		event.setX(0);
            event.setZ(0);
        } else {
            PlayerUtils.setMoveSpeed(event, speed.getValue());
        }
    	event.setY(0);
        if (mc.options.keySneak.isPressed())
        	event.setY(-speed.getValue());
        if (mc.options.keyJump.isPressed())
        	event.setY(speed.getValue());
    }
    
    @EventTarget
    public void pushOutOfBlocks(EventPushOutOfBlocks event) {
    	event.setCancelled(true);
    }
    
    @Override
    public void onTick() {
//    	mc.player.handSwingProgress += 400.0F;
        mc.player.noClip = true;
    	super.onTick();
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            mc.worldRenderer.reload();
            savedCoords = new Vec3d(mc.player.getX(), mc.player.getY(), mc.player.getZ());
            lookVec = new RotationVector(mc.player);

            playerEntity = new FakePlayerEntity(mc.world, new GameProfile(UUID.randomUUID(), mc.getSession().getUsername()));
            playerEntity.copyFrom(mc.player);
            playerEntity.copyPositionAndRotation(mc.player);
            mc.world.addEntity(69420, playerEntity);
        }
        super.onEnable();
    }


    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player != null) {
            mc.player.noClip = false;
            mc.worldRenderer.reload();
            mc.player.setPos(savedCoords.getX(), savedCoords.getY(), savedCoords.getZ());
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(savedCoords.getX(), savedCoords.getY(), savedCoords.getZ(), false));
            if (!mc.isInSingleplayer())
                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(savedCoords.getX(), savedCoords.getY(), savedCoords.getZ(), true));
            mc.player.setYaw(lookVec.getYaw());
            mc.player.setPitch(lookVec.getPitch());
        }
        savedCoords = Vec3d.ZERO;
        if (playerEntity != null) {
            playerEntity.setPos(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            if (mc.world != null)
                mc.world.removeEntity(playerEntity.getId(), Entity.RemovalReason.DISCARDED);
            playerEntity = null;
        }
    }

}
