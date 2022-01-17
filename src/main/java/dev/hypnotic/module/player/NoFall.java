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
import dev.hypnotic.event.events.EventSendPacket;
import dev.hypnotic.mixin.PlayerMoveC2SPacketAccessor;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.utils.ColorUtils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;

public class NoFall extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Packet", "Packet");
	
    public NoFall() {
        super("NoFall", "Prevents you from taking fall damage", Category.PLAYER);
        addSetting(mode);
    }

    @Override
    public void onTick() {
    	this.setDisplayName("NoFall " + ColorUtils.gray + mode.getSelected());
        super.onTick();
    }
    
    @EventTarget
    private void onSendPacket(EventSendPacket event) {
    	if (mc.player == null) return;
        if (mc.player.getAbilities().creativeMode
            || !(event.getPacket() instanceof PlayerMoveC2SPacket) || mc.player.isOnGround() || mc.player.fallDistance < 2.5) return;


        if ((mc.player.isFallFlying()) && mc.player.getVelocity().y < 1) {
            BlockHitResult result = mc.world.raycast(new RaycastContext(
                mc.player.getPos(),
                mc.player.getPos().subtract(0, 0.5, 0),
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                mc.player)
            );

            if (result != null && result.getType() == HitResult.Type.BLOCK) {
                ((PlayerMoveC2SPacketAccessor) event.getPacket()).setOnGround(true);
            }
        }
        else {
            ((PlayerMoveC2SPacketAccessor) event.getPacket()).setOnGround(true);
        }
    }
}
