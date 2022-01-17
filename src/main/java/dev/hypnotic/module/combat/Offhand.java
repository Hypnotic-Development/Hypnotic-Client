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

import org.lwjgl.glfw.GLFW;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventMouseButton;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Wrapper;
import dev.hypnotic.utils.player.inventory.FindItemResult;
import dev.hypnotic.utils.player.inventory.InventoryUtils;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SwordItem;

public class Offhand extends Mod {

	public ModeSetting mode = new ModeSetting("Item", "EGap", "EGap", "Gap", "Crystal", "Shield");
	public BooleanSetting swordGap = new BooleanSetting("Sword-Gap", false);
	public BooleanSetting crystalCa = new BooleanSetting("CrystalAura-Crystal", false);
	public BooleanSetting crystalMine = new BooleanSetting("Crystal Mine", false);
	public BooleanSetting hotbar = new BooleanSetting("Hotbar", true);
	public BooleanSetting rightClick = new BooleanSetting("Right Click", false);
	
	private boolean isClicking;
    private boolean sentMessage;
    private Item currentItem;
    
	public Offhand() {
		super("Offhand", "Swaps items in your offhand based on the situation", Category.COMBAT);
		addSettings(mode, rightClick, swordGap, crystalCa, crystalMine, hotbar);
	}
	
	@Override
	public void onTick() {
		if (mc.world == null || mc.interactionManager == null) return;
		this.setDisplayName("Offhand " + ColorUtils.gray + mode.getSelected());
		AutoTotem autoTotem = ModuleManager.INSTANCE.getModule(AutoTotem.class);

        if ((mc.player.getMainHandStack().getItem() instanceof SwordItem
            || mc.player.getMainHandStack().getItem() instanceof AxeItem) && swordGap.isEnabled()) currentItem = Item.EGap;

        else if ((ModuleManager.INSTANCE.getModule(CrystalAura.class).isEnabled() && crystalCa.isEnabled())
            || mc.interactionManager.isBreakingBlock() && crystalMine.isEnabled()) currentItem = Item.Crystal;

        else currentItem = getItem();

        if (mc.player.getOffHandStack().getItem() != currentItem.item) {
            FindItemResult item = InventoryUtils.find(itemStack -> itemStack.getItem() == currentItem.item, hotbar.isEnabled() ? 0 : 9, 35);

            if (!item.found()) {
                if (!sentMessage) {
                    Wrapper.tellPlayer("Chosen item not found.");
                    sentMessage = true;
                }
            }

            else if ((isClicking || !rightClick.isEnabled()) && !autoTotem.isLocked() && !item.isOffhand()) {
                InventoryUtils.move().from(item.getSlot()).toOffhand();
                sentMessage = false;
            }
        }

        else if (!isClicking && rightClick.isEnabled()) {
            if (autoTotem.isEnabled()) {
                FindItemResult totem = InventoryUtils.find(itemStack -> itemStack.getItem() == Items.TOTEM_OF_UNDYING, hotbar.isEnabled() ? 0 : 9, 35);

                if (totem.found() && !totem.isOffhand()) {
                    InventoryUtils.move().from(totem.getSlot()).toOffhand();
                }
            } else {
                FindItemResult empty = InventoryUtils.find(ItemStack::isEmpty, hotbar.isEnabled() ? 0 : 9, 35);
                if (empty.found()) InventoryUtils.move().fromOffhand().to(empty.getSlot());
            }
        }
		super.onTick();
	}
	
	@EventTarget
    private void onMouseButton(EventMouseButton event) {
        isClicking = mc.currentScreen == null && !ModuleManager.INSTANCE.getModule(AutoTotem.class).isLocked() && !usableItem() && !mc.player.isUsingItem() && event.getButton() == GLFW.GLFW_MOUSE_BUTTON_RIGHT;
    }

    private boolean usableItem() {
        return mc.player.getMainHandStack().getItem() == Items.BOW
            || mc.player.getMainHandStack().getItem() == Items.TRIDENT
            || mc.player.getMainHandStack().getItem() == Items.CROSSBOW
            || mc.player.getMainHandStack().getItem().isFood();
    }
    
    private Item getItem() {
    	return switch(mode.getSelected()) {
    		case "EGap" ->  Item.EGap;
    		case "Crystal" -> Item.Crystal;
    		case "Gap" -> Item.Gap;
    		case "Shield" -> Item.Shield;
    		default -> Item.Crystal;
    	};
    }
	
	public enum Item {
        EGap(Items.ENCHANTED_GOLDEN_APPLE),
        Gap(Items.GOLDEN_APPLE),
        Crystal(Items.END_CRYSTAL),
        Shield(Items.SHIELD);

        net.minecraft.item.Item item;

        Item(net.minecraft.item.Item item) {
            this.item = item;
        }
    }
}
