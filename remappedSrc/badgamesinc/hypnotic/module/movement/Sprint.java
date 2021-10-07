package badgamesinc.hypnotic.module.movement;

import org.lwjgl.glfw.GLFW;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.render.ClickGUIModule;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.player.PlayerUtils;

public class Sprint extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Omni", "Omni", "Vanilla");
	
    public Sprint() {
        super("Sprint", "Makes you sprint all the time", Category.MOVEMENT);
        addSetting(mode);
    }

    @Override
    public void onMotion() {
    	ModuleManager.INSTANCE.getModule(ClickGUIModule.class).setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
    	this.setDisplayName("Sprint " + ColorUtils.gray + mode.getSelected());
    	if (mc.player != null && mc.player.input != null) {
	    	if (mode.is("Vanilla")) mc.options.keySprint.setPressed(true);
	    	else if (mode.is("Omni") && PlayerUtils.isMoving()) mc.player.setSprinting(true);
    	}
    	super.onMotion();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}