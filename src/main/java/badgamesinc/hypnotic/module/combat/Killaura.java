package badgamesinc.hypnotic.module.combat;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Lists;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.RotationUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;

public class Killaura extends Mod {

	public static LivingEntity target;
	
	public ModeSetting rotation = new ModeSetting("Rotations Mode", "Silent", "Silent", "Lock View");
	public NumberSetting range = new NumberSetting("Range", 4, 1, 6, 0.1);
	public BooleanSetting delay = new BooleanSetting("1.9 Delay", true);
	public BooleanSetting swing = new BooleanSetting("Swing", true);
	
	public Killaura() {
		super("Killaura", "Attacks select surrounding entities", Category.COMBAT);
		addSettings(rotation, range, delay, swing);
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
						if(target instanceof LivingEntity && target != mc.player && mc.player.distanceTo(target) <= range.getValue() && target.isAlive() && mc.player.isAlive()){
							RotationUtils.setSilentPitch(RotationUtils.getRotations(target)[1]);
							RotationUtils.setSilentYaw(RotationUtils.getRotations(target)[0]);
							if(delay.isEnabled() ? mc.player.getAttackCooldownProgress(0.5F) == 1 : true){
								mc.interactionManager.attackEntity(mc.player, target);
								MinecraftClient.getInstance().player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround((float) RotationUtils.getRotations(target)[0], (float) RotationUtils.getRotations(target)[1], MinecraftClient.getInstance().player.isOnGround()));
								if (swing.isEnabled()) mc.player.swingHand(Hand.MAIN_HAND);
							}
						}
					}
				}
				if(target == null)
					RotationUtils.resetPitch();
			}
		}catch(ArrayIndexOutOfBoundsException e){
			e.printStackTrace();
		}
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		RotationUtils.resetPitch();
		RotationUtils.resetYaw();
		super.onDisable();
	}
}
