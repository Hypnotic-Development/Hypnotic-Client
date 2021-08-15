package badgamesinc.hypnotic.module.player;

import java.util.ArrayList;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventSendPacket;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.player.FakePlayerInstance;
import badgamesinc.hypnotic.utils.player.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Blink extends Mod {

	public BooleanSetting buffer = new BooleanSetting("Buffer Packets", true);
	public NumberSetting amount = new NumberSetting("Send Amount PT", 25, 5, 50, 1);
	
	private ArrayList<PlayerMoveC2SPacket> packets = new ArrayList<>();
	public static PlayerEntity playerEntity;
	private boolean stopCatching;
	
    public Blink() {
        super("Blink", "Spoofs extreme lag so you visiblly teleport", Category.PLAYER);
        addSettings(buffer, amount);
    }
    
    @Override
    public void onEnable() {
    	stopCatching = false;
    	if (mc.player != null) {
    		playerEntity = new FakePlayerInstance(mc.world, new GameProfile(UUID.randomUUID(), mc.player.getName().asString()));
    		playerEntity.copyFrom(mc.player);
    		playerEntity.copyPositionAndRotation(mc.player);
			mc.world.addEntity(1000000, playerEntity);
		}
    	super.onEnable();
    }

    @Override
    public void onTick() {
    	if (stopCatching && !packets.isEmpty()) {
			for (int i = 0; i < amount.getValue(); i++) {
				mc.getNetworkHandler().sendPacket(packets.get(i));
			}
		}
        super.onTick();
    }
    
    @EventTarget
    private void onSendPacket(EventSendPacket event) {
    	if (mc.player == null || (packets.isEmpty() && stopCatching)) {
			packets.clear();
			this.setEnabled(false);
			return;
		}
		if (!stopCatching && event.getPacket() instanceof PlayerMoveC2SPacket) {
			if (PlayerUtils.isMoving()) {
				packets.add((PlayerMoveC2SPacket) event.getPacket());
			}
			event.setCancelled(true);;
		}
    }
    
    @Override
    public void onDisable() {
    	stopCatching = true;
		if (!buffer.isEnabled() || packets.isEmpty())
			super.onDisable();
		if (!buffer.isEnabled())
			packets.forEach(packet -> {
				mc.getNetworkHandler().sendPacket(packet);
			});
		packets.clear();
    	if (playerEntity != null) {
    		playerEntity.setPos(0, Double.NEGATIVE_INFINITY, 0);
    		if (mc.world != null) mc.world.removeEntity(1000000, Entity.RemovalReason.DISCARDED);
    		playerEntity = null;
    	}
    	super.onDisable();
    }
}

