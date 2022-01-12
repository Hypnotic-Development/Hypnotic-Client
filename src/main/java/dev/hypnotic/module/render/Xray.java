package dev.hypnotic.module.render;

import java.util.ArrayList;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.OreBlock;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.util.registry.Registry;

public class Xray extends Mod {

	public static ArrayList<Block> blocks = new ArrayList<>();
	
	public Xray() {
		super("Xray", "See shit through blocks", Category.RENDER);
		Registry.BLOCK.forEach(block -> {
            if (blockApplicable(block)) blocks.add(block);
        });
	}
	
	boolean blockApplicable(Block block) {
        boolean c1 = block == Blocks.LAVA || block == Blocks.CHEST || block == Blocks.FURNACE || block == Blocks.END_GATEWAY || block == Blocks.COMMAND_BLOCK || block == Blocks.ANCIENT_DEBRIS;
        boolean c2 = block instanceof OreBlock || block instanceof RedstoneOreBlock;
        return c1 || c2;
    }
	
	@Override
    public void onEnable() {
        mc.worldRenderer.reload();
    }

    @Override
    public void onDisable() {
        mc.worldRenderer.reload();
    }
}
