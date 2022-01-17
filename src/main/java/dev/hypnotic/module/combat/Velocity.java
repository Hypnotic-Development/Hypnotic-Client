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
package dev.hypnotic.module.combat;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventReceivePacket;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.ReflectionHelper;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;

public class Velocity extends Mod {

	public NumberSetting horizontal = new NumberSetting("Horizontal", 0, -100, 200, 1);
	public NumberSetting vertical = new NumberSetting("Vertical", 0, 0, 200, 1);
	
	public Velocity() {
		super("Velocity", "Attacks select surrounding entities", Category.COMBAT);
		addSettings(horizontal, vertical);
	}
	
	@EventTarget
	public void onReceivePacket(EventReceivePacket event) {
		if(mc.world == null || mc.player == null) return;
		this.setDisplayName("Velocity " + ColorUtils.gray + "H:" + horizontal.getValue() + " V:" + vertical.getValue());
        if(event.getPacket() instanceof EntityVelocityUpdateS2CPacket) {
            EntityVelocityUpdateS2CPacket velocity = (EntityVelocityUpdateS2CPacket) event.getPacket();
            if(velocity.getId() == mc.player.getId() && !mc.player.isTouchingWater()) {
                ReflectionHelper.setPrivateValue(EntityVelocityUpdateS2CPacket.class, velocity, (int) (velocity.getVelocityX() * (horizontal.getValue() * 0.01)), "velocityX", "field_12561");
                ReflectionHelper.setPrivateValue(EntityVelocityUpdateS2CPacket.class, velocity, (int) (velocity.getVelocityY() * (vertical.getValue() * 0.01)), "velocityY", "field_12562");
                ReflectionHelper.setPrivateValue(EntityVelocityUpdateS2CPacket.class, velocity, (int) (velocity.getVelocityZ() * (horizontal.getValue() * 0.01)), "velocityZ", "field_12563");
            }
        } else if(event.getPacket() instanceof ExplosionS2CPacket) {
            ExplosionS2CPacket velocity = (ExplosionS2CPacket) event.getPacket();
            ReflectionHelper.setPrivateValue(ExplosionS2CPacket.class, velocity, (int) (velocity.getPlayerVelocityX() * (horizontal.getValue() * 0.01)), "playerVelocityX", "field_12176");
            ReflectionHelper.setPrivateValue(ExplosionS2CPacket.class, velocity, (int) (velocity.getPlayerVelocityY() * (vertical.getValue() * 0.01)), "playerVelocityY", "field_12183");
            ReflectionHelper.setPrivateValue(ExplosionS2CPacket.class, velocity, (int) (velocity.getPlayerVelocityZ() * (horizontal.getValue() * 0.01)), "playerVelocityZ", "field_12182");
        }
	}
}
