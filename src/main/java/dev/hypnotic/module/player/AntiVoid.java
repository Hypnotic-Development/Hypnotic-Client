/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.module.player;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.movement.Flight;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Timer;

public class AntiVoid extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "BadAC");
	public NumberSetting fallDistance = new NumberSetting("Fall Distance", 10, 1, 20, 0.1);
	private double lastX, lastY, lastZ;
	Timer timer = new Timer();
	
    public AntiVoid() {
        super("AntiVoid", "Prevents you from falling into the void", Category.PLAYER);
        addSettings(mode, fallDistance);
    }

    int tries = 0;
    boolean shouldBeOnGround = true;
    @Override
    public void onTick() {
    	this.setDisplayName("AntiVoid " + ColorUtils.gray + mode.getSelected());
    	if (mc.player.isOnGround() && timer.hasTimeElapsed(100, true)) {
    		this.lastX = mc.player.lastRenderX;
    		this.lastY = mc.player.lastRenderY;
    		this.lastZ = mc.player.lastRenderZ;
    		this.shouldBeOnGround = false;
    		tries = 0;
    		mc.player.getAbilities().flying = false;
    	}
    	if (mc.world.getBlockState(mc.player.getBlockPos().down()) != null) {
    		mc.player.getAbilities().flying = false;
    	}
    	if (mc.player.fallDistance > fallDistance.getValue() && timer.hasTimeElapsed(100, true) && !mc.player.isOnGround() && !ModuleManager.INSTANCE.getModule(Flight.class).isEnabled()) {
    		if(mode.is("Vanilla") && mc.player != null) {
    			mc.player.setPosition(lastX, lastY, lastZ);
    			mc.player.updatePosition(lastX, lastY, lastZ);
    			mc.player.fallDistance = 0;
    			this.shouldBeOnGround = true;
    			if (this.shouldBeOnGround) {
    				switch(tries) {
	    				case 0:
		    				mc.player.setPosition(lastX - 0.5, lastY, lastZ);
		    				tries++;
		    				break;
	    				case 1:
	    					mc.player.setPosition(lastX, lastY, lastZ - 0.5);
		    				tries++;
		    				break;
	    				case 2:
	    					mc.player.setPosition(lastX - 0.5, lastY, lastZ - 0.5);
		    				tries++;
		    				break;
	    				case 3:
	    					mc.player.setPosition(lastX + 0.5, lastY, lastZ);
		    				tries++;
		    				break;
	    				case 4:
	    					mc.player.setPosition(lastX, lastY, lastZ + 0.5);
		    				tries++;
		    				break;
	    				case 5:
	    					mc.player.setPosition(lastX + 0.5, lastY, lastZ + 0.5);
		    				tries++;
		    				break;
	    				case 6:
	    					mc.player.setPosition(lastX, lastY + 1, lastZ);
	    					tries++;
		    				break;
	    				case 7:
		    				tries=0;
		    				break;
    				}
    				
    			}
//    			mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
    		} else if (mode.is("BadAC")) {
    			mc.player.getAbilities().flying = true;
    		}
    	}
        super.onTick();
    }
}
