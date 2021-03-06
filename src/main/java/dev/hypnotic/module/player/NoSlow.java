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

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventMotionUpdate;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.combat.Killaura;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.player.PlayerUtils;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

public class NoSlow extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "NCP");
	boolean blocking = false;
	
	public NoSlow() {
        super("NoSlow", "Prevents items from slowing you down", Category.PLAYER);
        addSettings(mode);
    }

    @Override
    public void onTick() {
    	
    	super.onTick();
    }
    
    @EventTarget
    public void onMotionUpdate(EventMotionUpdate event) {
    	if (mc.player.isOnGround()) {
	    	if (event.isPre()) {
	        	if (mc.player.isBlocking() && mode.is("NCP") && Killaura.target == null) {
	        		mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, Direction.DOWN));
	        		blocking = false;
	        	}
	        } else {
	            if (mc.player.isBlocking() && mode.is("NCP") && Killaura.target == null && !blocking) {
	                PlayerUtils.sendSequencedPacket(id -> new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, new BlockHitResult(new Vec3d(0.0f, 0.0f, 0.0f), Direction.DOWN, new BlockPos(-1, -1, -1), false), id));
	                mc.interactionManager.interactItem(mc.player, Hand.OFF_HAND);
					mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
					blocking = true;
	            }
	        }
    	}
    	this.setDisplayName("NoSlow " + ColorUtils.gray + mode.getSelected());
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
