package dev.hypnotic.hypnotic_client.module.player;

import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.settings.settingtypes.BooleanSetting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.NumberSetting;
import dev.hypnotic.hypnotic_client.utils.TimeHelper;
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
