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
package dev.hypnotic.module.combat;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.player.inventory.FindItemResult;
import dev.hypnotic.utils.player.inventory.InventoryUtils;
import net.minecraft.item.Items;

public class AutoTotem extends Mod {

	public BooleanSetting lock = new BooleanSetting("Lock", false);
	public BooleanSetting smart = new BooleanSetting("Smart", true);
	
	int totems;
	private boolean locked;
	
	public AutoTotem() {
		super("AutoTotem", "Automatically places totems in your offhand", Category.COMBAT);
		addSettings(lock, smart);
	}

	@Override
	public void onTick() {
		FindItemResult result = InventoryUtils.find(Items.TOTEM_OF_UNDYING);
        totems = result.getCount();
        this.setDisplayName("AutoTotem " + ColorUtils.gray + totems);
		if (mc.player.getInventory().contains(Items.TOTEM_OF_UNDYING.getDefaultStack())) {
			if (this.lock.isEnabled()) this.locked = true;
			if (mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
				if (!smart.isEnabled()) {
					this.locked = true;
					InventoryUtils.move().from(result.getSlot()).to(InventoryUtils.OFFHAND);
				} else {
					if (mc.player.getHealth() < 15 || mc.player.isFallFlying() || mc.player.fallDistance > 6) {
						this.locked = true;
						InventoryUtils.move().from(result.getSlot()).to(InventoryUtils.OFFHAND);
					} else {
						this.locked = false;
					}
				}
			}
			if (!smart.isEnabled()) {
				this.locked = true;
			} else {
				if (mc.player.getHealth() < 10 || mc.player.isFallFlying() || mc.player.fallDistance > 6) {
					this.locked = true;
				} else {
					this.locked = false;
				}
			}
		}
		super.onTick();
	}
	
	public boolean isLocked() {
		return locked;
	}
}
