package badgamesinc.hypnotic.module.player;

import java.util.ArrayList;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventPlayerJump;
import badgamesinc.hypnotic.event.events.EventWalkOffLedge;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.RotationUtils;
import badgamesinc.hypnotic.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class Scaffold extends Mod {
	
    public final NumberSetting extend = new NumberSetting("Extend", 0, 0, 8, 0.1);
    private final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    private final BooleanSetting down = new BooleanSetting("Down", false);
    private final BooleanSetting tower = new BooleanSetting("Tower", true);

    public Scaffold() {
    	super("Scaffold", "Places blocks underneath you", Category.PLAYER);
    	addSettings(extend, rotate, down, tower);
    }
    
    @EventTarget
    public void onWalkOffLedge(EventWalkOffLedge event) {
        if(!down.isEnabled() && !mc.player.isSprinting()) event.isSneaking = true;
        if(down.isEnabled() && mc.options.keySneak.isPressed()) event.isSneaking = false;
    }

    @EventTarget
    public void onPlayerJump(EventPlayerJump event) {
        if(tower.isEnabled() && extend.getValue() <= 0) {
            event.setCancelled(true);
        }
    }

    @Override
    public void onTick() {
    	if (rotate.isEnabled()) {
    		if (extend.getValue() < 1) {
		    	RotationUtils.setSilentYaw(mc.player.getYaw() - 180);
		        RotationUtils.setSilentPitch(50);
    		}
    	}
        if(tower.isEnabled() && mc.options.keyJump.isPressed() && extend.getValue() <= 0) {
        	mc.player.addVelocity(0, 0.5, 0);
        }
    }

    @Override
    public void onMotion() {
    	
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
        if(mc.options.keySneak.isPressed() && down.isEnabled()) {

            BlockPos under = new BlockPos(mc.player.getX(), mc.player.getY() - 2, mc.player.getZ());

            if(mc.player != null && (mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0) && !mc.player.isTouchingWater()) {
                float yaw = mc.player.getYaw();
                float forward = 1;

                if(mc.player.forwardSpeed < 0) {
                    yaw += 180;
                    forward = -0.5f;
                } else if(mc.player.forwardSpeed > 0) forward = 0.5f;

                if(mc.player.sidewaysSpeed > 0) yaw -= 90 * forward;
                if(mc.player.sidewaysSpeed < 0) yaw += 90 * forward;

                yaw = (float) Math.toRadians(yaw);

                mc.player.setVelocity(-Math.sin(yaw) * 0.2, mc.player.getVelocity().y, Math.cos(yaw) * 0.2);
                
                
            }
            
            if(mc.world.getBlockState(under).getMaterial().isReplaceable()) WorldUtils.placeBlockMainHand(under, rotate.isEnabled());

            mc.player.getInventory().selectedSlot = oldSlot;

            return;
        }

        if(extend.getValue() == 0) {
            BlockPos under = new BlockPos(mc.player.getX(), mc.player.getY() - 1, mc.player.getZ());

            if(mc.world.getBlockState(under).getMaterial().isReplaceable()) WorldUtils.placeBlockMainHand(under, rotate.isEnabled());

            mc.player.getInventory().selectedSlot = oldSlot;

            return;
        }

        ArrayList<BlockPos> blocks = new ArrayList<>();
        for(double i = (int) 0; i < extend.getValue(); i+=0.01) {
        	blocks.add(WorldUtils.getForwardBlock((mc.player.input.movementForward < 0) ? (-i) : (i)).down());
        }

        for(BlockPos x : blocks) {
            if(mc.world.getBlockState(x).getMaterial().isReplaceable()) {
                WorldUtils.placeBlockMainHand(x, rotate.isEnabled());
                break;
            }
        }

        mc.player.getInventory().selectedSlot = oldSlot;
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
    
    @Override
    public void onDisable() {
    	RotationUtils.resetPitch();
    	super.onDisable();
    }
}
