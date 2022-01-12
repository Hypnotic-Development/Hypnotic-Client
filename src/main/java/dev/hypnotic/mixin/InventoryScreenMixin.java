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
import net.minecraft.text.LiteralText;
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
	        this.addDrawableChild(new ButtonWidget(this.x + 130, this.height / 2 - 24, 40, 20, new LiteralText("Dupe"), (b) -> {
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
