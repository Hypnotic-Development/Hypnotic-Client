package dev.hypnotic.module.player;

import java.util.ArrayList;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventMotionUpdate;
import dev.hypnotic.event.events.EventPlayerJump;
import dev.hypnotic.event.events.EventRender3D;
import dev.hypnotic.event.events.EventWalkOffLedge;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.movement.Speed;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.RotationUtils;
import dev.hypnotic.utils.Timer;
import dev.hypnotic.utils.player.PlayerUtils;
import dev.hypnotic.utils.world.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class Scaffold extends Mod {
	
	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "NCP");
    public final NumberSetting extend = new NumberSetting("Extend", 0.7, 0, 8, 0.1);
    public BooleanSetting boost = new BooleanSetting("Boost", false);
    public final NumberSetting boostSpeed = new NumberSetting("Speed", 0, 0.25, 1, 0.01);
    private final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    public final BooleanSetting down = new BooleanSetting("Down", false);
    private final BooleanSetting tower = new BooleanSetting("Tower", true);
    
    private final BooleanSetting swing = new BooleanSetting("Swing", false);

    private final BooleanSetting keepY = new BooleanSetting("KeepY", false);
    private final BooleanSetting space = new BooleanSetting("Override KeepY", false);
    
    private int startY;
    private BlockPos pos;
    public Timer towerTimer = new Timer();
    
    public Scaffold() {
    	super("Scaffold", "Places blocks underneath you", Category.PLAYER);
    	addSettings(mode, extend, boost, boostSpeed, rotate, down, tower, swing, keepY, space);
    }
    
    @EventTarget
    public void onWalkOffLedge(EventWalkOffLedge event) {
        if(down.isEnabled() && mc.options.keySneak.isPressed()) event.isSneaking = false;
    }

    @EventTarget
    public void onPlayerJump(EventPlayerJump event) {
        if(tower.isEnabled() && getBlockInHotbar() != -1 && !PlayerUtils.isMoving() && !(this.keepY.isEnabled() && (this.space.isEnabled() ? !mc.options.keyJump.isPressed() : true))) {
            event.setCancelled(true);
        }
    }
    
    @Override
    public void onEnable() {
    	startY = (int) (mc.player.getY() - 1);
    	super.onEnable();
    }

    @Override
    public void onTick() {
    	if (boost.isEnabled() && !ModuleManager.INSTANCE.getModule(Speed.class).isEnabled()) PlayerUtils.setMotion(boostSpeed.getValue());
    }

    @Override
    public void onMotion() {
    	
    }
    
    public int getBlockInHotbar() {
        for(int i = 0; i < 9; i++) {
            if(
                    mc.player.getInventory().getStack(i) == ItemStack.EMPTY
                            || !(mc.player.getInventory().getStack(i).getItem() instanceof BlockItem)
                            || !Block.getBlockFromItem(mc.player.getInventory().getStack(i).getItem()).getDefaultState().isFullCube(mc.world, new BlockPos(0, 0, 0))
            ) continue;

            return i;
        }

        return -1;
    }
    
    @EventTarget
    public void render3d(EventRender3D event) {
    	
    }
    
    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event) {
    	if (event.isPre()) {
	    	this.setDisplayName("Scaffold " + ColorUtils.gray + mode.getSelected());
	    	int oldSlot = mc.player.getInventory().selectedSlot;
	        int newSlot = getBlockInHotbar();
	        if(newSlot != -1) {
	            mc.player.getInventory().selectedSlot = newSlot;
	        } else {
	        	return;
	        }
	        float yaw1 = mc.player.getYaw();
	        float forward1 = 1;
	        if(mc.player != null && (mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0)){
	        	if(mc.player.forwardSpeed < 0) {
	                yaw1 += 180;
	                forward1 = -0.5f;
	        	} else if(mc.player.forwardSpeed > 0) forward1 = 0.5f;
	        	if(mc.player.sidewaysSpeed > 0) yaw1 -= 90 * forward1;
	            if(mc.player.sidewaysSpeed < 0) yaw1 += 90 * forward1;
	            
	            yaw1 = (float) Math.toRadians(yaw1);
	        }
	        
	        boolean keepY = this.keepY.isEnabled() && (this.space.isEnabled() ? !mc.options.keyJump.isPressed() : true);
	        
	        if (mc.options.keyJump.isPressed() && this.space.isEnabled() && mc.options.keyJump.isPressed()) {
	        	startY = (int) (mc.player.getY() - 1);
	        }
	        
	        if (tower.isEnabled() && mc.options.keyJump.isPressed() && getBlockInHotbar() != -1 && !keepY) {
	        	if ((PlayerUtils.isOnGround(0.70) || mc.player.isOnGround()) && mc.world.getBlockState(mc.player.getBlockPos().down()).getBlock() != Blocks.AIR) {
	        		
	        		mc.player.setVelocity(0, 0.40985, 0);
	        	}
	        	
	        	if(towerTimer.hasTimeElapsed(500, true)) {
	                mc.player.setVelocity(mc.player.getVelocity().x, -1, mc.player.getVelocity().z);
	            }
	        } else {
	        	towerTimer.reset();
	        }
	        
	        if(mc.options.keySneak.isPressed() && down.isEnabled()) {
	
	            BlockPos under = new BlockPos(mc.player.getX(), !keepY ? mc.player.getY() - 2 : startY, mc.player.getZ());
	            pos = under;
	            
	            if(mc.world.getBlockState(under).getMaterial().isReplaceable()) WorldUtils.placeBlockMainHand(under, false, true, true, swing.isEnabled());
	
	            mc.player.getInventory().selectedSlot = oldSlot;
	
	            return;
	        }
	
	        if(extend.getValue() == 0) {
	            BlockPos under = new BlockPos(mc.player.getX(), !keepY ? mc.player.getY() - 1 : startY, mc.player.getZ());
	            pos = under;
	            if(mc.world.getBlockState(under).getMaterial().isReplaceable()) WorldUtils.placeBlockMainHand(under, false, true, true, swing.isEnabled());
	
	            mc.player.getInventory().selectedSlot = oldSlot;
	
	            return;
	        }
	
	        ArrayList<BlockPos> blocks = new ArrayList<>();
	        for(double i = (int) 0; i < extend.getValue(); i+=0.01) {
	        	BlockPos pos = WorldUtils.getForwardBlock((mc.player.input.movementForward < 0) ? (-i) : (i)).down();
	        	if (PlayerUtils.distanceTo(pos) > 10) startY = (int) (mc.player.getY() - 1);
	        	if (tower.isEnabled() ? (this.keepY.isEnabled() && !space.isEnabled() ? true : !mc.options.keyJump.isPressed()) : true) blocks.add(new BlockPos(pos.getX(), !keepY ? pos.getY() : startY, pos.getZ()));
	        	else blocks.add(new BlockPos(mc.player.getX(), !keepY ? mc.player.getBlockPos().getY() - 0.2 : startY, mc.player.getZ()));
	        }
	
	        for(BlockPos x : blocks) {
	            if(mc.world.getBlockState(x).getMaterial().isReplaceable()) {
	            	pos = x;
	            	if (PlayerUtils.distanceTo(x) > 10) startY = (int) (mc.player.getY() - 1);
	                WorldUtils.placeBlockMainHand(x, false, true, true, swing.isEnabled());
	                break;
	            }
	        }
	        if (rotate.isEnabled() && pos != null) {
	        	event.setYaw((float) RotationUtils.getYaw(pos));
	        	event.setPitch((float) RotationUtils.getPitch(pos));
			    RotationUtils.setSilentYaw(event.getYaw());
			    RotationUtils.setSilentPitch(event.getPitch());
	        }
	        mc.player.getInventory().selectedSlot = oldSlot;
    	}
    }
    
    @Override
    public void onTickDisabled() {
    	boostSpeed.setVisible(boost.isEnabled());
    	space.setVisible(keepY.isEnabled());
    	super.onTickDisabled();
    }
    
    @Override
    public void onDisable() {
    	RotationUtils.resetYaw();
    	RotationUtils.resetPitch();
    	super.onDisable();
    }
}
