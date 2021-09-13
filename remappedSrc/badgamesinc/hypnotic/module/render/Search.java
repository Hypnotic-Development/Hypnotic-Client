package badgamesinc.hypnotic.module.render;

import java.awt.Color;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.tuple.Pair;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventReceivePacket;
import badgamesinc.hypnotic.event.events.EventRender3D;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import badgamesinc.hypnotic.utils.world.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.network.packet.s2c.play.UnloadChunkS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.chunk.Chunk;


public class Search extends Mod {

	public Set<Block> blocks = new HashSet<>();
	private Set<BlockPos> foundBlocks = new HashSet<>();

	private ExecutorService chunkSearchers = Executors.newFixedThreadPool(4);
	private Map<ChunkPos, Future<Set<BlockPos>>> chunkFutures = new HashMap<>();

	private Queue<ChunkPos> queuedChunks = new ArrayDeque<>();
	private Queue<ChunkPos> queuedUnloads = new ArrayDeque<>();
	private Queue<Pair<BlockPos, BlockState>> queuedBlocks = new ArrayDeque<>();

	private Set<Block> prevBlockList = new HashSet<>();

	private int oldViewDistance = -1;
	
	public Search() {
		super("Search", "look for funny things", Category.RENDER);
		blocks.add(Blocks.NETHER_PORTAL);
	}
	
	@Override
	public void onTick() {
		try {
			Set<Block> blockList = (Set<Block>) blocks;
	
			if (!prevBlockList.equals(blockList) || oldViewDistance != mc.options.viewDistance) {
				reset();
	
				for (Chunk chunk: WorldUtils.getLoadedChunks()) {
					submitChunk(chunk.getPos(), chunk);
				}
	
				prevBlockList = new HashSet<>(blockList);
				oldViewDistance = mc.options.viewDistance;
				return;
			}
	
	
			while (!queuedBlocks.isEmpty()) {
				Pair<BlockPos, BlockState> blockPair = queuedBlocks.poll();
	
				if (blocks.contains(blockPair.getRight().getBlock())) {
					foundBlocks.add(blockPair.getLeft());
				} else {
					foundBlocks.remove(blockPair.getLeft());
				}
			}
	
			while (!queuedUnloads.isEmpty()) {
				ChunkPos chunkPos = queuedUnloads.poll();
				queuedChunks.remove(chunkPos);
	
				for (BlockPos pos: new HashSet<>(foundBlocks)) {
					if (pos.getX() >= chunkPos.getStartX()
							&& pos.getX() <= chunkPos.getEndX()
							&& pos.getZ() >= chunkPos.getStartZ()
							&& pos.getZ() <= chunkPos.getEndZ()) {
						foundBlocks.remove(pos);
					}
				}
			}
	
			while (!queuedChunks.isEmpty()) {
				submitChunk(queuedChunks.poll());
			}
	
			for (Entry<ChunkPos, Future<Set<BlockPos>>> f: new HashMap<>(chunkFutures).entrySet()) {
				if (f.getValue().isDone()) {
					try {
						foundBlocks.addAll(f.getValue().get());
	
						chunkFutures.remove(f.getKey());
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
			}
		} catch(Exception e) {
			System.out.println("error");
			e.printStackTrace();
		}
	}
	
	@EventTarget
	public void receivePacket(EventReceivePacket event) {
		if (event.getPacket() instanceof DisconnectS2CPacket
				|| event.getPacket() instanceof GameJoinS2CPacket
				|| event.getPacket() instanceof PlayerRespawnS2CPacket) {
			reset();
		} else if (event.getPacket() instanceof BlockUpdateS2CPacket) {
			BlockUpdateS2CPacket packet = (BlockUpdateS2CPacket) event.getPacket();

			queuedBlocks.add(Pair.of(packet.getPos(), packet.getState()));
		} else if (event.getPacket() instanceof ExplosionS2CPacket) {
			ExplosionS2CPacket packet = (ExplosionS2CPacket) event.getPacket();

			for (BlockPos pos: packet.getAffectedBlocks()) {
				queuedBlocks.add(Pair.of(pos, Blocks.AIR.getDefaultState()));
			}
		} else if (event.getPacket() instanceof ChunkDeltaUpdateS2CPacket) {
			ChunkDeltaUpdateS2CPacket packet = (ChunkDeltaUpdateS2CPacket) event.getPacket();

			packet.visitUpdates((pos, state) -> queuedBlocks.add(Pair.of(pos.toImmutable(), state)));
		} else if (event.getPacket() instanceof ChunkDataS2CPacket) {
			ChunkDataS2CPacket packet = (ChunkDataS2CPacket) event.getPacket();

			ChunkPos cp = new ChunkPos(packet.getX(), packet.getZ());
			queuedChunks.add(cp);
			queuedUnloads.remove(cp);
		} else if (event.getPacket() instanceof UnloadChunkS2CPacket) {
			UnloadChunkS2CPacket packet = (UnloadChunkS2CPacket) event.getPacket();

			queuedUnloads.add(new ChunkPos(packet.getX(), packet.getZ()));
		}
	}

	@EventTarget
	public void render3d(EventRender3D event) {
		for (BlockPos pos : foundBlocks) {
			BlockState state = mc.world.getBlockState(pos);
			VoxelShape voxelShape = state.getOutlineShape(mc.world, pos);
			if (voxelShape.isEmpty()) {
				voxelShape = VoxelShapes.cuboid(0, 0, 0, 1, 1, 1);
			}
			RenderUtils.line(RenderUtils.center(), new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), Color.CYAN, event.getMatrices());
		}
	}
	
	@Override
	public void onDisable() {
		reset();
		super.onDisable();
	}
	
	private void submitChunk(ChunkPos pos) {
		submitChunk(pos, mc.world.getChunk(pos.x, pos.z));
	}
	
	private void submitChunk(ChunkPos pos, Chunk chunk) {
		chunkFutures.put(chunk.getPos(), chunkSearchers.submit(new Callable<Set<BlockPos>>() {

			@Override
			public Set<BlockPos> call() {
				Set<BlockPos> found = new HashSet<>();
				
				for (int x = 0; x < 16; x++) {
					for (int y = mc.world.getBottomY(); y <= mc.world.getTopY(); y++) {
						for (int z = 0; z < 16; z++) {
							BlockPos pos = new BlockPos(chunk.getPos().x * 16 + x, y, chunk.getPos().z * 16 + z);
							BlockState state = chunk.getBlockState(pos);

							if (blocks.contains(state.getBlock())) {
								found.add(pos);
							}
						}
					}
				}

				return found;
			}
		}));
	}
	
	private void reset() {
		chunkSearchers.shutdownNow();
		chunkSearchers = Executors.newFixedThreadPool(4);

		chunkFutures.clear();
		foundBlocks.clear();
		queuedChunks.clear();
		queuedUnloads.clear();
		prevBlockList.clear();
	}
}
