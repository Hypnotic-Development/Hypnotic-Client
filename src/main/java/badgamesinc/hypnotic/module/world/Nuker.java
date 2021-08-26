package badgamesinc.hypnotic.module.world;

import java.util.ArrayList;
import java.util.List;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventRender3D;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class Nuker extends Mod {

	final List<BlockPos> renders = new ArrayList<>();
	public NumberSetting radius = new NumberSetting("Radius", 5, 0, 6, 1);

	public Nuker() {
		super("Nuker", "brake stuff", Category.WORLD);
		addSettings(radius);
	}

	@Override
	public void onTick() {
		BlockPos ppos1 = mc.player.getBlockPos();
		renders.clear();
		for (double y = radius.getValue(); y > -radius.getValue() - 1; y--) {
            for (double x = -radius.getValue(); x < radius.getValue() + 1; x++) {
                for (double z = -radius.getValue(); z < radius.getValue() + 1; z++) {
//                    if (blocksBroken >= blocksPerTick.getValue()) break;
                    BlockPos vp = new BlockPos(x, y, z);
                    BlockPos np = ppos1.add(vp);
                    Vec3d vp1 = new Vec3d(np.getX(), np.getY(), np.getZ());
                    if (vp1.distanceTo(mc.player.getPos()) >= mc.interactionManager.getReachDistance() - 0.2)
                        continue;
                    BlockState bs = mc.world.getBlockState(np);
//                    boolean b = !ignoreXray.getValue() || !XRAY.blocks.contains(bs.getBlock());
                    if (!bs.isAir() && bs.getBlock() != Blocks.WATER && bs.getBlock() != Blocks.LAVA && bs.getBlock() != Blocks.BEDROCK && mc.world.getWorldBorder().contains(np)) {
                        renders.add(np);
//                        if (autoTool.getValue()) AutoTool.pick(bs);
//                        mc.player.swingHand(Hand.MAIN_HAND);
//                        if (!mc.player.getAbilities().creativeMode) {
                            mc.interactionManager.updateBlockBreakingProgress(np, Direction.DOWN);
//                        } //else mc.interactionManager.attackBlock(np, Direction.DOWN);
//                        Rotations.lookAtV3(new Vec3d(np.getX() + .5, np.getY() + .5, np.getZ() + .5));
//                        blocksBroken++;
                    }
                }
            }
        }
		super.onTick();
	}
	
	@EventTarget
	public void render3d(EventRender3D event) {
		for (BlockPos pos : renders) {
			RenderUtils.setup3DRender(true);
			RenderUtils.drawOutlineBox(event.getMatrices(), new Box(pos), -1);
			RenderUtils.end3DRender();
		}
	}
}
