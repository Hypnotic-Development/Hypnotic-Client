package badgamesinc.hypnotic.module.player;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventReceivePacket;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.utils.Wrapper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;

public class EntityDesync extends Mod {

	Entity ridingEntity;
	
	public EntityDesync() {
		super("EntityDesync", "does stuff", Category.PLAYER);
	}

	@Override
	public void onEnable() {
		if (mc.player != null) {
			if (!mc.player.isRiding()) {
				ridingEntity = mc.player.getVehicle();
				mc.player.dismountVehicle();
				mc.world.removeEntity(ridingEntity.getId(), RemovalReason.UNLOADED_TO_CHUNK);
			} else {
				Wrapper.tellPlayer("you need to be riding something dipshit");
				ridingEntity = null;
				this.toggle();
			}
		} else {
			ridingEntity = null;
			this.toggle();
			return;
		}
		super.onEnable();
	}
	
	@Override
	public void onTick() {
		if (ridingEntity == null) return;
//		mc.player.setOnGround(true);
		ridingEntity.setPos(mc.player.getX(), mc.player.getY(), mc.player.getZ());
		mc.player.networkHandler.sendPacket(new VehicleMoveC2SPacket(ridingEntity));
		super.onTick();
	}
	
	@EventTarget
	public void entityEvent(EventReceivePacket event) {
		if (event.getPacket() instanceof ClientCommandC2SPacket) {
			ClientCommandC2SPacket packet = (ClientCommandC2SPacket)event.getPacket();
			if (packet.getMode() == Mode.PRESS_SHIFT_KEY) {
				Wrapper.tellPlayer("Dismounted");
				this.toggle();
			}
		}
	}
	
	@Override
	public void onDisable() {
		if (ridingEntity != null) {
			if (mc.player.isRiding()) {
				mc.world.spawnEntity(ridingEntity);
				mc.player.startRiding(ridingEntity, true);
			}
			ridingEntity = null;
			Wrapper.tellPlayer("Re-mounted");
		}
		super.onDisable();
	}
}
