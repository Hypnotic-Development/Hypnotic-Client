package badgamesinc.hypnotic.module.world;

import java.util.ArrayList;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.world.WorldUtils;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;

public class Surround extends Mod {
	
	public NumberSetting delay = new NumberSetting("Delay", 50, 0, 1000, 10);
	public BooleanSetting autoDisable = new BooleanSetting("Auto Disable", true);
	public BooleanSetting onlyObi = new BooleanSetting("Only Obsidian", true);
	badgamesinc.hypnotic.utils.Timer delayTimer = new badgamesinc.hypnotic.utils.Timer();

	public Surround() {
		super("Surround", "Surround", Category.WORLD);
		addSettings(delay, autoDisable, onlyObi);
	}
	
	@Override
	public void onEnable() {
		double x = Math.round(mc.player.getX()) + 0.5; double y =
		Math.round(mc.player.getY()); double z = Math.round(mc.player.getZ()) + 0.5;
		mc.player.setPos(x, y, z); mc.player.setYaw(0);
		super.onEnable();
	}
	
	int stage = 0;
	@Override
	public void onTick() {
		ArrayList<BlockPos> blocks = new ArrayList<>();
		if ((onlyObi.isEnabled() && mc.player.getMainHandStack().getItem() != Blocks.OBSIDIAN.asItem()) || !(mc.player.getMainHandStack().getItem() instanceof BlockItem)) return;
		
		if (delayTimer.hasTimeElapsed((long)delay.getValue() / 1000, false)) {
			System.out.println("eh");
			if (stage < 4) stage++;
			else stage = 0;
			delayTimer.reset();
		}
		switch(stage) {
			case 0:
				if (mc.world.getBlockState(mc.player.getBlockPos().north()).getMaterial().isReplaceable()) {
					WorldUtils.placeBlockMainHand(mc.player.getBlockPos().north(), true, true);
				}
				break;
			case 1:
				if (mc.world.getBlockState(mc.player.getBlockPos().south()).getMaterial().isReplaceable()) {
					WorldUtils.placeBlockMainHand(mc.player.getBlockPos().south(), true, true);
				}
				break;
			case 2:
				if (mc.world.getBlockState(mc.player.getBlockPos().east()).getMaterial().isReplaceable()) {
					WorldUtils.placeBlockMainHand(mc.player.getBlockPos().east(), true, true);
				}
				break;
			case 3:
				if (mc.world.getBlockState(mc.player.getBlockPos().west()).getMaterial().isReplaceable()) {
					WorldUtils.placeBlockMainHand(mc.player.getBlockPos().west(), true, true);
				}
				break;
		}
		for (BlockPos pos : blocks) {
			if (delayTimer.hasTimeElapsed((long)delay.getValue() / 1000, true)) WorldUtils.placeBlockMainHand(pos, true, true);
			delayTimer.reset();
		}
		super.onTick();
	}
}
