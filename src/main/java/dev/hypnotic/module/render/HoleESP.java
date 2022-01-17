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

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventRender3D;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.utils.render.QuadColor;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class HoleESP extends Mod {

	public BooleanSetting hide = new BooleanSetting("Hide Current Hole", false);
	
	public BooleanSetting bedrock = new BooleanSetting("Bedrock", true);
	public ColorSetting bedrockColor = new ColorSetting("Bedrock Color", Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue(), false);
	
	public BooleanSetting mixed = new BooleanSetting("Mixed", true);
	public ColorSetting mixedColor = new ColorSetting("Mixed Color", Color.YELLOW.getRed(), Color.YELLOW.getGreen(), Color.YELLOW.getBlue(), false);
	
	public BooleanSetting obi = new BooleanSetting("Obsidian", true);
	public ColorSetting obiColor = new ColorSetting("Obsidian Color", Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), false);

	private Map<BlockPos, float[]> holes = new HashMap<>();
	
	public HoleESP() {
		super("HoleESP", "Render a box on safe holes", Category.RENDER);
		addSettings(hide, bedrock, bedrockColor, mixed, mixedColor, obi, obiColor);
	}
	
	@Override
	public void onTick() {
		if (mc.player.age % 14 == 0) {
			holes.clear();

			int dist = 10;

			for (BlockPos pos : BlockPos.iterateOutwards(mc.player.getBlockPos(), dist, dist, dist)) {
				if (!mc.world.isInBuildLimit(pos.down())
						|| (mc.world.getBlockState(pos.down()).getBlock() != Blocks.BEDROCK
						&& mc.world.getBlockState(pos.down()).getBlock() != Blocks.OBSIDIAN)
						|| !mc.world.getBlockState(pos).getCollisionShape(mc.world, pos).isEmpty()
						|| !mc.world.getBlockState(pos.up(1)).getCollisionShape(mc.world, pos.up(1)).isEmpty()
						|| !mc.world.getBlockState(pos.up(2)).getCollisionShape(mc.world, pos.up(2)).isEmpty()) {
					continue;
				}

				if (hide.isEnabled()
						&& mc.player.getBoundingBox().getCenter().x > pos.getX() + 0.1
						&& mc.player.getBoundingBox().getCenter().x < pos.getX() + 0.9
						&& mc.player.getBoundingBox().getCenter().z > pos.getZ() + 0.1
						&& mc.player.getBoundingBox().getCenter().z < pos.getZ() + 0.9) {
					continue;
				}

				int bedrockCounter = 0;
				int obsidianCounter = 0;
				for (BlockPos pos1 : neighbours(pos)) {
					if (mc.world.getBlockState(pos1).getBlock() == Blocks.BEDROCK) {
						bedrockCounter++;
					} else if (mc.world.getBlockState(pos1).getBlock() == Blocks.OBSIDIAN) {
						obsidianCounter++;
					} else {
						break;
					}
				}

				if (bedrockCounter == 5 && bedrock.isEnabled()) {
					holes.put(pos.toImmutable(), bedrockColor.getRGBFloat());
				} else if (obsidianCounter == 5 && obi.isEnabled()) {
					holes.put(pos.toImmutable(), obiColor.getRGBFloat());
				} else if (bedrockCounter >= 1 && obsidianCounter >= 1
						&& bedrockCounter + obsidianCounter == 5 && mixed.isEnabled()) {
					holes.put(pos.toImmutable(), mixedColor.getRGBFloat());
				}
			}
		}
		super.onTick();
	}
	
	@EventTarget
	public void render3d(EventRender3D event) {
		holes.forEach((pos, color) -> {
			RenderUtils.drawBoxOutline(new Box(Vec3d.of(pos), Vec3d.of(pos).add(1, 0, 1)).stretch(0, 1, 0),
					QuadColor.single(color[0], color[1], color[2], 1f), 2);
			RenderUtils.drawBoxFill(new Box(Vec3d.of(pos), Vec3d.of(pos).add(1, 0, 1)).stretch(0, 1, 0),
					QuadColor.single(color[0], color[1], color[2], 0.5f));
		});
	}
	
	private BlockPos[] neighbours(BlockPos pos) {
		return new BlockPos[] {
				pos.west(), pos.east(), pos.south(), pos.north(), pos.down()
		};
	}
	
	@Override
	public void onTickDisabled() {
		// TODO Auto-generated method stub
		super.onTickDisabled();
	}
}
