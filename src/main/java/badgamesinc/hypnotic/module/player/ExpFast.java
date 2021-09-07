package badgamesinc.hypnotic.module.player;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.TimeHelper;
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
