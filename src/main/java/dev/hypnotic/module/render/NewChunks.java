package dev.hypnotic.module.render;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventReceivePacket;
import dev.hypnotic.event.events.EventRender3D;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.render.QuadColor;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;

public class NewChunks extends Mod {

	public NumberSetting yOffset = new NumberSetting("Y-Offset", -100, 100, 0, 1);
	public BooleanSetting remove = new BooleanSetting("Remove", true);
	public BooleanSetting fill = new BooleanSetting("Fill", true);
	public BooleanSetting newChunksSet = new BooleanSetting("NewChunks", true);
	public ColorSetting color = new ColorSetting("Color", 0.9f, 0.2f, 0.2f, 1f, false);
	private static final Direction[] skipDirs = new Direction[] { Direction.DOWN, Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH };
	private Set<ChunkPos> newChunks = Collections.synchronizedSet(new HashSet<>());
	private Set<ChunkPos> oldChunks = Collections.synchronizedSet(new HashSet<>());

	public NewChunks() {
		super("NewChunks", "Renders a border around newley generated chunks", Category.RENDER);
		addSettings(yOffset, remove, fill, newChunksSet, color);
		newChunksSet.addChild(color);
	}

	@Override
	public void onDisable() {
		if (remove.isEnabled()) {
			newChunks.clear();
			oldChunks.clear();
		}
		super.onDisable();
	}

	@EventTarget
	public void onReadPacket(EventReceivePacket event) {
		Direction[] searchDirs = new Direction[] { Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.UP };
		if (event.getPacket() instanceof ChunkDeltaUpdateS2CPacket) {
			ChunkDeltaUpdateS2CPacket packet = (ChunkDeltaUpdateS2CPacket) event.getPacket();
			packet.visitUpdates((pos, state) -> {
				if (!state.getFluidState().isEmpty() && !state.getFluidState().isStill()) {
					ChunkPos chunkPos = new ChunkPos(pos);

					for (Direction dir: searchDirs) {
						if (mc.world.getBlockState(pos.offset(dir)).getFluidState().isStill() && !oldChunks.contains(chunkPos)) {
							newChunks.add(chunkPos);
							return;
						}
					}
				}
			});
		} else if (event.getPacket() instanceof BlockUpdateS2CPacket) {
			BlockUpdateS2CPacket packet = (BlockUpdateS2CPacket) event.getPacket();

			if (!packet.getState().getFluidState().isEmpty() && !packet.getState().getFluidState().isStill()) {
				ChunkPos chunkPos = new ChunkPos(packet.getPos());

				for (Direction dir: searchDirs) {
					if (mc.world.getBlockState(packet.getPos().offset(dir)).getFluidState().isStill() && !oldChunks.contains(chunkPos)) {
						newChunks.add(chunkPos);
						return;
					}
				}
			}
		}
	}

	@EventTarget
	public void onWorldRender(EventRender3D event) {
		int renderY = (int) (mc.world.getBottomY() + yOffset.getValue());

		if (newChunksSet.isEnabled()) {
			int color = this.color.getRGB();
			QuadColor outlineColor = QuadColor.single(0xff000000 | color);
			QuadColor fillColor = QuadColor.single(((int) (100) << 24) | color);

			synchronized (newChunks) {
				for (ChunkPos c: newChunks) {
					if (mc.getCameraEntity().getBlockPos().isWithinDistance(c.getStartPos(), 1024)) {
						Box box = new Box(
								c.getStartX(), renderY, c.getStartZ(),
								c.getStartX() + 16, renderY, c.getStartZ() + 16);

						if (fill.isEnabled()) {
							RenderUtils.drawBoxFill(box, fillColor, skipDirs);
						}
	
						RenderUtils.drawBoxOutline(box, outlineColor, 2f, skipDirs);
					}
				}
			}
		}
	}
}
