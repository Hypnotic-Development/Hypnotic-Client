package badgamesinc.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        this.addDrawableChild(new ButtonWidget(this.x + 130, this.height / 2 - 24, 40, 20, new LiteralText("Dupe"), (b) -> {
//            System.out.println(class_310.method_1551().method_1558().field_3760.getString());
            this.dupe();
        }));
    }

    private void dupe() {
        Slot outputSlot = (Slot)((PlayerScreenHandler)this.handler).slots.get(0);
        this.onMouseClick(outputSlot, outputSlot.id, 0, SlotActionType.THROW);
    }
}
