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
import badgamesinc.hypnotic.utils.TimeHelper;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

public class Killaura extends Mod {

	public static LivingEntity target;
	
	public ModeSetting rotation = new ModeSetting("Mode", "Silent", "Silent", "Lock View");
	public NumberSetting range = new NumberSetting("Range", 4, 1, 6, 0.1);
	public NumberSetting aps = new NumberSetting("APS", 15, 1, 20, 1);
	public BooleanSetting delay = new BooleanSetting("1.9 Delay", true);
	public BooleanSetting swing = new BooleanSetting("Swing", true);
	public BooleanSetting autoBlock = new BooleanSetting("AutoBlock", true);
	private TimeHelper apsTimer = new TimeHelper();
	int passedTicks;
	
	public Killaura() {
		super("Killaura", "Attacks select surrounding entities", Category.COMBAT);
		addSettings(rotation, range, aps, delay, swing, autoBlock);
	}
	
	@Override
	public void onTick(){
		try{
			if(mc.world != null){
				List<LivingEntity> targets = Lists.<LivingEntity>newArrayList();
				for(Entity e : mc.world.getEntities()){
					if (e instanceof LivingEntity && e != mc.player) targets.add((LivingEntity)e);
				}
				targets.sort(Comparator.comparingDouble(entity -> mc.player.distanceTo(entity)));
				if(!targets.isEmpty()){
					target = (LivingEntity)targets.get(0);
					if (mc.player.distanceTo(target) > range.getValue()) target = null;
					if(target != null){
						this.setDisplayName("Killaura " + ColorUtils.gray + (target instanceof PlayerEntity ? target.getName().asString().replaceAll(ColorUtils.colorChar, "&") : target.getDisplayName().asString()));
						if(target instanceof LivingEntity && target != mc.player && mc.player.distanceTo(target) <= range.getValue() && target.isAlive() && mc.player.isAlive() && !FriendManager.INSTANCE.isFriend(target)){
							RotationUtils.setSilentPitch(RotationUtils.getRotations(target)[1]);
							RotationUtils.setSilentYaw(RotationUtils.getRotations(target)[0]);
							if (passedTicks > aps.getValue()) passedTicks--;
							else if (passedTicks <= aps.getValue()) passedTicks=(int) (aps.getValue() * 100);
							if(delay.isEnabled() ? mc.player.getAttackCooldownProgress(0.5F) == 1 : apsTimer.passedSec((long) (aps.getValue() * 0.1))){
								mc.interactionManager.attackEntity(mc.player, target);
//								mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround((float) RotationUtils.getRotations(target)[0], (float) RotationUtils.getRotations(target)[1], mc.player.isOnGround()));
								apsTimer.reset();
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
		}catch(ArrayIndexOutOfBoundsException | NullPointerException | IllegalArgumentException e){
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
			if (target != null) {
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setYaw(RotationUtils.getRotations(target)[0]);
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch(RotationUtils.getRotations(target)[1]);
			} else {
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setYaw(mc.player.getYaw());
				((PlayerMoveC2SPacketAccessor) event.getPacket()).setPitch(mc.player.getPitch());
			}
		}
	}
}
