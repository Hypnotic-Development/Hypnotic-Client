package badgamesinc.hypnotic.module.render;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventRender3D;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.utils.render.QuadColor;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
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
//		if (getSetting(1).asToggle().state) {
//			int bottomMode = getSetting(1).asToggle().getChild(0).asMode().mode;
//			Direction[] excludeDirs = ArrayUtils.remove(Direction.values(), 0);
//
//			if (bottomMode == 0 || bottomMode == 2) {
//				holes.forEach((pos, color) -> {
//					RenderUtils.drawBoxFill(pos, QuadColor.single(color[0], color[1], color[2], getSetting(1).asToggle().getChild(2).asSlider().getValueFloat()), excludeDirs);
//				});
//			}
//
//			if (bottomMode == 0 || bottomMode == 1) {
//				holes.forEach((pos, color) -> {
//					RenderUtils.drawBoxOutline(pos, QuadColor.single(color[0], color[1], color[2], 1f), getSetting(1).asToggle().getChild(1).asSlider().getValueFloat(), excludeDirs);
//				});
//			}
//		}

//		if (getSetting(2).asToggle().state) {
//			float height = getSetting(2).asToggle().getChild(3).asSlider().getValueFloat();
//			Direction[] excludeDirs = new Direction[] { Direction.UP, Direction.DOWN, };

//			if (sideMode == 0 || sideMode == 1) {

				holes.forEach((pos, color) -> {
					Direction[] excludeDirs = new Direction[] { Direction.SOUTH, Direction.NORTH, Direction.EAST, Direction.WEST, Direction.UP};
//					Vec3d renderPos = RenderUtils.getRenderPosition(pos);
//					Box box = new Box(renderPos.x, renderPos.y - 1, renderPos.z, renderPos.x + 1, renderPos.y, renderPos.z + 1);
//					RenderUtils.drawFilledBox(event.getMatrices(), box, new Color(color.getRed(), color.getGreen(), color.getBlue(), 100).getRGB());
//					RenderUtils.drawOutlineBox(event.getMatrices(), box, color.getRGB());
					RenderUtils.drawBoxFill(new Box(Vec3d.of(pos), Vec3d.of(pos).add(1, 0, 1)).stretch(0, 1, 0),
							QuadColor.single(color[0], color[1], color[2], 0.5f), excludeDirs);
				});
//			} else {
//				if (sideMode == 2 || sideMode == 4) {
//					holes.forEach((pos, color) -> {
//						RenderUtils.drawBoxFill(new Box(Vec3d.of(pos), Vec3d.of(pos).add(1, 0, 1)).stretch(0, height, 0),
//								QuadColor.single(color[0], color[1], color[2], getSetting(2).asToggle().getChild(2).asSlider().getValueFloat()), excludeDirs);
//					});
//				}
//
//				if (sideMode == 2 || sideMode == 3) {
//					holes.forEach((pos, color) -> {
//						RenderUtils.drawBoxOutline(new Box(Vec3d.of(pos), Vec3d.of(pos).add(1, 0, 1)).stretch(0, height, 0),
//								QuadColor.single(color[0], color[1], color[2], 1f), getSetting(2).asToggle().getChild(1).asSlider().getValueFloat(), excludeDirs);
//					});
//				}
//			}
//		}
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
