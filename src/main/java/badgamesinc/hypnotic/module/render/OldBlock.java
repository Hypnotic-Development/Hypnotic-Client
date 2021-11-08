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
import badgamesinc.hypnotic.utils.Timer;
import net.minecraft.client.option.Perspective;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;

public class OldBlock extends Mod {

	public ModeSetting animation = new ModeSetting("Block Animation", "1.7(ish)", "1.7(ish)", "Slide", "Sigma", "Swing");
	public OldBlock() {
		super("OldBlock", "", Category.RENDER);
		addSettings(animation);
	}

	public float swingTicks = 0;
	public boolean swingHasElapsed = true;
	
	@Override
	public void onTick() {
		
		super.onTick();
	}
	
	
	public static Timer animationTimer = new Timer();
	@EventTarget
    private void runMethod(EventRenderItem event) {
		boolean shouldmove = animationTimer.hasTimeElapsed(1000 / 75, true);
		if (ModuleManager.INSTANCE.getModule(Killaura.class).autoBlockMode.is("Visual") && Killaura.target != null ? Killaura.target == null : !mc.player.isUsingItem()) return;
        if ((event.getItemStack().getItem() instanceof AxeItem || mc.player.getMainHandStack().getItem() instanceof SwordItem) && shouldmove) {
        	if (swingTicks < 60 && !swingHasElapsed) {
        		swingTicks+=7;
        	}
        	if (swingTicks >= 60) swingHasElapsed = true;
            if (swingHasElapsed) {
            	swingTicks-=7;
            	if (swingTicks <= 0) {
            		swingHasElapsed = false;
            	}
            }
        }
    }

    @EventTarget
    private void renderHeldItem(EventRenderHeldItem eventRenderHeldItem) {
        if (eventRenderHeldItem.getHand() == Hand.OFF_HAND && eventRenderHeldItem.getItemStack().getItem() instanceof ShieldItem && (mc.player.getMainHandStack().getItem() instanceof AxeItem || mc.player.getMainHandStack().getItem() instanceof SwordItem) && mc.options.getPerspective() == Perspective.FIRST_PERSON)
            eventRenderHeldItem.setCancelled(true);
    }
    
    @EventTarget
    public void onSwingHand(EventSwingHand event) {
    	if (mc.options.keyUse.isPressed() && (mc.player.getMainHandStack().getItem() instanceof SwordItem || mc.player.getMainHandStack().getItem() instanceof AxeItem ) && !animation.is("Swing"))
    	event.setCancelled(true);
    }
}
