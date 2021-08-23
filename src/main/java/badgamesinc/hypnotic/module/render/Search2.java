package badgamesinc.hypnotic.module.render;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventReceivePacket;
import badgamesinc.hypnotic.event.events.EventRender3D;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.MinPriorityThreadFactory;
import badgamesinc.hypnotic.utils.RotationUtils;
import badgamesinc.hypnotic.utils.render.BlockVertexCompiler;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import badgamesinc.hypnotic.utils.world.ChunkSearcher;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.gl.VertexBuffer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.network.packet.s2c.play.ChunkDeltaUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;;

public final class Search2 extends Mod
{
	public NumberSetting area = new NumberSetting("Chunk Radius", 11, 3, 35, 1);
	public NumberSetting limit = new NumberSetting("Limit", 100, 1, 1000, 10);
	public List<Block> blocks = new ArrayList<>();
	private int prevLimit;
	private boolean notify;
	
	private final HashMap<Chunk, ChunkSearcher> searchers = new HashMap<>();
	private final Set<Chunk> chunksToUpdate =
		Collections.synchronizedSet(new HashSet<>());
	private ExecutorService pool1 = MinPriorityThreadFactory.newFixedThreadPool();;
	
	private ForkJoinPool pool2 = new ForkJoinPool();
	private ForkJoinTask<HashSet<BlockPos>> getMatchingBlocksTask;
	private ForkJoinTask<ArrayList<int[]>> compileVerticesTask;
	
	private VertexBuffer vertexBuffer;
	private boolean bufferUpToDate;
	
	public Search2()
	{
		super("Search2", "Helps you to find specific blocks by\n"
			+ "highlighting them in rainbow color.", Category.RENDER);
		setCategory(Category.RENDER);
		addSetting(area);
		addSetting(limit);
		blocks.add(Blocks.NETHER_PORTAL);
	}
	
	@Override
	public void onEnable()
	{
		prevLimit = (int) limit.getValue();
		notify = true;
		
		pool1 = MinPriorityThreadFactory.newFixedThreadPool();
		pool2 = new ForkJoinPool();
		
		bufferUpToDate = false;
		super.onEnable();
	}
	
	@Override
	public void onDisable()
	{
		
		stopPool2Tasks();
		pool1.shutdownNow();
		pool2.shutdownNow();
		
		if(vertexBuffer != null)
			vertexBuffer.close();
		
		chunksToUpdate.clear();
		super.onDisable();
	}
	
	@EventTarget
	public void onReceivedPacket(EventReceivePacket event)
	{
		if (true) return;
		ClientPlayerEntity player = mc.player;
		ClientWorld world = mc.world;
		if(player == null || world == null)
			return;
		
		Packet<?> packet = event.getPacket();
		Chunk chunk;
		
		if(packet instanceof BlockUpdateS2CPacket)
		{
			BlockUpdateS2CPacket change = (BlockUpdateS2CPacket)event.getPacket();
			BlockPos pos = change.getPos();
			chunk = world.getChunk(pos);
			
		}else if(packet instanceof ChunkDeltaUpdateS2CPacket)
		{
			ChunkDeltaUpdateS2CPacket change = (ChunkDeltaUpdateS2CPacket)event.getPacket();
			ArrayList<BlockPos> changedBlocks = new ArrayList<>();
			change.visitUpdates((pos, state) -> changedBlocks.add(pos));
			if(changedBlocks.isEmpty())
				return;
			
			chunk = world.getChunk(changedBlocks.get(0));
			
		}else if(packet instanceof ChunkDataS2CPacket)
		{
			ChunkDataS2CPacket chunkData = (ChunkDataS2CPacket)event.getPacket();
			chunk = world.getChunk(chunkData.getX(), chunkData.getZ());
			
		}else
			return;
		
		chunksToUpdate.add(chunk);
	}
	
	@Override
	public void onTick()
	{
		BlockPos eyesPos = new BlockPos(RotationUtils.getEyesPos());
		
		ChunkPos center = getPlayerChunkPos(eyesPos);
		int range = (int) area.getValue();
		int dimensionId = mc.world.getRegistryKey().toString().hashCode();
		
		for (Block block : blocks) {
			addSearchersInRange(center, range, block, dimensionId);
			removeSearchersOutOfRange(center, range);
			replaceSearchersWithDifferences(block, dimensionId);
			replaceSearchersWithChunkUpdate(block, dimensionId);
		}
		
		if(!areAllChunkSearchersDone())
			return;
		
		checkIfLimitChanged();
		
		if(getMatchingBlocksTask == null)
			startGetMatchingBlocksTask(eyesPos);
		
		if(!getMatchingBlocksTask.isDone())
			return;
		
		if(compileVerticesTask == null)
			startCompileVerticesTask();
		
		if(!compileVerticesTask.isDone())
			return;
		
		if(!bufferUpToDate)
			setBufferFromTask();
	}
	
	@EventTarget
	public void onRender(EventRender3D event, float partialTicks)
	{
		// GL settings
		 MatrixStack matrixStack = event.getMatrices();
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		System.out.println("d");
		matrixStack.push();
		RenderUtils.applyRegionalRenderOffset(matrixStack);
		
		// generate rainbow color
		float x = System.currentTimeMillis() % 2000 / 1000F;
		float red = 0.5F + 0.5F * MathHelper.sin(x * (float)Math.PI);
		float green =
			0.5F + 0.5F * MathHelper.sin((x + 4F / 3F) * (float)Math.PI);
		float blue =
			0.5F + 0.5F * MathHelper.sin((x + 8F / 3F) * (float)Math.PI);
		
		RenderSystem.setShaderColor(red, green, blue, 0.5F);
		RenderSystem.setShader(GameRenderer::getPositionShader);
		
		if(vertexBuffer != null)
		{
			Matrix4f viewMatrix = matrixStack.peek().getModel();
			Matrix4f projMatrix = RenderSystem.getProjectionMatrix();
			Shader shader = RenderSystem.getShader();
			vertexBuffer.setShader(viewMatrix, projMatrix, shader);
		}
		BlockPos pos = new BlockPos(BlockPos.ORIGIN);
		RenderUtils.line(RenderUtils.center(), new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5), Color.CYAN, event.getMatrices());
		
		matrixStack.pop();
		
		// GL resets
		RenderSystem.setShaderColor(1, 1, 1, 1);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}
	
	private ChunkPos getPlayerChunkPos(BlockPos eyesPos)
	{
		int chunkX = eyesPos.getX() >> 4;
		int chunkZ = eyesPos.getZ() >> 4;
		return mc.world.getChunk(chunkX, chunkZ).getPos();
	}
	
	private void addSearchersInRange(ChunkPos center, int chunkRange,
		Block block, int dimensionId)
	{
		ArrayList<Chunk> chunksInRange = getChunksInRange(center, chunkRange);
		
		for(Chunk chunk : chunksInRange)
		{
			if(searchers.containsKey(chunk))
				continue;
			
			addSearcher(chunk, block, dimensionId);
		}
	}
	
	private ArrayList<Chunk> getChunksInRange(ChunkPos center, int chunkRange)
	{
		ArrayList<Chunk> chunksInRange = new ArrayList<>();
		
		for(int x = center.x - chunkRange; x <= center.x + chunkRange; x++)
			for(int z = center.z - chunkRange; z <= center.z + chunkRange; z++)
			{
				Chunk chunk = mc.world.getChunk(x, z);
				if(chunk instanceof EmptyChunk)
					continue;
				
				chunksInRange.add(chunk);
			}
		
		return chunksInRange;
	}
	
	private void removeSearchersOutOfRange(ChunkPos center, int chunkRange)
	{
		for(ChunkSearcher searcher : new ArrayList<>(searchers.values()))
		{
			ChunkPos searcherPos = searcher.getChunk().getPos();
			
			if(Math.abs(searcherPos.x - center.x) <= chunkRange
				&& Math.abs(searcherPos.z - center.z) <= chunkRange)
				continue;
			
			removeSearcher(searcher);
		}
	}
	
	private void replaceSearchersWithDifferences(Block currentBlock,
		int dimensionId)
	{
		for(ChunkSearcher oldSearcher : new ArrayList<>(searchers.values()))
		{
			if(currentBlock.equals(oldSearcher.getBlock())
				&& dimensionId == oldSearcher.getDimensionId())
				continue;
			
			removeSearcher(oldSearcher);
			addSearcher(oldSearcher.getChunk(), currentBlock, dimensionId);
		}
	}
	
	private void replaceSearchersWithChunkUpdate(Block currentBlock,
		int dimensionId)
	{
		synchronized(chunksToUpdate)
		{
			if(chunksToUpdate.isEmpty())
				return;
			
			for(Iterator<Chunk> itr = chunksToUpdate.iterator(); itr.hasNext();)
			{
				Chunk chunk = itr.next();
				
				ChunkSearcher oldSearcher = searchers.get(chunk);
				if(oldSearcher == null)
					continue;
				
				removeSearcher(oldSearcher);
				addSearcher(chunk, currentBlock, dimensionId);
				itr.remove();
			}
		}
	}
	
	private void addSearcher(Chunk chunk, Block block, int dimensionId)
	{
		stopPool2Tasks();
		
		ChunkSearcher searcher = new ChunkSearcher(chunk, block, dimensionId);
		searchers.put(chunk, searcher);
		searcher.startSearching(pool1);
	}
	
	private void removeSearcher(ChunkSearcher searcher)
	{
		stopPool2Tasks();
		
		searchers.remove(searcher.getChunk());
		searcher.cancelSearching();
	}
	
	private void stopPool2Tasks()
	{
		if(getMatchingBlocksTask != null)
		{
			getMatchingBlocksTask.cancel(true);
			getMatchingBlocksTask = null;
		}
		
		if(compileVerticesTask != null)
		{
			compileVerticesTask.cancel(true);
			compileVerticesTask = null;
		}
		
		bufferUpToDate = false;
	}
	
	private boolean areAllChunkSearchersDone()
	{
		for(ChunkSearcher searcher : searchers.values())
			if(searcher.getStatus() != ChunkSearcher.Status.DONE)
				return false;
			
		return true;
	}
	
	private void checkIfLimitChanged()
	{
		if(limit.getValue() != prevLimit)
		{
			stopPool2Tasks();
			notify = true;
			prevLimit = (int) limit.getValue();
		}
	}
	
	private void startGetMatchingBlocksTask(BlockPos eyesPos)
	{
		int maxBlocks = (int)Math.pow(10, limit.getValue());
		
		Callable<HashSet<BlockPos>> task = () -> searchers.values()
			.parallelStream()
			.flatMap(searcher -> searcher.getMatchingBlocks().stream())
			.sorted(Comparator
				.comparingInt(pos -> eyesPos.getManhattanDistance(pos)))
			.limit(maxBlocks).collect(Collectors.toCollection(HashSet::new));
		
		getMatchingBlocksTask = pool2.submit(task);
	}
	
	private HashSet<BlockPos> getMatchingBlocksFromTask()
	{
		HashSet<BlockPos> matchingBlocks = new HashSet<>();
		
		try
		{
			matchingBlocks = getMatchingBlocksTask.get();
			
		}catch(InterruptedException | ExecutionException e)
		{
			throw new RuntimeException(e);
		}
		
		int maxBlocks = (int)Math.pow(10, limit.getValue());
		
		if(matchingBlocks.size() < maxBlocks)
			notify = true;
		else if(notify)
		{
			
			notify = false;
		}
		
		return matchingBlocks;
	}
	
	private void startCompileVerticesTask()
	{
		HashSet<BlockPos> matchingBlocks = getMatchingBlocksFromTask();
		
		BlockPos camPos = mc.getBlockEntityRenderDispatcher().camera.getBlockPos();
		int regionX = (camPos.getX() >> 9) * 512;
		int regionZ = (camPos.getZ() >> 9) * 512;
		
		Callable<ArrayList<int[]>> task =
			BlockVertexCompiler.createTask(matchingBlocks, regionX, regionZ);
		
		compileVerticesTask = pool2.submit(task);
	}
	
	private void setBufferFromTask()
	{
		ArrayList<int[]> vertices = getVerticesFromTask();
		
		if(vertexBuffer != null)
			vertexBuffer.close();
		
		vertexBuffer = new VertexBuffer();
		
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS,
			VertexFormats.POSITION);
		
		for(int[] vertex : vertices)
			bufferBuilder.vertex(vertex[0], vertex[1], vertex[2]).next();
		
		bufferBuilder.end();
		vertexBuffer.upload(bufferBuilder);
		
		bufferUpToDate = true;
	}
	
	public ArrayList<int[]> getVerticesFromTask()
	{
		try
		{
			return compileVerticesTask.get();
			
		}catch(InterruptedException | ExecutionException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	private enum Area
	{
		D3("3x3 chunks", 1),
		D5("5x5 chunks", 2),
		D7("7x7 chunks", 3),
		D9("9x9 chunks", 4),
		D11("11x11 chunks", 5),
		D13("13x13 chunks", 6),
		D15("15x15 chunks", 7),
		D17("17x17 chunks", 8),
		D19("19x19 chunks", 9),
		D21("21x21 chunks", 10),
		D23("23x23 chunks", 11),
		D25("25x25 chunks", 12),
		D27("27x27 chunks", 13),
		D29("29x29 chunks", 14),
		D31("31x31 chunks", 15),
		D33("33x33 chunks", 16);
		
		private final String name;
		private final int chunkRange;
		
		private Area(String name, int chunkRange)
		{
			this.name = name;
			this.chunkRange = chunkRange;
		}
		
		@Override
		public String toString()
		{
			return name;
		}
	}
}