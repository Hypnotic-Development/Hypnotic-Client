package badgamesinc.hypnotic.module.movement;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.combat.Killaura;
import badgamesinc.hypnotic.module.combat.TargetStrafe;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.player.PlayerUtils;

public class Speed extends Mod {

	public NumberSetting speed = new NumberSetting("Speed", 1, 1, 10, 0.1);
	public BooleanSetting jump = new BooleanSetting("Jump", true);
	public NumberSetting jumpHeight = new NumberSetting("Jump Height", 4, 1, 10, 0.1);
	public BooleanSetting override = new BooleanSetting("Override Jump Height", true);
	
	private int wallTicks = 0;
	private boolean direction = false;
	
	public Speed() {
		super("Speed", "Makes you go fast", Category.MOVEMENT);
		addSettings(speed, jump, jumpHeight, override);
	}
	
	@Override
	public void onTick() {
		if(mc.player.isOnGround() && PlayerUtils.isMoving() && jump.isEnabled()) mc.player.setVelocity(mc.player.getVelocity().x, override.isEnabled() && mc.options.keyJump.isPressed() ? 0.45 : jumpHeight.getValue() * 0.1, mc.player.getVelocity().z);
		super.onTick();
	}

	@Override
	public void onMotion() {
		this.setDisplayName("Speed " + ColorUtils.gray + (jump.isEnabled() ? "Hop" : "Ground"));
		if(mc.player != null && (mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0) && !mc.player.isTouchingWater()) {
			if (!mc.player.isOnGround()) {
				wallTicks++;
				if (wallTicks > 7 && mc.player.horizontalCollision) {
					direction = !direction;
					wallTicks = 0;
				}
			} else wallTicks = 0;
            double speed = this.speed.getValue() * 0.1 + (mc.player.isOnGround() ? this.speed.getValue() * 0.01 : 0);
            PlayerUtils.setMotion(speed);
            if (TargetStrafe.canStrafe()) {
            	TargetStrafe.strafe(speed * 1.05, Killaura.target, direction, false);
            }
        }
		super.onMotion();
	}
	
	@Override
	public void onTickDisabled() {
		jumpHeight.setVisible(jump.isEnabled());
		super.onTickDisabled();
	}
	
//	@Override
//	public void onDisable() {
//		mc.options.
//		super.onDisable();
//	}
}
