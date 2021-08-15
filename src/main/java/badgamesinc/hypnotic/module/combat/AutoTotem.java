package badgamesinc.hypnotic.module.combat;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.player.inventory.FindItemResult;
import badgamesinc.hypnotic.utils.player.inventory.InventoryUtils;
import net.minecraft.item.Items;

public class AutoTotem extends Mod {

	int totems;
	
	public AutoTotem() {
		super("AutoTotem", "Automatically places totems in your offhand", Category.COMBAT);
	}

	@Override
	public void onTick() {
		FindItemResult result = InventoryUtils.find(Items.TOTEM_OF_UNDYING);
        totems = result.getCount();
        this.setDisplayName("AutoTotem " + ColorUtils.gray + totems);
		if (mc.player.getInventory().contains(Items.TOTEM_OF_UNDYING.getDefaultStack())) {
			if (mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
				InventoryUtils.move().from(result.getSlot()).to(InventoryUtils.OFFHAND);
			}
		}
		super.onTick();
	}
}
