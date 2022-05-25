/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*/
package dev.hypnotic.module.combat;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventMotionUpdate;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.ChatUtils;
import dev.hypnotic.utils.RotationUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.util.Hand;

/**
* @author BadGamesInc
*/
public class FightBot extends Mod {

	public NumberSetting range = new NumberSetting("Attack Range", 4, 1, 6, 0.1);
	
	public PlayerEntity target;
	
	public FightBot() {
		super("FightBot", "Automatically hunts a specified player", Category.COMBAT);
		addSettings(range);
	}
	
	@Override
	public void onEnable() {
		if (target == null) ChatUtils.tellPlayerRaw("Use .fightbot hunt <target> to begin");
		else mc.player.sendChatMessage(".b follow " + target.getName().asString());
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		mc.player.sendChatMessage(".b stop");
		super.onDisable();
	}
	
	@EventTarget
	public void onMotionUpdate(EventMotionUpdate event) {
		if (target != null && target != mc.player) {
			if (ModuleManager.INSTANCE.getModule(Killaura.class).isEnabled()) ModuleManager.INSTANCE.getModule(Killaura.class).toggle();
			
			attack(event, target);
		}
	}
	
	public void attack(EventMotionUpdate event, PlayerEntity target) {
		if (canAttack(target)) {
			RotationUtils.setSilentPitch(RotationUtils.getRotations(target)[1]);
			RotationUtils.setSilentYaw(RotationUtils.getRotations(target)[0]);
			
			if (mc.player.getAttackCooldownProgress(0.5F) == 1){
				mc.interactionManager.attackEntity(mc.player, target);
				mc.player.networkHandler.sendPacket(new HandSwingC2SPacket(Hand.MAIN_HAND));
			}
		}
		if (target.isDead()) {
			RotationUtils.resetYaw();
			RotationUtils.resetPitch();
		}
	}
	
	private boolean canAttack(LivingEntity entity) {
		return entity != mc.player && mc.player.distanceTo(entity) <= range.getValue() && entity.isAlive() && mc.player.isAlive();
	}

	public void setTarget(PlayerEntity target) {
		this.target = target;
	}
}
