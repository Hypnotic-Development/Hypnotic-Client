package badgamesinc.hypnotic.module.render;

import java.awt.Color;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventRender3D;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import badgamesinc.hypnotic.utils.world.WorldUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.block.entity.EnderChestBlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class StorageESP extends Mod {

	public BooleanSetting chest = new BooleanSetting("Chest", true);
	public BooleanSetting shulker = new BooleanSetting("Shulker", true);
	public BooleanSetting dispenser = new BooleanSetting("Dispenser", true);
	public BooleanSetting echest = new BooleanSetting("EChest", true);
	public BooleanSetting furnace = new BooleanSetting("Furnace", true);
	
	public StorageESP() {
		super("StorageESP", "Renders a box on storage containers", Category.RENDER);
		addSettings(chest, echest, shulker, dispenser, furnace);
	}

	@EventTarget
	public void eventRender3D(EventRender3D event) {
		for (BlockEntity block : WorldUtils.blockEntities()) {
			if (shouldRenderBlock(block)) {
				Vec3d renderPos = RenderUtils.getRenderPosition(block.getPos());
                Box box = new Box(renderPos.x, renderPos.y, renderPos.z, renderPos.x + 1, renderPos.y + 1, renderPos.z + 1);
                RenderUtils.setup3DRender(true);
                RenderUtils.drawOutlineBox(event.getMatrices(), box, getBlockColor(block, 255), true);
				RenderUtils.drawFilledBox(event.getMatrices(), box, getBlockColor(block, 80), true);
				RenderUtils.end3DRender();
			}
		}
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
		if (block instanceof ChestBlockEntity) return new Color(255, 255, 255, alpha);
		if (block instanceof ShulkerBoxBlockEntity) return new Color(Color.PINK.getRed(), Color.PINK.getGreen(), Color.PINK.getBlue(), alpha);
		if (block instanceof EnderChestBlockEntity) return new Color(Color.MAGENTA.getRed(), Color.MAGENTA.getGreen(), Color.MAGENTA.getBlue(), alpha);
		if (block instanceof DispenserBlockEntity) return new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), alpha).brighter();
		if (block instanceof FurnaceBlockEntity) return new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), alpha).brighter();
		return new Color(255, 255, 255, alpha);
	}
}
