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
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.module.movement;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.combat.Killaura;
import dev.hypnotic.module.combat.TargetStrafe;
import dev.hypnotic.module.misc.Timer;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.player.PlayerUtils;
import net.minecraft.block.Blocks;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.BlockPos;

public class Speed extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "NCP");
	public NumberSetting speed = new NumberSetting("Speed", 4, 1, 10, 0.1);
	public NumberSetting timerBoost = new NumberSetting("Timer Boost", 1, 1, 5, 0.01);
	public BooleanSetting jump = new BooleanSetting("Jump", true);
	public NumberSetting jumpHeight = new NumberSetting("Jump Height", 4, 1, 10, 0.1);
	public BooleanSetting override = new BooleanSetting("Override Jump Height", true);
	
	private int wallTicks = 0;
	private boolean direction = false;
	
	public Speed() {
		super("Speed", "Makes you go fast", Category.MOVEMENT);
		addSettings(mode, speed, timerBoost, jump, jumpHeight, override);
	}
	
	@Override
	public void onTick() {
		if(mc.player.isOnGround() && PlayerUtils.isMoving() && jump.isEnabled()) mc.player.setVelocity(mc.player.getVelocity().x, override.isEnabled() && mc.options.jumpKey.isPressed() ? 0.45 : jumpHeight.getValue() * 0.1, mc.player.getVelocity().z);
		super.onTick();
	}

	@Override
	public void onMotion() {
		this.setDisplayName("Speed " + ColorUtils.gray + mode.getSelected());
		if (timerBoost.getValue() != 1.0) PlayerUtils.setTimerSpeed((float) timerBoost.getValue());
		if(mc.player != null && (mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0) && !mc.player.isTouchingWater()) {
			if (!mc.player.isOnGround()) {
				wallTicks++;
				if (wallTicks > 7 && mc.player.horizontalCollision) {
					direction = !direction;
					wallTicks = 0;
				}
			} else wallTicks = 0;
			BlockPos downBlock = mc.player.isOnGround() ? mc.player.getBlockPos() : mc.player.getBlockPos().down();
			boolean isIce = mc.world.getBlockState(downBlock).getBlock() == Blocks.ICE || mc.world.getBlockState(downBlock).getBlock() == Blocks.PACKED_ICE || mc.world.getBlockState(downBlock).getBlock() == Blocks.BLUE_ICE;
			double ncpLimit = (mc.player.isOnGround() ? 0 : (mc.player.hasStatusEffect(StatusEffects.SPEED) ? (mc.player.getStatusEffect(StatusEffects.SPEED).getAmplifier() == 1 ? 4 : 3.5) : 2.9)) + (isIce ? 4.3 : 0);
            double speed = (mode.is("NCP") ? ncpLimit * 0.1 : (this.speed.getValue()) * 0.1 + (mc.player.isOnGround() ? this.speed.getValue() * 0.01 : 0));
            if (mode.is("NCP") && !mc.player.isOnGround()) PlayerUtils.setMotion(speed);
            else if (mode.is("Vanilla")) PlayerUtils.setMotion(speed);
            if (TargetStrafe.canStrafe()) {
            	TargetStrafe.strafe(speed, Killaura.target, direction, false);
            }
        }
		super.onMotion();
	}
	
	@Override
	public void onTickDisabled() {
		speed.setVisible(!mode.is("NCP"));
		jumpHeight.setVisible(jump.isEnabled());
		super.onTickDisabled();
	}
	
	@Override
	public void onDisable() {
		if (!ModuleManager.INSTANCE.getModule(Timer.class).isEnabled()) PlayerUtils.setTimerSpeed(1f);
		super.onDisable();
	}

}
