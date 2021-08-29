package badgamesinc.hypnotic.module.movement;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.combat.Killaura;
import badgamesinc.hypnotic.module.combat.TargetStrafe;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.player.PlayerUtils;

public class Speed extends Mod {

	public NumberSetting speed = new NumberSetting("Speed", 1, 1, 10, 0.1);
	public NumberSetting jumpHeight = new NumberSetting("Jump Height", 1, 1, 10, 0.1);
	
	private int wallTicks = 0;
	private boolean direction = false;
	
	public Speed() {
		super("Speed", "Makes you go fast", Category.MOVEMENT);
		addSettings(speed, jumpHeight);
	}
	
	@Override
	public void onTick() {
		if(mc.player.isOnGround() && PlayerUtils.isMoving()) mc.player.setVelocity(mc.player.getVelocity().x, 0.4, mc.player.getVelocity().z);
		super.onTick();
	}

	@Override
	public void onMotion() {
		if(mc.player != null && (mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0) && !mc.player.isTouchingWater()) {
//			if(mc.player.isOnGround()) mc.player.jump();

			if (!mc.player.isOnGround()) {
				wallTicks++;
				if (wallTicks > 7 && mc.player.horizontalCollision) {
					direction = !direction;
					wallTicks = 0;
				}
			} else wallTicks = 0;
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
            if (TargetStrafe.canStrafe()) {
            	TargetStrafe.strafe(speed, Killaura.target, direction, false);
            }
        }
		super.onMotion();
	}
	
//	@Override
//	public void onDisable() {
//		mc.options.
//		super.onDisable();
//	}
}
