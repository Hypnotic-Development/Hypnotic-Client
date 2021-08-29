package badgamesinc.hypnotic.module.render;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventReceivePacket;
import badgamesinc.hypnotic.event.events.EventRender3D;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.render.QuadColor;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.chunk.WorldChunk;

public class NewChunks extends Mod {

	public NumberSetting yOffset = new NumberSetting("Y-Offset", -100, 100, 0, 1);
	public BooleanSetting remove = new BooleanSetting("Remove", true);
	public BooleanSetting fill = new BooleanSetting("Fill", true);
	public BooleanSetting newChunksSet = new BooleanSetting("NewChunks", true);
	public BooleanSetting oldChunksSet = new BooleanSetting("OldChunks", true);
	public ColorSetting color = new ColorSetting("Color", 0.9f, 0.2f, 0.2f, false);
	private static final Direction[] skipDirs = new Direction[] { Direction.DOWN, Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH };
	private Set<ChunkPos> newChunks = Collections.synchronizedSet(new HashSet<>());
	private Set<ChunkPos> oldChunks = Collections.synchronizedSet(new HashSet<>());

	public NewChunks() {
		super("NewChunks", "Renders a border around newley generated chunks", Category.RENDER);
		addSettings(yOffset, remove, fill, newChunksSet, oldChunksSet, color);
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
		} else if (event.getPacket() instanceof ChunkDataS2CPacket && mc.world != null) {
			ChunkDataS2CPacket packet = (ChunkDataS2CPacket) event.getPacket();

			ChunkPos pos = new ChunkPos(packet.getX(), packet.getZ());
			
			if (!newChunks.contains(pos) && mc.world.getChunkManager().getChunk(packet.getX(), packet.getZ()) == null) {
				WorldChunk chunk = new WorldChunk(mc.world, pos, null);
				chunk.loadFromPacket(null, packet.getReadBuffer(), new NbtCompound(), packet.getVerticalStripBitmask());
				
				for (int x = 0; x < 16; x++) {
					for (int y = 0; y < mc.world.getHeight(); y++) {
						for (int z = 0; z < 16; z++) {
							FluidState fluid = chunk.getFluidState(x, y, z);
							
							if (!fluid.isEmpty() && !fluid.isStill()) {
								oldChunks.add(pos);
								return;
							}
						}
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

		if (oldChunksSet.isEnabled()) {
			int color = this.color.getRGB();
			QuadColor outlineColor = QuadColor.single(0xff000000 | color);
			QuadColor fillColor = QuadColor.single(((int) (100) << 24) | color);

			synchronized (oldChunks) {
				for (ChunkPos c: oldChunks) {
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
