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
package dev.hypnotic.utils.player.inventory;

import static dev.hypnotic.utils.MCUtils.mc;

import net.minecraft.util.Hand;

public class FindItemResult {
    private final int slot, count;

    public FindItemResult(int slot, int count) {
        this.slot = slot;
        this.count = count;
    }

    public int getSlot() {
        return slot;
    }

    public int getCount() {
        return count;
    }

    public boolean found() {
        return slot != -1;
    }

    public Hand getHand() {
        if (slot == InventoryUtils.OFFHAND) return Hand.OFF_HAND;
        else if (slot == mc.player.getInventory().selectedSlot) return Hand.MAIN_HAND;
        return null;
    }

    public boolean isMainHand() {
        return getHand() == Hand.MAIN_HAND;
    }

    public boolean isOffhand() {
        return getHand() == Hand.OFF_HAND;
    }

    public boolean isHotbar() {
        return slot >= InventoryUtils.HOTBAR_START && slot <= InventoryUtils.HOTBAR_END;
    }

    public boolean isMain() {
        return slot >= InventoryUtils.MAIN_START && slot <= InventoryUtils.MAIN_END;
    }

    public boolean isArmor() {
        return slot >= InventoryUtils.ARMOR_START && slot <= InventoryUtils.ARMOR_END;
    }
}
