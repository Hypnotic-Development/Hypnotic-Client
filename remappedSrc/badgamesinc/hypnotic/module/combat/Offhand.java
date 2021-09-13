package badgamesinc.hypnotic.module.combat;

import org.lwjgl.glfw.GLFW;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventMouseButton;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.Wrapper;
import badgamesinc.hypnotic.utils.player.inventory.FindItemResult;
import badgamesinc.hypnotic.utils.player.inventory.InventoryUtils;
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
		this.setDisplayName("Offhand " + ColorUtils.gray + mode.getSelected());
		AutoTotem autoTotem = ModuleManager.INSTANCE.getModule(AutoTotem.class);

        // Sword Gap
        if ((mc.player.getMainHandStack().getItem() instanceof SwordItem
            || mc.player.getMainHandStack().getItem() instanceof AxeItem) && swordGap.isEnabled()) currentItem = Item.EGap;

        // Ca and mining
        else if ((ModuleManager.INSTANCE.getModule(CrystalAura.class).isEnabled() && crystalCa.isEnabled())
            || mc.interactionManager.isBreakingBlock() && crystalMine.isEnabled()) currentItem = Item.Crystal;

        else currentItem = getItem();

        // Checking offhand item
        if (mc.player.getOffHandStack().getItem() != currentItem.item) {
            FindItemResult item = InventoryUtils.find(itemStack -> itemStack.getItem() == currentItem.item, hotbar.isEnabled() ? 0 : 9, 35);

            // No offhand item
            if (!item.found()) {
                if (!sentMessage) {
                    Wrapper.tellPlayer("Chosen item not found.");
                    sentMessage = true;
                }
            }

            // Swap to offhand
            else if ((isClicking || !rightClick.isEnabled()) && !autoTotem.isLocked() && !item.isOffhand()) {
                InventoryUtils.move().from(item.getSlot()).toOffhand();
                sentMessage = false;
            }
        }

        // If not clicking, set to totem if auto totem is on
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
