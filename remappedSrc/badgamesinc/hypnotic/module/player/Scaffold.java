package badgamesinc.hypnotic.module.player;

import java.util.ArrayList;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventPlayerJump;
import badgamesinc.hypnotic.event.events.EventSwingHand;
import badgamesinc.hypnotic.event.events.EventWalkOffLedge;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.RotationUtils;
import badgamesinc.hypnotic.utils.player.PlayerUtils;
import badgamesinc.hypnotic.utils.world.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class Scaffold extends Mod {
	
    public final NumberSetting extend = new NumberSetting("Extend", 0, 0, 8, 0.1);
    private final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    public final BooleanSetting down = new BooleanSetting("Down", false);
    private final BooleanSetting tower = new BooleanSetting("Tower", true);
    private final BooleanSetting swing = new BooleanSetting("Swing", false);
    
    public Scaffold() {
    	super("Scaffold", "Places blocks underneath you", Category.PLAYER);
    	addSettings(extend, rotate, down, tower, swing);
    }
    
    @EventTarget
    public void onWalkOffLedge(EventWalkOffLedge event) {
        if(down.isEnabled() && mc.options.keySneak.isPressed()) event.isSneaking = false;
    }

    @EventTarget
    public void onPlayerJump(EventPlayerJump event) {
        if(tower.isEnabled() && getBlockInHotbar() != -1 && !PlayerUtils.isMoving()) {
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
        if(tower.isEnabled() && mc.options.keyJump.isPressed() && getBlockInHotbar() != -1) {
        	mc.player.setVelocity(mc.player.getVelocity().x, 0.3805, mc.player.getVelocity().z);
        }
    }

    @Override
    public void onMotion() {
    	this.setDisplayName("Scaffold " + ColorUtils.gray + extend.getValue());
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

            
            if(mc.world.getBlockState(under).getMaterial().isReplaceable()) WorldUtils.placeBlockMainHand(under, rotate.isEnabled(), swing.isEnabled());

            mc.player.getInventory().selectedSlot = oldSlot;

            return;
        }

        if(extend.getValue() == 0) {
            BlockPos under = new BlockPos(mc.player.getX(), mc.player.getY() - 1, mc.player.getZ());

            if(mc.world.getBlockState(under).getMaterial().isReplaceable()) WorldUtils.placeBlockMainHand(under, rotate.isEnabled(), swing.isEnabled());

            mc.player.getInventory().selectedSlot = oldSlot;

            return;
        }

        ArrayList<BlockPos> blocks = new ArrayList<>();
        for(double i = (int) 0; i < extend.getValue(); i+=0.01) {
        	if (!mc.options.keyJump.isPressed()) blocks.add(WorldUtils.getForwardBlock((mc.player.input.movementForward < 0) ? (-i) : (i)).down());
        	else blocks.add(mc.player.getBlockPos().down());
        }

        for(BlockPos x : blocks) {
            if(mc.world.getBlockState(x).getMaterial().isReplaceable()) {
            	if (PlayerUtils.isMoving())
                WorldUtils.placeBlockMainHand(x, rotate.isEnabled(), swing.isEnabled());
            	else WorldUtils.placeBlockMainHand(mc.player.getBlockPos().down(), rotate.isEnabled(), swing.isEnabled());
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
    
    @EventTarget
    public void onSwingHand(EventSwingHand event) {
//    	if (!swing.isEnabled()) event.setCancelled(true);
    }
    
    @Override
    public void onDisable() {
    	RotationUtils.resetPitch();
    	super.onDisable();
    }
}