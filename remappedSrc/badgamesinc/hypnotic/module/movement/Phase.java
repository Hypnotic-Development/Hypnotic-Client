package badgamesinc.hypnotic.module.movement;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventCollide;
import badgamesinc.hypnotic.event.events.EventPushOutOfBlocks;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class Phase extends Mod {

	public Phase() {
		super("Phase", "Go through da walls", Category.PLAYER);
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@Override
	public void onTick() {
//		this.setEnabled(false);
		/*if (mc.options.keySneak.isPressed()) {
			mc.player.setSneaking(false);
//			mc.player.noClip = true;
//			mc.player.setBoundingBox(new Box(0, 0, 0, 0, 0, 0));
			
			if (mc.player.horizontalCollision) {
				BlockPos pos = WorldUtils.getForwardBlock(1);
				mc.player.updatePosition(pos.getX(), pos.getY(), pos.getZ());
			}
//			mc.player.setVelocity(mc.player.getVelocity().x * 3, 0, mc.player.getVelocity().z * 3);
			
		}*/
		if (mc.player.horizontalCollision && mc.options.keySneak.isPressed()) {
			Vec3i v31 = mc.player.getMovementDirection().getVector();
	        Vec3d v3 = new Vec3d(v31.getX(), 0, v31.getZ());
	        for (double o = 2; o < 100; o++) {
	            Vec3d coff = v3.multiply(o);
	            BlockPos cpos = mc.player.getBlockPos().add(new Vec3i(coff.x, coff.y, coff.z));
	            BlockState bs1 = mc.world.getBlockState(cpos);
	            BlockState bs2 = mc.world.getBlockState(cpos.up());
	            if (!bs1.getMaterial().blocksMovement() && !bs2.getMaterial().blocksMovement() && bs1.getBlock() != Blocks.LAVA && bs2.getBlock() != Blocks.LAVA) {
	                mc.player.updatePosition(cpos.getX() + 0.5, cpos.getY(), cpos.getZ() + 0.5);
	                mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
	                break;
	            }
	        }
		}
		super.onTick();
	}
	
	@EventTarget
	public void onPushOutOfBlocks(EventPushOutOfBlocks event) {
		event.setCancelled(true);
	}
	
	@EventTarget
	public void onCollision(EventCollide.Block event) {
		
	}
	
	@Override
	public void onDisable() {
		super.onDisable();
	}
}
