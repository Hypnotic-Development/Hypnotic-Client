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
package dev.hypnotic.module.player;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.TimeHelper;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class ExpFast extends Mod {

	public NumberSetting delay = new NumberSetting("Delay", 0, 0, 1, 0.1);
	public BooleanSetting swap = new BooleanSetting("Switch Item", true);
	
	TimeHelper timer = new TimeHelper();
	
	public ExpFast() {
		super("ExpFast", "Throw exp pots at the speed of sound", Category.PLAYER);
		addSettings(delay, swap);
	}

	@Override
	public void onTick() {
		if (mc.player.getMainHandStack().getItem() == Items.EXPERIENCE_BOTTLE && mc.options.keyUse.isPressed()) {
			mc.interactionManager.interactItem(mc.player, mc.world, Hand.MAIN_HAND);
		} else if (mc.player.getMainHandStack().getItem() != Items.EXPERIENCE_BOTTLE && swap.isEnabled()) {
			int xpSlot = mc.player.getInventory().getSlotWithStack(Items.EXPERIENCE_BOTTLE.getDefaultStack());
			if (xpSlot != -1) mc.player.getInventory().selectedSlot = xpSlot;
		}
		super.onTick();
	}
}
