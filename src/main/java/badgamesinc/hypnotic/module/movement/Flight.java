package badgamesinc.hypnotic.module.movement;

import org.lwjgl.glfw.GLFW;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.combat.Killaura;
import badgamesinc.hypnotic.module.combat.TargetStrafe;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.Vec3d;

public class Flight extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Velocity", "Velocity", "Vanilla");
	public NumberSetting speed = new NumberSetting("Speed", 1, 0, 10, 0.1);
	public BooleanSetting damage = new BooleanSetting("Damage", false);
	boolean hasDamaged = false;
	private int wallTicks = 0;
	private boolean direction = false;
	
    public Flight() {
        super("Flight", "Allows you to fly", Category.MOVEMENT);
        this.setKey(GLFW.GLFW_KEY_G);
        addSettings(mode, speed, damage);
    }
    
    @Override
    public void onEnable() {
    	if (damage.isEnabled()) {
    		mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 5.1 + 5, mc.player.getZ(), true));
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), true));
    	}
    	super.onEnable();
    }

    @Override
    public void onTick() {
    	this.setDisplayName("Flight " + ColorUtils.gray + mode.getSelected());
    	if (mc.player == null)
    		return;
    	if (!mc.player.isOnGround()) {
			wallTicks++;
			if (wallTicks > 7 && mc.player.horizontalCollision) {
				direction = !direction;
				wallTicks = 0;
			}
		} else wallTicks = 0;
    	if (damage.isEnabled() && !hasDamaged) {
    		mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 5.1 + 5, mc.player.getZ(), true));
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), true));
            hasDamaged = true;
    	}
    	if (mode.is("Vanilla")) {
    		mc.player.getAbilities().flying = true;
    	} else {
    		mc.player.getAbilities().flying = false;
    		mc.player.flyingSpeed = (float) speed.getValue();
    		
//    		if (!TargetStrafe.canStrafe())
        	mc.player.setVelocity(0, 0, 0);
        		
    		Vec3d velocity = mc.player.getVelocity();
    		
    		if (TargetStrafe.canStrafe()) {
    			mc.player.getAbilities().flying = true;
    			TargetStrafe.strafe(speed.getValue(), Killaura.target, direction, true);
            }
    		if(mc.options.keyJump.isPressed() && !mc.options.keySneak.isPressed()) {
    			mc.player.setVelocity(velocity.add(0, speed.getValue(), 0));
    		}
    		
    		if(mc.options.keySneak.isPressed() && !mc.options.keyJump.isPressed()) {
    			mc.player.setVelocity(velocity.subtract(0, speed.getValue(), 0));
    		}
    		
    	}
        super.onTick();
    }

    @Override
    public void onDisable() {
    	mc.player.getAbilities().flying = false;
    	hasDamaged = false;
    	//skygun
        super.onDisable();
    }
}
