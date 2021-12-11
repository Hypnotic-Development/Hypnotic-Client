package dev.hypnotic.hypnotic_client.module.render;

import dev.hypnotic.hypnotic_client.event.EventTarget;
import dev.hypnotic.hypnotic_client.event.events.EventRender3D;
import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ColorSetting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ModeSetting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.NumberSetting;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.render.QuadColor;
import dev.hypnotic.hypnotic_client.utils.render.RenderUtils;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class BlockOutline extends Mod {

	public ModeSetting mode = new ModeSetting("Outline Mode", "Box", "Box", "Box-Fill", "Face", "Face-Fill");
	public NumberSetting width = new NumberSetting("Line Width", 3, 1, 10, 1);
	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	
	public BlockOutline() {
		super("BlockOutline", "Renders a better outline around the block you are looking at", Category.RENDER);
		addSettings(mode, width, color);
	}
	
	@EventTarget
	public void render3d(EventRender3D event) {
		HitResult target = mc.crosshairTarget;
		if (target != null) {
			if (target.getType() == Type.BLOCK) {
				BlockPos pos = (BlockPos) ((BlockHitResult) target).getBlockPos();
				float[] c = color.getRGBFloat();
				switch(mode.getSelected()) {
					case "Box":
						RenderUtils.drawBoxOutline(pos, QuadColor.single(c[0], c[1], c[2], 1), (int) width.getValue());
						break;
					case "Box-Fill":
						RenderUtils.drawBoxOutline(pos, QuadColor.single(c[0], c[1], c[2], 1), (int) width.getValue());
						RenderUtils.drawBoxFill(pos, QuadColor.single(c[0], c[1], c[2], 0.4f));
						break;
					case "Face":
						RenderUtils.drawFaceOutline(pos, QuadColor.single(c[0], c[1], c[2], 1), (int) width.getValue(), (Direction) ((BlockHitResult) target).getSide());
						break;
					case "Face-Fill":
						RenderUtils.drawFaceFill(pos, QuadColor.single(c[0], c[1], c[2], 0.4f), (Direction) ((BlockHitResult) target).getSide());
						RenderUtils.drawFaceOutline(pos, QuadColor.single(c[0], c[1], c[2], 1), (int) width.getValue(), (Direction) ((BlockHitResult) target).getSide());
						break;
				}
				
			}
		}
	}
	
	@Override
	public void onTickDisabled() {
		super.onTickDisabled();
	}
}
