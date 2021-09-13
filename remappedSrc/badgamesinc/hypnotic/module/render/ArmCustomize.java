package badgamesinc.hypnotic.module.render;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventRenderHand;
import badgamesinc.hypnotic.event.events.EventRenderItem;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import net.minecraft.item.ItemGroup;

public class ArmCustomize extends Mod {

	public NumberSetting scale = new NumberSetting("Hand Scale", 1, 0, 2, 0.01);
	public NumberSetting mainX = new NumberSetting("Mainhand X", 0, -2, 2, 0.01);
	public NumberSetting mainY = new NumberSetting("Mainhand Y", 0, -2, 2, 0.01);
	public NumberSetting mainZ = new NumberSetting("Mainhand Z", 0, -2, 2, 0.01);
	public NumberSetting offX = new NumberSetting("Offhand X", 0, -2, 2, 0.01);
	public NumberSetting offY = new NumberSetting("Offhand Y", 0, -2, 2, 0.01);
	public NumberSetting offZ = new NumberSetting("Offhand Z", 0, -2, 2, 0.01);
	
	public ArmCustomize() {
		super("ArmCustomize", "Change how your arm looks", Category.RENDER);
		addSettings(scale, mainX, mainY, mainZ, offX, offY, offZ);
	}
	
	@EventTarget
	public void renderHeldItem(EventRenderItem event) {
		float main = (float) scale.getValue();
		float off = (float) scale.getValue();
		switch (event.getRenderTime()) {
					case PRE:
						event.getMatrixStack().push();
						switch (event.getType()) {
					        case FIRST_PERSON_RIGHT_HAND:
					        	event.getMatrixStack().translate(mainX.getValue(), mainY.getValue(), mc.player.getMainHandStack().getItem().getGroup() == ItemGroup.FOOD && mc.player.isUsingItem() ? 0 : mainZ.getValue());
								event.getMatrixStack().scale(main, main, main);
								break;
					        case FIRST_PERSON_LEFT_HAND:
					        	event.getMatrixStack().translate(offX.getValue(), offY.getValue(), mc.player.getMainHandStack().getItem().getGroup() == ItemGroup.FOOD && mc.player.isUsingItem() ? 0 : -offZ.getValue());
					        	event.getMatrixStack().scale(off, off, off);
					        	break;
							case FIXED:
								break;
							case GROUND:
								break;
							case GUI:
								break;
							case HEAD:
								break;
							case NONE:
								break;
							case THIRD_PERSON_LEFT_HAND:
								
								break;
							case THIRD_PERSON_RIGHT_HAND:
								break;
							default:
								break;
						}
						break;
					case POST:
						event.getMatrixStack().pop();
						break;
		}
	}
	
	@EventTarget
	public void renderHand(EventRenderHand event) {
	}
}
