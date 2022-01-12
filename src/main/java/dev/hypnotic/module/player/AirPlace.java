package dev.hypnotic.module.player;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventRender3D;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.render.QuadColor;
import dev.hypnotic.utils.render.RenderUtils;
import dev.hypnotic.utils.world.WorldUtils;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;

public class AirPlace extends Mod {

	public NumberSetting range = new NumberSetting("Range", 4, 1, 6, 0.1);
	public NumberSetting delay = new NumberSetting("Place Delay", 25, 0, 100, 1);
	public BooleanSetting outline = new BooleanSetting("Render Outline", true);
	public ColorSetting outlineColor = new ColorSetting("Outline Color", 0, 0.71f, 0.64f, 1f, true);
	
	private BlockPos placePos;
	
	public AirPlace() {
		super("AirPlace", "Places a block in the air where you are looking", Category.PLAYER);
		addSettings(range, delay, outline, outlineColor);
	}
	
	int delayTicks;
	@Override
	public void onTick() {
		System.out.println(delayTicks);
		if (mc.world != null && mc.player != null) {
			placePos = new BlockPos(mc.getCameraEntity().raycast(range.getValue(), 0, false).getPos());
				if (placePos != null && mc.world.getBlockState(placePos).getMaterial().isReplaceable() && mc.options.keyUse.isPressed()) {
					if (delayTicks < delay.getValue() * 10) {
						delayTicks++;
					} else if (delayTicks == delay.getValue() * 10) {
						if (mc.player.getMainHandStack().getItem() instanceof BlockItem) {
							WorldUtils.placeBlockMainHand(placePos, false, true);
						} else if (mc.player.getOffHandStack().getItem() instanceof BlockItem) {
							WorldUtils.placeBlockNoRotate(Hand.OFF_HAND, placePos, true);
						}
						delayTicks = 0;
					}
				}
		}
		super.onTick();
	}
	
	@EventTarget
	public void render(EventRender3D event) {
		if (outline.isEnabled() && placePos != null && mc.world.getBlockState(placePos).getMaterial().isReplaceable() && (mc.player.getMainHandStack().getItem() instanceof BlockItem || mc.player.getOffHandStack().getItem() instanceof BlockItem)) {
			float[] c = outlineColor.getRGBFloat();
			RenderUtils.drawBoxOutline(placePos, QuadColor.single(c[0], c[1], c[2], 1f), 1);
		}
	}
	
	@Override
	public void onTickDisabled() {
		outlineColor.setVisible(outline.isEnabled());
		super.onTickDisabled();
	}
}
