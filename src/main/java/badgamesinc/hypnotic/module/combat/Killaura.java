package badgamesinc.hypnotic.module.combat;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import badgamesinc.hypnotic.config.friends.FriendManager;
import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventMotionUpdate;
import badgamesinc.hypnotic.event.events.EventRender3D;
import badgamesinc.hypnotic.event.events.EventSendPacket;
import badgamesinc.hypnotic.mixin.PlayerMoveC2SPacketAccessor;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.player.Scaffold;
import badgamesinc.hypnotic.module.render.OldBlock;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.RotationUtils;
import badgamesinc.hypnotic.utils.Timer;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Killaura extends Mod {

	public static LivingEntity target;
	public ModeSetting mode = new ModeSetting("Mode", "Sort", "Sort", "Multi");
	public ModeSetting sortMode = new ModeSetting("Sort Mode", "Distance", "Distance", "Health");
	public ModeSetting rotation = new ModeSetting("Rotations", "Silent", "Silent", "Lock View");
	public NumberSetting range = new NumberSetting("Range", 4, 1, 6, 0.1);
	public NumberSetting minAps = new NumberSetting("Min APS", 10, 1, 20, 1);
	public NumberSetting maxAps = new NumberSetting("Max APS", 15, 1, 20, 1);
	public BooleanSetting delay = new BooleanSetting("1.9 Delay", true);
	public BooleanSetting random = new BooleanSetting("Random Extra Delay", false);
	public BooleanSetting swing = new BooleanSetting("Swing", true);
	public BooleanSetting autoBlock = new BooleanSetting("AutoBlock", true);
	public ModeSetting autoBlockMode = new ModeSetting("AutoBlock Mode", "Normal", "Normal", "NCP", "Visual");
	public BooleanSetting esp = new BooleanSetting("ESP", true);
	public ColorSetting espColor = new ColorSetting("ESP Color", ColorUtils.pingle);
	public BooleanSetting trigger = new BooleanSetting("Trigger", false);
	public BooleanSetting walls = new BooleanSetting("Walls", true);
	public BooleanSetting players = new BooleanSetting("Players", true);
	public BooleanSetting animals = new BooleanSetting("Animals", false);
	public BooleanSetting monsters = new BooleanSetting("Monsters", false);
	public BooleanSetting passives = new BooleanSetting("Passives", false);
	public BooleanSetting invisibles = new BooleanSetting("Invisibles", false);
	
	private static Timer attackTimer = new Timer();
	
	int passedTicks;
	boolean blocking = false;
	
	public Killaura() {
		super("Killaura", "Attacks select surrounding entities", Category.COMBAT);
		addSettings(mode, sortMode, rotation, range, minAps, maxAps, delay, random, swing, autoBlock, autoBlockMode, esp, espColor, trigger, walls, players, animals, monsters, passives, invisibles);
	}
	
	@EventTarget
	public void onMotionUpdate(EventMotionUpdate event) {
		if (maxAps.getValue() <= minAps.getValue()) maxAps.setValue(minAps.getValue() + 1);
		if (event.isPre()) {
			try{
				if(mc.world != null){
					List<LivingEntity> targets = Lists.<LivingEntity>newArrayList();
					for(Entity e : mc.world.getEntities()){
						if (e instanceof LivingEntity && e != mc.player && !FriendManager.INSTANCE.isFriend((LivingEntity)e) && mc.player.distanceTo(e) <= range.getValue() && canAttack((LivingEntity)e)) targets.add((LivingEntity)e);
						else {
							if (targets.contains(e)) targets.remove(e);
						}
					}
					if (target != null && mc.player.distanceTo(target) > range.getValue()) {
						targets.remove(target);
						target = null;
						if(!ModuleManager.INSTANCE.getModule(Scaffold.class).isEnabled()){
							RotationUtils.resetPitch();
							RotationUtils.resetYaw();
						}
					}
					switch(mode.getSelected()) {
						case "Sort":
							switch(sortMode.getSelected()) {
								case "Distance": 
									targets.sort(Comparator.comparingDouble(entity -> mc.player.distanceTo(entity)));
									break;
								case "Health": 
									targets.sort(Comparator.comparingDouble(entity -> ((LivingEntity)entity).getHealth()));
									break;
							}
							if(!targets.isEmpty()){
								if (!FriendManager.INSTANCE.isFriend((LivingEntity)targets.get(0))) target = (LivingEntity)targets.get(0);
								if (mc.player.distanceTo(target) > range.getValue()) target = null;
								if(target != null){
									this.setDisplayName("Killaura " + ColorUtils.gray + (target instanceof PlayerEntity ? target.getName().asString().replaceAll(ColorUtils.colorChar, "&") : target.getDisplayName().asString()));
									if(canAttack(target)){
										RotationUtils.setSilentPitch(RotationUtils.getRotations(target)[1]);
										RotationUtils.setSilentYaw(RotationUtils.getRotations(target)[0]);
										if (rotation.is("Lock View")) {
											mc.player.setYaw(RotationUtils.getRotations(target)[0]);
											mc.player.setPitch(RotationUtils.getRotations(target)[1]);
										}
										long aps = minAps.getValue() < 20 ? new Random().nextInt((int) (maxAps.getValue() - minAps.getValue())) + (int) minAps.getValue() : 20;
										if (delay.isEnabled() && random.isEnabled() && mc.player.getAttackCooldownProgress(0.5F) != 1) attackTimer.reset();
										if(delay.isEnabled() ? mc.player.getAttackCooldownProgress(0.5F) == 1 && (random.isEnabled() ? attackTimer.hasTimeElapsed(new Random().nextInt(300 - 100) + 100, true) : true) : attackTimer.hasTimeElapsed((long) (1000L / aps), true)){
											if(autoBlock.isEnabled() && mc.player.getOffHandStack().getItem() instanceof ShieldItem && (mc.player.getMainHandStack().getItem() instanceof ToolItem || mc.player.getMainHandStack().getItem() == Items.AIR || mc.player.getMainHandStack().getItem() instanceof AxeItem || mc.player.getMainHandStack().getItem() instanceof SwordItem) && autoBlockMode.is("Normal")) {
												mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
											}
											blocking = false;
											mc.interactionManager.attackEntity(mc.player, target);
											if (swing.isEnabled() && (autoBlock.isEnabled() ? !ModuleManager.INSTANCE.getModule(OldBlock.class).isEnabled() : true) || (ModuleManager.INSTANCE.getModule(OldBlock.class).isEnabled() ? mc.options.getPerspective() != Perspective.FIRST_PERSON : false) && !(mc.player.getMainHandStack().getItem() instanceof SwordItem)) mc.player.swingHand(Hand.MAIN_HAND);
											else mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
											if (swing.isEnabled() && !(mc.player.getMainHandStack().getItem() instanceof SwordItem) || ModuleManager.INSTANCE.getModule(OldBlock.class).animation.is("Swing")) mc.player.swingHand(Hand.MAIN_HAND); 
										}
									}
									if (target.isDead()) {
										RotationUtils.resetYaw();
										RotationUtils.resetPitch();
									}
								}else{
									if (blocking) mc.options.keyUse.setPressed(false);
									blocking = false;
									if(!ModuleManager.INSTANCE.getModule(Scaffold.class).isEnabled()){
										RotationUtils.resetPitch();
										RotationUtils.resetYaw();
									}
									this.setDisplayName("Killaura " + ColorUtils.gray + "None");
								}
							} else {
								if (blocking) mc.options.keyUse.setPressed(false);
								blocking = false;
								if(!ModuleManager.INSTANCE.getModule(Scaffold.class).isEnabled()){
									RotationUtils.resetPitch();
									RotationUtils.resetYaw();
								}
							}
							break;
						case "Multi":
							if(!targets.isEmpty()){
								for (LivingEntity entity : targets) {
									if(entity != null && !FriendManager.INSTANCE.isFriend(entity)){
										this.setDisplayName("Killaura " + ColorUtils.gray + (entity instanceof PlayerEntity ? entity.getName().asString().replaceAll(ColorUtils.colorChar, "&") : entity.getDisplayName().asString()));
										if(canAttack(entity)){
										 	if (target != null) RotationUtils.setSilentPitch(RotationUtils.getRotations(target)[1]);
										 	if (target != null) RotationUtils.setSilentYaw(RotationUtils.getRotations(target)[0]);
											if (rotation.is("Lock View")) {
												if (target != null) mc.player.setYaw(RotationUtils.getRotations(target)[0]);
												if (target != null) mc.player.setPitch(RotationUtils.getRotations(target)[1]);
											}
											long aps = minAps.getValue() < 20 ? new Random().nextInt((int) (maxAps.getValue() - minAps.getValue())) + (int) minAps.getValue() : 20;
											if (delay.isEnabled() && random.isEnabled() && mc.player.getAttackCooldownProgress(0.5F) != 1) attackTimer.reset();
											if ((delay.isEnabled() ? mc.player.getAttackCooldownProgress(0.5F) == 1 && (random.isEnabled() ? attackTimer.hasTimeElapsed(new Random().nextInt(300 - 100) + 100, true) : true) : attackTimer.hasTimeElapsed((long) (1000L / aps), true)) && targets.indexOf(entity) == new Random().nextInt(targets.size())) {
													if(autoBlock.isEnabled() && mc.player.getOffHandStack().getItem() instanceof ShieldItem && (mc.player.getMainHandStack().getItem() instanceof ToolItem || mc.player.getMainHandStack().getItem() == Items.AIR || mc.player.getMainHandStack().getItem() instanceof AxeItem || mc.player.getMainHandStack().getItem() instanceof SwordItem) && autoBlockMode.is("NCP")) {
														mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
													}
													blocking = false;
													target = entity;
													mc.interactionManager.attackEntity(mc.player, entity);
													if (swing.isEnabled() && (autoBlock.isEnabled() ? !ModuleManager.INSTANCE.getModule(OldBlock.class).isEnabled() : true) || (ModuleManager.INSTANCE.getModule(OldBlock.class).isEnabled() ? mc.options.getPerspective() != Perspective.FIRST_PERSON : false) && !(mc.player.getMainHandStack().getItem() instanceof SwordItem)) mc.player.swingHand(Hand.MAIN_HAND);
													else mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
													if (swing.isEnabled() && !(mc.player.getMainHandStack().getItem() instanceof SwordItem) || ModuleManager.INSTANCE.getModule(OldBlock.class).animation.is("Swing")) mc.player.swingHand(Hand.MAIN_HAND); 
												}
										}
										if (entity.isDead()) {
											RotationUtils.resetYaw();
											RotationUtils.resetPitch();
										}
									}else{
										
									}
								}
							} else {
								if(!ModuleManager.INSTANCE.getModule(Scaffold.class).isEnabled()){
									RotationUtils.resetPitch();
									RotationUtils.resetYaw();
								}
							}
						break;
					}
					if (targets.isEmpty()) target = null;
					this.setDisplayName("Killaura " + ColorUtils.gray + mode.getSelected());
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		} else {
			if (target == null) return;
			if(autoBlock.isEnabled() && (mc.player.getMainHandStack().getItem() instanceof ToolItem || mc.player.getMainHandStack().getItem() == Items.AIR || mc.player.getMainHandStack().getItem() instanceof AxeItem || mc.player.getMainHandStack().getItem() instanceof SwordItem) && !autoBlockMode.is("Visual")) {
				mc.interactionManager.interactItem(mc.player, mc.world, Hand.OFF_HAND);
				mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
				if (!blocking && autoBlockMode.is("NCP")) {
					mc.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(new Vec3d(0.0f, 0.0f, 0.0f), Direction.DOWN, new BlockPos(-1, -1, -1), false)));
					blocking = true;
				}
			}
		}
	}
	
	@Override
	public void onDisable() {
		RotationUtils.resetPitch();
		RotationUtils.resetYaw();
		target = null;
		super.onDisable();
	}
	
	@EventTarget
	public void sendPacket(EventSendPacket event) {
		if (event.getPacket() instanceof PlayerMoveC2SPacket) {
			if (target != null && rotation.is("Silent")) {
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setYaw(RotationUtils.getRotations(target)[0]);
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch(RotationUtils.getRotations(target)[1]);
			} else {
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setYaw(mc.player.getYaw());
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch(mc.player.getPitch());
			}
		}
	}
	
	private boolean canAttack(LivingEntity entity) {
		if (entity != mc.player && mc.player.distanceTo(entity) <= range.getValue() && entity.isAlive() && mc.player.isAlive()) {
				if (!trigger.isEnabled()) {
				if (players.isEnabled() && entity instanceof PlayerEntity) return true;
				if (animals.isEnabled() && entity instanceof AnimalEntity) return true;
				if (monsters.isEnabled() && entity instanceof Monster) return true;
				if (passives.isEnabled() && entity instanceof PassiveEntity && !(entity instanceof ArmorStandEntity)) return true;
				if (passives.isEnabled() && entity.isInvisible()) return true;
			} else {
				HitResult hitResult = mc.crosshairTarget;
				if (hitResult != null && hitResult.getType() == Type.ENTITY) {
					Entity entity1 = ((EntityHitResult) hitResult).getEntity();
					if (entity1 != null && entity1 == entity) return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	@Override
	public void onTickDisabled() {
		sortMode.setVisible(mode.is("Sort"));
		espColor.setVisible(esp.isEnabled());
		super.onTickDisabled();
	}
	
	double anim = 0;
	double anim2 = 0;
	boolean dir = false;
	boolean filler = false;
	
	@EventTarget
	public void render3d(EventRender3D event) {
		if (target != null) {

			if (anim > 200) {
				dir = false;
			}
			if (anim < 0) {
				dir = true;
			}
			if (dir) {
				anim+=3;
			} else {
				anim-=3;
			}

			RenderUtils.drawCircle(event.getMatrices(), new Vec3d(target.getX(), target.getY() + anim / 100, target.getZ()), event.getTickDelta(), 0.6f, 1, espColor.getColor().getRGB());
//			if (dir) {
//				for (double i = 0; i < anim / 20; i++) {
//					RenderUtils.drawCircle(event.getMatrices(), new Vec3d(target.getX(), target.getY() - (i * 0.015) + (anim / (100)) + (dir ? 0 : 0.1), target.getZ()), event.getTickDelta(), 0.6f, 1, new Color(espColor.getColor().getRed(), espColor.getColor().getGreen(), espColor.getColor().getBlue(), (255 - ((int)i * 25))).getRGB());
//				}
//			} else {
//				for (double i = 0; i < anim / 20; i++) {
//					RenderUtils.drawCircle(event.getMatrices(), new Vec3d(target.getX(), target.getY() + (i * 0.015) + (anim / (100)), target.getZ()), event.getTickDelta(), 0.6f, 1, new Color(espColor.getColor().getRed(), espColor.getColor().getGreen(), espColor.getColor().getBlue(), (255 - ((int)i * 25))).getRGB());
//				}
//			}
		} else {
			anim = 0;
		}
	}
}
