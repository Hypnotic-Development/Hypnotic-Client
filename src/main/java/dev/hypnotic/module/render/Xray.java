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
