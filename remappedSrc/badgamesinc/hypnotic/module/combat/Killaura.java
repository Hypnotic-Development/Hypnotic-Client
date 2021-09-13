package badgamesinc.hypnotic.module.combat;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;

import badgamesinc.hypnotic.config.friends.FriendManager;
import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventSendPacket;
import badgamesinc.hypnotic.mixin.PlayerMoveC2SPacketAccessor;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.player.Scaffold;
import badgamesinc.hypnotic.module.render.OldBlock;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.RotationUtils;
import badgamesinc.hypnotic.utils.Timer;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;

public class Killaura extends Mod {

	public static LivingEntity target;
	//mecraftpvp
	public ModeSetting rotation = new ModeSetting("Rotations", "Silent", "Silent", "Lock View");
	public NumberSetting range = new NumberSetting("Range", 4, 1, 6, 0.1);
	public NumberSetting aps = new NumberSetting("APS", 15, 1, 20, 1);
	public BooleanSetting delay = new BooleanSetting("1.9 Delay", true);
	public BooleanSetting swing = new BooleanSetting("Swing", true);
	public BooleanSetting autoBlock = new BooleanSetting("AutoBlock", true);
	public BooleanSetting trigger = new BooleanSetting("Trigger", false);
	public BooleanSetting walls = new BooleanSetting("Walls", true);
	public BooleanSetting players = new BooleanSetting("Players", true);
	public BooleanSetting animals = new BooleanSetting("Animals", false);
	public BooleanSetting monsters = new BooleanSetting("Monsters", false);
	public BooleanSetting passives = new BooleanSetting("Passives", false);
	public BooleanSetting invisibles = new BooleanSetting("Invisibles", false);
	
	int passedTicks;
	
	public Killaura() {
		super("Killaura", "Attacks select surrounding entities", Category.COMBAT);
		addSettings(rotation, range, aps, delay, swing, autoBlock, trigger, walls, players, animals, monsters, passives, invisibles);
	}
	
	@Override
	public void onTick(){
		try{
			if(mc.world != null){
				List<LivingEntity> targets = Lists.<LivingEntity>newArrayList();
				for(Entity e : mc.world.getEntities()){
					if (e instanceof LivingEntity && e != mc.player && !FriendManager.INSTANCE.isFriend((LivingEntity)e)) targets.add((LivingEntity)e);
				}
				targets.sort(Comparator.comparingDouble(entity -> mc.player.distanceTo(entity)));
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
							if (passedTicks > aps.getValue()) passedTicks--;
							else if (passedTicks <= aps.getValue()) passedTicks=(int) (aps.getValue() * 100);
							if(delay.isEnabled() ? mc.player.getAttackCooldownProgress(0.5F) == 1 : new Timer().hasTimeElapsed((long) (aps.getValue() / 1000), true)){
								mc.interactionManager.attackEntity(mc.player, target);
								if (swing.isEnabled() && (autoBlock.isEnabled() ? !ModuleManager.INSTANCE.getModule(OldBlock.class).isEnabled() : true) || (ModuleManager.INSTANCE.getModule(OldBlock.class).isEnabled() ? mc.options.getPerspective() != Perspective.FIRST_PERSON : false) && !(mc.player.getMainHandStack().getItem() instanceof SwordItem)) mc.player.swingHand(Hand.MAIN_HAND);
								else mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
								if (swing.isEnabled() && !(mc.player.getMainHandStack().getItem() instanceof SwordItem) || ModuleManager.INSTANCE.getModule(OldBlock.class).animation.is("Swing")) mc.player.swingHand(Hand.MAIN_HAND); 
							}
							if(autoBlock.isEnabled() && mc.player.getOffHandStack().getItem() instanceof ShieldItem){
								mc.interactionManager.interactItem(mc.player, mc.world, Hand.OFF_HAND);
								mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
							}
						}
						if (target.isDead()) {
							RotationUtils.resetYaw();
							RotationUtils.resetPitch();
						}
					}else{
						if(!ModuleManager.INSTANCE.getModule(Scaffold.class).isEnabled()){
							RotationUtils.resetPitch();
							RotationUtils.resetYaw();
						}
						this.setDisplayName("Killaura " + ColorUtils.gray + "None");
						if(autoBlock.isEnabled()){
//							mc.interactionManager.stopUsingItem(mc.player);
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		super.onTick();
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
				if (passives.isEnabled() && entity instanceof PassiveEntity) return true;
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
}
