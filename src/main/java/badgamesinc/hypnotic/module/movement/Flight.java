package badgamesinc.hypnotic.module.movement;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import net.minecraft.util.math.Vec3d;

import org.lwjgl.glfw.GLFW;

public class Flight extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Velocity", "Velocity", "Vanilla");
	public NumberSetting speed = new NumberSetting("Speed", 1, 0, 10, 0.1);
	
    public Flight() {
        super("Flight", "Allows you to fly", Category.MOVEMENT);
        this.setKey(GLFW.GLFW_KEY_G);
        addSettings(mode, speed);
    }

    @Override
    public void onTick() {
    	this.setDisplayName("Flight " + ColorUtils.gray + mode.getSelected());
    	if (mc.player == null)
    		return;
    	if (mode.is("Vanilla")) {
    		mc.player.getAbilities().flying = true;
    	} else {
    		mc.player.getAbilities().flying = false;
    		mc.player.flyingSpeed = (float) speed.getValue();
    		
    		mc.player.setVelocity(0, 0, 0);
    		Vec3d velocity = mc.player.getVelocity();
    		
    		if(mc.options.keyJump.isPressed())
    			mc.player.setVelocity(velocity.add(0, speed.getValue() / 2, 0));
    		
    		if(mc.options.keySneak.isPressed())
    			mc.player.setVelocity(velocity.subtract(0, speed.getValue() / 2, 0));
    	}
        super.onTick();
    }

    @Override
    public void onDisable() {
    	if (mode.is("Vanilla")) {
    		mc.player.getAbilities().flying = false;
    	} else {
    		
    	}
        super.onDisable();
    }
}
