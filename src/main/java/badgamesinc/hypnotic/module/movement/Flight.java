package badgamesinc.hypnotic.module.movement;

import org.lwjgl.glfw.GLFW;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventReceivePacket;
import badgamesinc.hypnotic.event.events.EventSendPacket;
import badgamesinc.hypnotic.mixin.PlayerMoveC2SPacketAccessor;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.combat.Killaura;
import badgamesinc.hypnotic.module.combat.TargetStrafe;
import badgamesinc.hypnotic.module.exploit.AntiHunger;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.Timer;
import badgamesinc.hypnotic.utils.player.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Flight extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Velocity", "Velocity", "Vanilla", "Verus");
	public NumberSetting speed = new NumberSetting("Speed", 1, 0, 10, 0.1);
	public BooleanSetting damage = new BooleanSetting("Damage", false);
	public BooleanSetting blink = new BooleanSetting("Blink", false);
	boolean hasDamaged = false;
	private int wallTicks = 0;
	private boolean direction = false;
	private static Timer blinkTimer = new Timer();
	
    public Flight() {
        super("Flight", "Allows you to fly", Category.MOVEMENT);
        this.setKey(GLFW.GLFW_KEY_G);
        addSettings(mode, speed, damage, blink);
    }
    
    @Override
    public void onEnable() {
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
    		boolean antiHunger = ModuleManager.INSTANCE.getModule(AntiHunger.class).isEnabled();
    		if (antiHunger) ModuleManager.INSTANCE.getModule(AntiHunger.class).toggleSilent();
    		mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 3.1, mc.player.getZ(), true));
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), false));
            mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY(), mc.player.getZ(), true));
            hasDamaged = true;
            if (antiHunger) ModuleManager.INSTANCE.getModule(AntiHunger.class).toggleSilent();
    	}
    	if (mode.is("Vanilla")) {
    		mc.player.getAbilities().flying = true;
    	} else {
    		mc.player.getAbilities().flying = false;
    		mc.player.flyingSpeed = (float) speed.getValue();
    		
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
    		if (mode.is("Verus")) {
    			mc.player.setVelocity(mc.player.getVelocity().x, 0, mc.player.getVelocity().z);
    			mc.player.setOnGround(true);
    			if (mc.world.getBlockState(new BlockPos(mc.player.getX(), mc.player.getY() - 1.0, mc.player.getZ())).getBlock() == Blocks.AIR)
	                mc.world.setBlockState(new BlockPos(mc.player.getX(), mc.player.getY() - 1.0, mc.player.getZ()), Blocks.BARRIER.getDefaultState(), Block.NOTIFY_LISTENERS);
    			PlayerUtils.setMotion(speed.getValue());
    		}
    	}
        super.onTick();
    }
    
    @EventTarget
    public void eventReceivePacket(EventReceivePacket event) {
    	if (event.getPacket() instanceof PlayerMoveC2SPacket && mode.is("Verus") && (damage.isEnabled() ? hasDamaged == true : true)) {
    		((PlayerMoveC2SPacketAccessor) event.getPacket()).setOnGround(true);
    	}
    }
    
    @EventTarget 
    public void eventSendPacket(EventSendPacket event) {
    	if (blink.isEnabled()) {
    		if (blinkTimer.hasTimeElapsed(50, true) && (damage.isEnabled() ? hasDamaged : true)) {
    			ModuleManager.INSTANCE.getModule(FlightBlink.class).toggle();
    		}
    	}
    }

    @Override
    public void onDisable() {
    	mc.player.getAbilities().flying = false;
    	hasDamaged = false;
    	if (ModuleManager.INSTANCE.getModule(FlightBlink.class).isEnabled()) ModuleManager.INSTANCE.getModule(FlightBlink.class).toggle();
        super.onDisable();
    }
}
