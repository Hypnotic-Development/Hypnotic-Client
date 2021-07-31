package badgamesinc.hypnotic.module.movement;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;

public class Speed extends Mod {

	public NumberSetting speed = new NumberSetting("Speed", 1, 1, 10, 0.1);
	public NumberSetting jumpHeight = new NumberSetting("Jump Height", 1, 1, 10, 0.1);
	
	public Speed() {
		super("Speed", "Makes you go fast", Category.MOVEMENT);
		addSettings(speed, jumpHeight);
	}
	
	@Override
	public void onTick() {
//		if (mc.player.input != null && (mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0))
//			if (jump.isEnabled() && !mc.player.isTouchingWater() && mc.player.isOnGround() && !ModuleManager.INSTANCE.getModuleByName("Flight").isEnabled()) mc.options.keyJump.setPressed(true);;
		super.onTick();
	}

	@Override
	public void onMotion() {
		if(mc.player != null && (mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0) && !mc.player.isTouchingWater()) {
			if(mc.player.isOnGround()) mc.player.addVelocity(0, jumpHeight.getValue() * 0.02, 0);

            double speed = this.speed.getValue() * 0.1;

            float yaw = mc.player.getYaw();
            float forward = 1;

            if(mc.player.forwardSpeed < 0) {
                yaw += 180;
                forward = -0.5f;
            } else if(mc.player.forwardSpeed > 0) forward = 0.5f;

            if(mc.player.sidewaysSpeed > 0) yaw -= 90 * forward;
            if(mc.player.sidewaysSpeed < 0) yaw += 90 * forward;

            yaw = (float) Math.toRadians(yaw);

            mc.player.setVelocity(-Math.sin(yaw) * speed, mc.player.getVelocity().y, Math.cos(yaw) * speed);
            
            
        }
		super.onMotion();
	}
	
//	@Override
//	public void onDisable() {
//		mc.options.
//		super.onDisable();
//	}
}
