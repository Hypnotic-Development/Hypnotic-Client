package badgamesinc.hypnotic.module.combat;

import java.awt.Color;
import java.util.List;

import badgamesinc.hypnotic.config.friends.FriendManager;
import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventParticle;
import badgamesinc.hypnotic.event.events.EventRender3D;
import badgamesinc.hypnotic.event.events.EventSendPacket;
import badgamesinc.hypnotic.mixin.PlayerMoveC2SPacketAccessor;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.RotationUtils;
import badgamesinc.hypnotic.utils.player.DamageUtils;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import badgamesinc.hypnotic.utils.world.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.particle.ExplosionLargeParticle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class CrystalAura extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Risky", "Risky", "Safe", "Suicidal");
	public ModeSetting attackMode = new ModeSetting("Attack Mode", "Any", "Any", "Near Target");
	public NumberSetting attackDistance = new NumberSetting("Max Range", 4, 1, 6, 0.1);
	public NumberSetting placeDistance = new NumberSetting("Max Range", 4, 1, 6, 0.1);
	public NumberSetting delay = new NumberSetting("Place Delay", 200, 0, 2000, 10);
	public BooleanSetting autoPlace = new BooleanSetting("Auto Place", true);
	public BooleanSetting visualize = new BooleanSetting("Visualize", true);
	public BooleanSetting onlyShowPlacements = new BooleanSetting("Only Show Placements", true);
	public int thinkingColor = new Color(0, 150, 255).getRGB();
	public int placingColor = new Color(255, 0, 0).getRGB();
	BlockPos breakPos;

	private Timer timer = new Timer();
	private BlockPos placePos;
	
	public CrystalAura() {
		super("CrystalAura", "kill the people with funny crytsals", Category.COMBAT);
		addSettings(mode, attackMode, attackDistance, placeDistance, delay, autoPlace, visualize, onlyShowPlacements);
	}

	@Override
	public void onTick() {
//		if (event.getMode() == EventPlayerPackets.Mode.PRE) {
			this.setDisplayName("CrystalAura " + ColorUtils.gray + mode.getSelected());

			if (timer.hasPassed((long) delay.getValue()))
				if (autoPlace.isEnabled() && ((mc.player.getMainHandStack() != null && mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL))) {
					mc.world.getEntities().forEach(entity -> {
						if (entity instanceof PlayerEntity && entity != mc.player && !FriendManager.INSTANCE.isFriend(entity.getDisplayName().asString())) {
							PlayerEntity entityPlayer = (PlayerEntity)entity;
							BlockPos placingPos = getOpenBlockPos(entityPlayer);
							if (placingPos != null) {
								EndCrystalEntity crystal = new EndCrystalEntity(mc.world, placingPos.getX(), placingPos.getY(), placingPos.getZ());
								if (entityPlayer.distanceTo(crystal) <= 6 && mc.player.distanceTo(crystal) <= 6 && !FriendManager.INSTANCE.isFriend(entityPlayer.getName().getString()) && entityPlayer.getHealth() > 0 && shouldAttack(crystal)) {
									placePos = placingPos.down();
									timer.reset();
									return;
								}
							}
						}
					});
				}
			mc.world.getEntities().forEach(entity -> {
				if (entity instanceof EndCrystalEntity) {
					EndCrystalEntity enderCrystalEntity = (EndCrystalEntity) entity;
					if (shouldAttack(enderCrystalEntity)) {
						if (!willKillPlayer(mc.player)) {
							mc.interactionManager.attackEntity(mc.player, enderCrystalEntity);
							mc.player.swingHand(Hand.MAIN_HAND);
						} else {
							System.out.println("yes");
						}
						breakPos = enderCrystalEntity.getBlockPos();
					}
				}
			});
//		} else {
			if (placePos != null) {
				BlockHitResult blockHitResult = new BlockHitResult(new Vec3d(placePos.getX(), placePos.getY(), placePos.getZ()), Direction.UP, placePos, false);
				mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, blockHitResult));
				mc.player.swingHand(Hand.MAIN_HAND);
				placePos = null;
			}
//		}
	}

	@EventTarget
	public void render3d(EventRender3D event) {
		if (autoPlace.isEnabled() && visualize.isEnabled())
			mc.world.getEntities().forEach(entity -> {
				if (entity instanceof PlayerEntity && entity != mc.player) {
					PlayerEntity entityPlayer = (PlayerEntity) entity;
					BlockPos placingPos = getOpenBlockPos(entityPlayer);
					if (placingPos != null && !FriendManager.INSTANCE.isFriend(entityPlayer.getDisplayName().asString())) {
//						EndCrystalEntity crystal = new EndCrystalEntity(mc.world, placingPos.getX(), placingPos.getY(), placingPos.getZ());
						Vec3d renderPos = RenderUtils.getRenderPosition(placingPos.getX(), placingPos.getY(), placingPos.getZ());
						Box box = new Box(renderPos.x, renderPos.y, renderPos.z, renderPos.x + 1, renderPos.y + 1, renderPos.z + 1);
						RenderUtils.setup3DRender(true);
		                RenderUtils.drawOutlineBox(event.getMatrices(), box, new Color(255, 0, 0, 255), true);
						RenderUtils.drawFilledBox(event.getMatrices(), box, new Color(255, 0, 0, 80), true);
						RenderUtils.end3DRender();
					}
				}
			});
	}
	
	public boolean willKillPlayer(PlayerEntity entity) {
		mc.world.getEntities().forEach(e -> {
			if (e instanceof EndCrystalEntity) {
				EndCrystalEntity enderCrystalEntity = (EndCrystalEntity) e;
				DamageUtils.getInstance().crystalDamage(entity, new Vec3d(enderCrystalEntity.getBlockPos().getX(), enderCrystalEntity.getBlockPos().getY(), enderCrystalEntity.getBlockPos().getZ()), false, enderCrystalEntity.getBlockPos().down(), false);
			}
		});
		return false;
	}

	public BlockPos getOpenBlockPos(PlayerEntity entityPlayer) {
		double distance = 6;
		BlockPos closest = null;
		for (int x = -4; x < 4; x++) {
			for (int y = 0; y < 4; y++) {
				for (int z = -4; z < 4; z++) {
					BlockPos pos = new BlockPos(entityPlayer.getX() + x, (int) entityPlayer.getY() - y, entityPlayer.getZ() + z);
					EndCrystalEntity fakeCrystal = new EndCrystalEntity(mc.world, pos.getX(), pos.getY(), pos.getZ());

					List<Entity> list = mc.world.getOtherEntities((Entity) null, new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0D, pos.getY() + 2.0D, pos.getZ() + 1.0D));
					boolean collides = !list.isEmpty();
					if (WorldUtils.getBlock(pos) == Blocks.AIR && !collides && entityPlayer.canSee(fakeCrystal) && mc.player.canSee(fakeCrystal)) {
						BlockPos below = pos.down();
						Block belowBlock = WorldUtils.getBlock(below);
						if (belowBlock == Blocks.OBSIDIAN || belowBlock == Blocks.BEDROCK) {
							if (!isBlocking(pos, entityPlayer)) {
								if (onlyShowPlacements.isEnabled() && !shouldAttack(fakeCrystal))
									continue;
								double playerdist = entityPlayer.distanceTo(fakeCrystal);
								double distToMe = mc.player.distanceTo(fakeCrystal);
								if (playerdist < distance && distToMe < placeDistance.getValue()) {
									closest = pos;
									distance = playerdist;
								}
							}
						}
					}
				}
			}
		}
		return closest;
	}

	private boolean isBlocking(BlockPos blockPos, PlayerEntity EntityPlayer) {
		Box box = new Box(blockPos.up());
		if (EntityPlayer.getBoundingBox().intersects(box))
			return true;
		return false;
	}

	public boolean shouldAttack(EndCrystalEntity enderCrystalEntity) {
		float minDistance = 0;
		float range = (float) attackDistance.getValue();
		switch (mode.getSelected()) {
		case "Risky":
			minDistance = 4.5f;
			break;
		case "Safe":
			minDistance = 8;
			break;
		}

		if (mc.player.getY() <= (enderCrystalEntity.getY() - 1))
			minDistance = 0;

		if (!mc.player.canSee(enderCrystalEntity)) {
			range = 3;
			minDistance = 0;
		}

		if (attackMode.is("Any"))
			return mc.player.distanceTo(enderCrystalEntity) >= minDistance && mc.player.distanceTo(enderCrystalEntity) <= range;
		else {
			for (Entity entity : mc.world.getEntities())
				if (entity instanceof LivingEntity && isTarget((LivingEntity) entity, enderCrystalEntity)) {
					return mc.player.distanceTo(enderCrystalEntity) >= minDistance && mc.player.distanceTo(enderCrystalEntity) <= range;
				}
		}
		return false;
	}

	public boolean isTarget(LivingEntity livingEntity, EndCrystalEntity enderCrystalEntity) {
		if (livingEntity instanceof PlayerEntity && livingEntity != mc.player) {
			return !FriendManager.INSTANCE.isFriend(livingEntity.getName().getString()) && livingEntity.distanceTo(enderCrystalEntity) <= 6 && livingEntity.getHealth() > 0;
		}
		return false;
	}

	public class Timer {

	    private long currentMS = 0L;
	    private long lastMS = -1L;

	    public void update() {
	        currentMS = System.currentTimeMillis();
	    }

	    public void reset() {
	        lastMS = System.currentTimeMillis();
	    }

	    public boolean hasPassed(long MS) {
	        update();
	        return currentMS >= lastMS + MS;
	    }

	    public long getPassed() {
	        update();
	        return currentMS - lastMS;
	    }

	    public long getCurrentMS() {
	        return currentMS;
	    }

	    public long getLastMS() {
	        return lastMS;
	    }
	}
	
	@EventTarget 
	public void sendPacket(EventSendPacket event) {
//		if (event.getMode() == EventPlayerPackets.Mode.PRE) {
			this.setDisplayName("CrystalAura " + ColorUtils.gray + mode.getSelected());

			if (timer.hasPassed((long) delay.getValue()))
				if (autoPlace.isEnabled() && ((mc.player.getMainHandStack() != null && mc.player.getMainHandStack().getItem() == Items.END_CRYSTAL))) {
					mc.world.getEntities().forEach(entity -> {
						if (entity instanceof PlayerEntity && entity != mc.player && !FriendManager.INSTANCE.isFriend(entity.getDisplayName().asString())) {
							PlayerEntity entityPlayer = (PlayerEntity)entity;
							BlockPos placingPos = getOpenBlockPos(entityPlayer);
							if (placingPos != null) {
								EndCrystalEntity crystal = new EndCrystalEntity(mc.world, placingPos.getX(), placingPos.getY(), placingPos.getZ());
								if (entityPlayer.distanceTo(crystal) <= 6 && mc.player.distanceTo(crystal) <= 6 && !FriendManager.INSTANCE.isFriend(entityPlayer.getName().getString()) && entityPlayer.getHealth() > 0 && shouldAttack(crystal)) {
									float[] rotation = new float[] {(float) RotationUtils.getYaw(new Vec3d(getOpenBlockPos(entityPlayer).down().getX(), getOpenBlockPos(entityPlayer).down().getY(), getOpenBlockPos(entityPlayer).down().getZ()).add(new Vec3d(0.5, 0.5, 0.5))), (float) RotationUtils.getPitch(new Vec3d(getOpenBlockPos(entityPlayer).down().getX(), getOpenBlockPos(entityPlayer).down().getY(), getOpenBlockPos(entityPlayer).down().getZ()).add(new Vec3d(0.5, 0.5, 0.5)))};
									if (event.getPacket() instanceof PlayerMoveC2SPacket) {
										((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch(rotation[0]);
										((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch(rotation[1]);
										RotationUtils.setSilentYaw(rotation[0]);
										RotationUtils.setSilentPitch(rotation[1]);
									}
								}
							}
						}
					});
				}
			mc.world.getEntities().forEach(entity -> {
				if (entity instanceof EndCrystalEntity) {
					EndCrystalEntity enderCrystalEntity = (EndCrystalEntity) entity;
					if (shouldAttack(enderCrystalEntity)) {
						float[] rotation = RotationUtils.getRotations(enderCrystalEntity);
						if (event.getPacket() instanceof PlayerMoveC2SPacket) {
							((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch(rotation[0]);
							((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch(rotation[1]);
							RotationUtils.setSilentYaw(rotation[0]);
							RotationUtils.setSilentPitch(rotation[1]);
						}
					}
				}
			});
	}
	
	@EventTarget
	public void eventParticle(EventParticle.Normal event) {
		if (event.getParticle() instanceof ExplosionLargeParticle) event.setCancelled(true);
	}
}
