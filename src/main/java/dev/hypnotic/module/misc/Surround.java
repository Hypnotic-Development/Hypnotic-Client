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
package dev.hypnotic.module.misc;

import java.util.ArrayList;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.player.inventory.InventoryUtils;
import dev.hypnotic.utils.world.WorldUtils;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;

public class Surround extends Mod {
	
	public NumberSetting delay = new NumberSetting("Delay", 50, 0, 1000, 10);
	public BooleanSetting autoSwitch = new BooleanSetting("Auto Switch", true);
	public BooleanSetting switchBack = new BooleanSetting("Auto Switch", true);
	public BooleanSetting onlyObi = new BooleanSetting("Only Obsidian", true);
	public BooleanSetting snap = new BooleanSetting("Snap To Center", false);
	dev.hypnotic.utils.Timer delayTimer = new dev.hypnotic.utils.Timer();

	public Surround() {
		super("Surround", "Surround", Category.MISC);
		addSettings(delay, autoSwitch, switchBack, onlyObi, snap);
	}
	
	@Override
	public void onEnable() {
		double x = Math.round(mc.player.getX()) + 0.5; double y =
		Math.round(mc.player.getY()); double z = Math.round(mc.player.getZ()) + 0.5;
		if (snap.isEnabled()) mc.player.setPos(x, y, z); mc.player.setYaw(0);
		super.onEnable();
	}
	
	int stage = 0;
	@Override
	public void onTick() {
		ArrayList<BlockPos> blocks = new ArrayList<>();
		
		if (autoSwitch.isEnabled() 
				&& mc.world.getBlockState(mc.player.getBlockPos().west()).getMaterial().isReplaceable()
				&& mc.world.getBlockState(mc.player.getBlockPos().east()).getMaterial().isReplaceable()
				&& mc.world.getBlockState(mc.player.getBlockPos().south()).getMaterial().isReplaceable()
				&& mc.world.getBlockState(mc.player.getBlockPos().north()).getMaterial().isReplaceable()) 
		{
			if (mc.player.getMainHandStack().getItem() != Blocks.OBSIDIAN.asItem()) {
				int obsidianSlot = InventoryUtils.findInHotbarInt(Blocks.OBSIDIAN.asItem());
				InventoryUtils.selectSlot(obsidianSlot);
			}
		}
		
		if ((onlyObi.isEnabled() && mc.player.getMainHandStack().getItem() != Blocks.OBSIDIAN.asItem()) || !(mc.player.getMainHandStack().getItem() instanceof BlockItem)) return;
		
		if (delayTimer.hasTimeElapsed((long)delay.getValue() / 1000, false)) {
			if (stage < 4) stage++;
			else stage = 0;
			delayTimer.reset();
		}
		switch(stage) {
			case 0:
				if (mc.world.getBlockState(mc.player.getBlockPos().north()).getMaterial().isReplaceable()) {
					WorldUtils.placeBlockMainHand(mc.player.getBlockPos().north(), true, true);
				}
				break;
			case 1:
				if (mc.world.getBlockState(mc.player.getBlockPos().south()).getMaterial().isReplaceable()) {
					WorldUtils.placeBlockMainHand(mc.player.getBlockPos().south(), true, true);
				}
				break;
			case 2:
				if (mc.world.getBlockState(mc.player.getBlockPos().east()).getMaterial().isReplaceable()) {
					WorldUtils.placeBlockMainHand(mc.player.getBlockPos().east(), true, true);
				}
				break;
			case 3:
				if (mc.world.getBlockState(mc.player.getBlockPos().west()).getMaterial().isReplaceable()) {
					WorldUtils.placeBlockMainHand(mc.player.getBlockPos().west(), true, true);
				}
				break;
		}
		for (BlockPos pos : blocks) {
			if (delayTimer.hasTimeElapsed((long)delay.getValue() / 1000, true)) WorldUtils.placeBlockMainHand(pos, true, true);
			delayTimer.reset();
		}
		super.onTick();
	}
	
	@Override
	public void onTickDisabled() {
		this.onlyObi.setVisible(!autoSwitch.isEnabled());
		this.switchBack.setVisible(autoSwitch.isEnabled());
		super.onTickDisabled();
	}
}
