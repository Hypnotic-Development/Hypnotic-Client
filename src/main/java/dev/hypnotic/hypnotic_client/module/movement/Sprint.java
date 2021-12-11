package dev.hypnotic.hypnotic_client.module.movement;

import org.lwjgl.glfw.GLFW;

import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.module.ModuleManager;
import dev.hypnotic.hypnotic_client.module.render.ClickGUIModule;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ModeSetting;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.player.PlayerUtils;

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
