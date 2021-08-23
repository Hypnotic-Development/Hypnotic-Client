package badgamesinc.hypnotic.module.render;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventRenderHeldItem;
import badgamesinc.hypnotic.event.events.EventRenderItem;
import badgamesinc.hypnotic.event.events.EventSwingHand;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.combat.Killaura;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Quaternion;

public class OldBlock extends Mod {

	public ModeSetting animation = new ModeSetting("Block Animation", "1.7(ish)", "1.7(ish)", "Slide", "Sigma", "Swing");
	public OldBlock() {
		super("OldBlock", "", Category.RENDER);
		addSettings(animation);
	}

	int swingTicks = 0;
	boolean swingHasElapsed = true;
	@EventTarget
    private void runMethod(EventRenderItem event) {
        if (event.getType().isFirstPerson()) {
        	if (!mc.player.isUsingItem()) return;
            MatrixStack matrixStack = event.getMatrixStack();
            boolean offHand = event.isLeftHanded() ? event.getType() == ModelTransformation.Mode.FIRST_PERSON_RIGHT_HAND : event.getType() == ModelTransformation.Mode.FIRST_PERSON_LEFT_HAND;
                switch (event.getRenderTime()) {
                    case PRE -> {
                        matrixStack.push();
                        if (!offHand) {
                            if (event.getItemStack().getItem() instanceof SwordItem) {
                            	//for some reason this works sometimes
                                matrixStack.multiply(new Quaternion(-270f, 0f, 270f, true));
                                matrixStack.multiply(new Quaternion(-120f, 270f, -150f, true));
                                matrixStack.multiply(new Quaternion(-70f, -108f, 90f, true));
                               
//                                matrixStack.multiply(new Quaternion(-100f, -90f, 230f, true));
//                                matrixStack.multiply(new Quaternion(-10f, -140f, 260f, true));
//                                matrixStack.multiply(new Quaternion(-15f, -196f, 260f, true));
                                if (swingTicks < 60 && !swingHasElapsed) {
                                	swingTicks+=7;
                                }
                                //only plays with killaura
                                if (Killaura.target != null && !Killaura.target.isDead()) {
                                	switch(animation.getSelected()) {
	                                	case "1.7(ish)":
	                                		matrixStack.multiply(new Quaternion(-swingTicks, 0, 0, true));
		                                	break;
	                                	case "Slide":
		                                	matrixStack.multiply(new Quaternion(-swingTicks, swingTicks, swingTicks / 2, true));
		                                	break;
	                                	case "Sigma":
		                                	matrixStack.multiply(new Quaternion(-swingTicks * 0.15f, 0, 0, true));
		                                	matrixStack.multiply(new Quaternion(0, 0, swingTicks * 0.5f, true));
		                                	break;
	                                	case "Swing":
	                                		mc.player.swingHand(Hand.MAIN_HAND);
	                                		break;
                                	}
                                }
                                if (swingTicks >= 60) swingHasElapsed = true;
                                if (swingHasElapsed) {
                                	swingTicks-=7;
                                	if (swingTicks <= 0) {
                                		swingHasElapsed = false;
                                	}
                                }
                                ArmCustomize arm = ModuleManager.INSTANCE.getModule(ArmCustomize.class);
                               if (arm.isEnabled()) matrixStack.translate(arm.mainX.getValue(), arm.mainY.getValue(), arm.mainZ.getValue());
                            }
                        }
                    }
                    case POST -> matrixStack.pop();
                }

        }
    }

    @EventTarget
    private void renderHeldItem(EventRenderHeldItem eventRenderHeldItem) {
        if (eventRenderHeldItem.getHand() == Hand.OFF_HAND && eventRenderHeldItem.getItemStack().getItem() instanceof ShieldItem && mc.player.getInventory().getMainHandStack().getItem() instanceof SwordItem && mc.options.getPerspective() == Perspective.FIRST_PERSON)
            eventRenderHeldItem.setCancelled(true);
    }
    
    @EventTarget
    public void onSwingHand(EventSwingHand event) {
    	if (mc.options.keyUse.isPressed() && mc.player.getInventory().getMainHandStack().getItem() instanceof SwordItem && !animation.is("Swing"))
    	event.setCancelled(true);
    }
}
