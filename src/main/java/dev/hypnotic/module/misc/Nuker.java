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
package dev.hypnotic.module.misc;

import java.util.ArrayList;
import java.util.List;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventRender3D;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.RotationUtils;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Nuker extends Mod {

	final List<BlockPos> renders = new ArrayList<>();
	public NumberSetting radius = new NumberSetting("Radius", 5, 0, 6, 1);
	public NumberSetting delay = new NumberSetting("Delay", 0, 0, 100, 1);

	public Nuker() {
		super("Nuker", "brake stuff", Category.MISC);
		addSettings(radius, delay);
	}

	int delayTicks = 0;
	@Override
	public void onTick() {
		BlockPos ppos1 = mc.player.getBlockPos();
		renders.clear();
		for (double y = radius.getValue(); y > -radius.getValue() - 1; y--) {
            for (double x = -radius.getValue(); x < radius.getValue() + 1; x++) {
                for (double z = -radius.getValue(); z < radius.getValue() + 1; z++) {
                    BlockPos vp = new BlockPos(x, y, z);
                    BlockPos np = ppos1.add(vp);
                    Vec3d vp1 = new Vec3d(np.getX(), np.getY(), np.getZ());
                    if (vp1.distanceTo(mc.player.getPos()) >= mc.interactionManager.getReachDistance() - 0.2)
                        continue;
                    BlockState bs = mc.world.getBlockState(np);
                    if (!bs.isAir() && bs.getBlock() != Blocks.WATER && bs.getBlock() != Blocks.LAVA && bs.getBlock() != Blocks.BEDROCK && mc.world.getWorldBorder().contains(np) && delayTicks <= 0) {
                        renders.add(np);
                            mc.interactionManager.updateBlockBreakingProgress(np, Direction.DOWN);
                          RotationUtils.setSilentYaw((float)RotationUtils.getYaw(np));
                          RotationUtils.setSilentPitch((float)RotationUtils.getPitch(np));
                          delayTicks = (int) delay.getValue();
                    }
                    if (delayTicks > 0) delayTicks--;
                }
            }
        }
		super.onTick();
	}
	
	@EventTarget
	public void render3d(EventRender3D event) {
		for (BlockPos pos : renders) {
			Vec3d render = RenderUtils.getRenderPosition(pos);
			RenderUtils.drawOutlineBox(event.getMatrices(), new Box(render.x, render.y, render.z, render.x + 1, render.y + 1, render.z + 1), -1);
		}
	}
}
