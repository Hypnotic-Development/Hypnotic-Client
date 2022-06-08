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
package dev.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.exploit.InvDupe;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

@Mixin({InventoryScreen.class})
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
    public InventoryScreenMixin(PlayerScreenHandler container, PlayerInventory playerInventory, Text name) {
        super(container, playerInventory, name);
    }

    @Inject(
        method = {"init"},
        at = {@At("TAIL")}
    )
    protected void init(CallbackInfo ci) {
    	if (ModuleManager.INSTANCE.getModule(InvDupe.class).isEnabled()) {
	        this.addDrawableChild(new ButtonWidget(this.x + 130, this.height / 2 - 24, 40, 20, Text.literal("Dupe"), (b) -> {
	            this.dupe();
	        }));
    	}
    }

    // Credits to Duper Trooper for publishing the dupe 
    // Original video https://www.youtube.com/watch?v=8Xd7DcApFbM
    
    private void dupe() {
        Slot outputSlot = (Slot)((PlayerScreenHandler)this.handler).slots.get(0);
        this.onMouseClick(outputSlot, outputSlot.id, 0, SlotActionType.THROW);
    }
}
