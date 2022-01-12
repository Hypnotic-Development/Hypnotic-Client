package dev.hypnotic.module.render;

import java.awt.Color;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonSyntaxException;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventBlockEntityRender;
import dev.hypnotic.event.events.EventRender3D;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.render.QuadColor;
import dev.hypnotic.utils.render.RenderUtils;
import dev.hypnotic.utils.render.shader.OutlineShaderManager;
import dev.hypnotic.utils.render.shader.OutlineVertexConsumers;
import dev.hypnotic.utils.render.shader.ShaderEffectLoader;
import dev.hypnotic.utils.world.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

public class StorageESP extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Shader", "Shader", "Box", "Box-Fill");
	public NumberSetting shaderWidth = new NumberSetting("Shader Width", 0, 0, 2, 1);
	
	public BooleanSetting chest = new BooleanSetting("Chest", true);
	public ColorSetting chestColor = new ColorSetting("Chest Color", Integer.toHexString(Color.YELLOW.getRGB()));
	
	public BooleanSetting shulker = new BooleanSetting("Shulker", true);
	public ColorSetting shulkerColor = new ColorSetting("Shulker Color", Integer.toHexString(Color.PINK.getRGB()));
	
	public BooleanSetting dispenser = new BooleanSetting("Dispenser", true);
	public ColorSetting dispenserColor = new ColorSetting("Dispenser Color", Integer.toHexString(Color.GRAY.getRGB()));
	
	public BooleanSetting echest = new BooleanSetting("EChest", true);
	public ColorSetting echestColor = new ColorSetting("EChest Color", Integer.toHexString(Color.MAGENTA.getRGB()));
	
	public BooleanSetting furnace = new BooleanSetting("Furnace", true);
	public ColorSetting furnaceColor = new ColorSetting("Furnace Color", Integer.toHexString(Color.GRAY.getRGB()));
	
	private Map<BlockEntity, float[]> blockEntities = new HashMap<>();
	
	private Set<BlockPos> blacklist = new HashSet<>();

	private int lastWidth = -1;
	private int lastHeight = -1;
	private double lastShaderWidth;
	private boolean shaderUnloaded = true;
	
	public StorageESP() {
		super("StorageESP", "Renders a box on storage containers", Category.RENDER);
		addSettings(mode, shaderWidth, chest, chestColor, echest, echestColor, shulker, shulkerColor, dispenser, dispenserColor, furnace, furnaceColor);
	}
	
	@Override
	public void onTick() {
		blockEntities.clear();

		for (BlockEntity be: WorldUtils.blockEntities()) {
			float[] color = getColorForBlock(be);

			if (color != null) {
				blockEntities.put(be, color);
			}
		}

		super.onTick();
	}
	
	public boolean shouldRenderBlock(BlockEntity block) {
		if (chest.isEnabled() && block instanceof ChestBlockEntity) return true;
		if (shulker.isEnabled() && block instanceof ShulkerBoxBlockEntity) return true;
		if (echest.isEnabled() && block instanceof EnderChestBlockEntity) return true;
		if (dispenser.isEnabled() && block instanceof DispenserBlockEntity) return true;
		if (furnace.isEnabled() && block instanceof FurnaceBlockEntity) return true;
		return false;
	}
	
	public Color getBlockColor(BlockEntity block, int alpha) {
		if (block instanceof ChestBlockEntity) return new Color(
				Color.YELLOW.getRed(), 
				Color.YELLOW.getGreen(), 
				Color.YELLOW.getBlue(), alpha);
		if (block instanceof ShulkerBoxBlockEntity) return new Color(
				Color.PINK.getRed(), 
				Color.PINK.getGreen(), 
				Color.PINK.getBlue(), alpha);
		if (block instanceof EnderChestBlockEntity) return new Color(
				Color.MAGENTA.getRed(), 
				Color.MAGENTA.getGreen(), 
				Color.MAGENTA.getBlue(), alpha);
		if (block instanceof DispenserBlockEntity) return new Color(
				Color.GRAY.getRed(), 
				Color.GRAY.getGreen(), 
				Color.GRAY.getBlue(), alpha).brighter();
		if (block instanceof FurnaceBlockEntity) return new Color(
				Color.GRAY.getRed(), 
				Color.GRAY.getGreen(), 
				Color.GRAY.getBlue(), alpha).brighter();
		return new Color(255, 255, 255, alpha);
	}
	
	@EventTarget
	public void onRender(EventRender3D event) {
		if (mode.is("Box-Fill") || mode.is("Box")) {
			for (Entry<BlockEntity, float[]> e: blockEntities.entrySet()) {
				if (blacklist.contains(e.getKey().getPos())) {
					continue;
				}

				Box box = new Box(e.getKey().getPos());

				Block block = e.getKey().getCachedState().getBlock();

				if (block == Blocks.CHEST || block == Blocks.TRAPPED_CHEST || block == Blocks.ENDER_CHEST) {
					box = box.contract(0.06);
					box = box.offset(0, -0.06, 0);

					Direction dir = getChestDirection(e.getKey().getPos());
					if (dir != null) {
						box = box.expand(Math.abs(dir.getOffsetX()) / 2d, 0, Math.abs(dir.getOffsetZ()) / 2d);
						box = box.offset(dir.getOffsetX() / 2d, 0, dir.getOffsetZ() / 2d);
						blacklist.add(e.getKey().getPos().offset(dir));
					}
				}

				if (mode.is("Box-Fill")) {
					RenderUtils.drawBoxFill(box, QuadColor.single(e.getValue()[0], e.getValue()[1], e.getValue()[2], 1));
				}

				if (mode.is("Box-Fill") || mode.is("Box")) {
					RenderUtils.drawBoxOutline(box, QuadColor.single(e.getValue()[0], e.getValue()[1], e.getValue()[2], 1f), 1);
				}
			}

			blacklist.clear();
		}
	}

	@EventTarget
	public void onBlockEntityRenderPre(EventBlockEntityRender.PreAll event) throws JsonSyntaxException, IOException {
		if (mode.is("Shader")) {
			if (mc.getWindow().getFramebufferWidth() != lastWidth || mc.getWindow().getFramebufferHeight() != lastHeight
					|| lastShaderWidth != shaderWidth.getValue() || shaderUnloaded) {
				try {
					ShaderEffect shader = ShaderEffectLoader.load(mc.getFramebuffer(), "storageesp-shader",
							String.format(
									Locale.ENGLISH,
									IOUtils.toString(getClass().getResource("/assets/hypnotic/shaders/mc_outline.ujson"), StandardCharsets.UTF_8), 
									shaderWidth.getValue() / 2,
									shaderWidth.getValue() / 4));

					shader.setupDimensions(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
					lastWidth = mc.getWindow().getFramebufferWidth();
					lastHeight = mc.getWindow().getFramebufferHeight();
					lastShaderWidth = shaderWidth.getValue();
					shaderUnloaded = false;

					OutlineShaderManager.loadShader(shader);
				} catch (JsonSyntaxException | IOException e) {
					e.printStackTrace();
				}
			}
		} else if (!shaderUnloaded) {
			OutlineShaderManager.loadDefaultShader();
			shaderUnloaded = true;
		}
	}

	@EventTarget
	public void onBlockEntityRender(EventBlockEntityRender.PreAll event) {
		if (mode.is("Shader")) {
			try {
				for (Entry<BlockEntity, float[]> be: blockEntities.entrySet()) {
					BlockEntityRenderer<BlockEntity> beRenderer = mc.getBlockEntityRenderDispatcher().get(be.getKey());
		
					BlockPos pos = be.getKey().getPos();
					MatrixStack matrices = RenderUtils.matrixFrom(pos.getX(), pos.getY(), pos.getZ());
					if (beRenderer != null) {
						beRenderer.render(
								be.getKey(),
								mc.getTickDelta(),
								matrices,
								OutlineVertexConsumers.outlineOnlyProvider(be.getValue()[0], be.getValue()[1], be.getValue()[2], 1f),
								0xf000f0, OverlayTexture.DEFAULT_UV);
					} else {
						BlockState state = be.getKey().getCachedState();
		
						mc.getBlockRenderManager().getModelRenderer().render(
								mc.world,
								mc.getBlockRenderManager().getModel(state),
								state,
								BlockPos.ORIGIN,
								matrices,
								OutlineVertexConsumers.outlineOnlyConsumer(be.getValue()[0], be.getValue()[1], be.getValue()[2], 1f),
								false,
								new Random(),
								0L,
								OverlayTexture.DEFAULT_UV);
					}
				}
			} catch(Exception e) {
				
			}
		}
	}
	
	private float[] getColorForBlock(BlockEntity be) {
		if ((be instanceof ChestBlockEntity || be instanceof BarrelBlockEntity) && chest.isEnabled()) {
			return new float[] { 1F, 0.6F, 0.3F };
		} else if (be instanceof EnderChestBlockEntity && echest.isEnabled()) {
			return new float[] { 1F, 0.05F, 1F };
		} else if (be instanceof AbstractFurnaceBlockEntity && furnace.isEnabled()) {
			return new float[] { 0.5F, 0.5F, 0.5F };
		} else if (be instanceof DispenserBlockEntity && dispenser.isEnabled()) {
			return new float[] { 0.55F, 0.55F, 0.7F };
		} else if (be instanceof ShulkerBoxBlockEntity && shulker.isEnabled()) {
			return new float[] { 0.5F, 0.2F, 1F };
		}

		return null;
	}
	
	private Direction getChestDirection(BlockPos pos) {
		BlockState state = mc.world.getBlockState(pos);

		if (state.getBlock() instanceof ChestBlock && state.get(ChestBlock.CHEST_TYPE) != ChestType.SINGLE) {
			return ChestBlock.getFacing(state);
		}

		return null;
	}
	
	@Override
	public void onTickDisabled() {
		chestColor.setVisible(chest.isEnabled());
		echestColor.setVisible(echest.isEnabled());
		shulkerColor.setVisible(shulker.isEnabled());
		dispenserColor.setVisible(dispenser.isEnabled());
		furnaceColor.setVisible(furnace.isEnabled());
		shaderWidth.setVisible(mode.is("Shader"));
		super.onTickDisabled();
	}

    @Override
    public void onEnable() {
        super.onEnable();
    }

}
