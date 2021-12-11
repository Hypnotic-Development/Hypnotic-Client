package dev.hypnotic.hypnotic_client.module.misc;

import java.util.ArrayList;

import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.settings.settingtypes.BooleanSetting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.NumberSetting;
import dev.hypnotic.hypnotic_client.utils.player.inventory.InventoryUtils;
import dev.hypnotic.hypnotic_client.utils.world.WorldUtils;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.util.math.BlockPos;

public class Surround extends Mod {
	
	public NumberSetting delay = new NumberSetting("Delay", 50, 0, 1000, 10);
	public BooleanSetting autoSwitch = new BooleanSetting("Auto Switch", true);
	public BooleanSetting switchBack = new BooleanSetting("Auto Switch", true);
	public BooleanSetting onlyObi = new BooleanSetting("Only Obsidian", true);
	public BooleanSetting snap = new BooleanSetting("Snap To Center", false);
	dev.hypnotic.hypnotic_client.utils.Timer delayTimer = new dev.hypnotic.hypnotic_client.utils.Timer();

	public Surround() {
		super("Surround", "Surround", Category.MISC);
		addSettings(delay, autoSwitch, switchBack, onlyObi, snap);
	}
	
	@Override
	public void onEnable() {
		double x = Math.round(mc.player.getX()) + 0.5; double y =
		Math.round(mc.player.getY()); double z = Math.round(mc.player.getZ()) + 0.5;
		if (snap.isEnabled()) mc.player.setPos(x, y, z); mc.player.setYaw(0);
		super.onEnable();
	}
	
	int stage = 0;
	@Override
	public void onTick() {
		ArrayList<BlockPos> blocks = new ArrayList<>();
		
		if (autoSwitch.isEnabled() 
				&& mc.world.getBlockState(mc.player.getBlockPos().west()).getMaterial().isReplaceable()
				&& mc.world.getBlockState(mc.player.getBlockPos().east()).getMaterial().isReplaceable()
				&& mc.world.getBlockState(mc.player.getBlockPos().south()).getMaterial().isReplaceable()
				&& mc.world.getBlockState(mc.player.getBlockPos().north()).getMaterial().isReplaceable()) 
		{
			if (mc.player.getMainHandStack().getItem() != Blocks.OBSIDIAN.asItem()) {
				int obsidianSlot = InventoryUtils.findInHotbarInt(Blocks.OBSIDIAN.asItem());
				InventoryUtils.selectSlot(obsidianSlot);
			}
		}
		
		if ((onlyObi.isEnabled() && mc.player.getMainHandStack().getItem() != Blocks.OBSIDIAN.asItem()) || !(mc.player.getMainHandStack().getItem() instanceof BlockItem)) return;
		
		if (delayTimer.hasTimeElapsed((long)delay.getValue() / 1000, false)) {
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
	
	@Override
	public void onTickDisabled() {
		this.onlyObi.setVisible(!autoSwitch.isEnabled());
		this.switchBack.setVisible(autoSwitch.isEnabled());
		super.onTickDisabled();
	}
}
