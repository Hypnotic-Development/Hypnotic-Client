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
package dev.hypnotic.module.player;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.Timer;
import dev.hypnotic.utils.player.inventory.InventoryUtils;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;

/**
* @author BadGamesInc
*/
public class ChestStealer extends Mod {

	public NumberSetting delay = new NumberSetting("Delay", 100, 0, 500, 10);
	
	private Timer delayTimer = new Timer();
	
	Thread stealThread = new Thread(this::steal);
	
	public ChestStealer() {
		super("ChestStealer", "Steals items from chests", Category.PLAYER);
		addSetting(delay);
		
		stealThread.start();
	}
	
	private void steal() {
		for (;;) {
			if (mc.currentScreen instanceof GenericContainerScreen && this.isEnabled()) {
				if (!InventoryUtils.isInventoryFull() && !InventoryUtils.isContainerEmpty(mc.player.currentScreenHandler)) {
					ScreenHandler handler = mc.player.currentScreenHandler;
					if (delayTimer.hasTimeElapsed(delay.getValueInt(), true)) {
						for (int i = 0; i < handler.slots.size() - InventoryUtils.MAIN_END; i++) {
							Slot slot = handler.slots.get(i);
							ItemStack stack = slot.getStack();
							System.out.println(i);
							if (stack.getItem() != Items.AIR) {
								mc.interactionManager.clickSlot(handler.syncId, slot.id, 0, SlotActionType.QUICK_MOVE, mc.player);

								try {
									Thread.sleep(delay.getValueInt());
								} catch(Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				} else {
					mc.player.closeHandledScreen();
				}
			}
		}
	}
}
