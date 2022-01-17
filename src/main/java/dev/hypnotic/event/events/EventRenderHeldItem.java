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
package dev.hypnotic.event.events;

import dev.hypnotic.event.Event;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public class EventRenderHeldItem extends Event {

    private ItemStack itemStack;
    private Hand hand;
    private float partialTicks;
    private MatrixStack matrixStack;

    public EventRenderHeldItem(ItemStack itemStack, Hand hand, float partialTicks, MatrixStack matrixStack) {
        this.itemStack = itemStack;
        this.hand = hand;
        this.partialTicks = partialTicks;
        this.matrixStack = matrixStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Hand getHand() {
        return hand;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }
}
