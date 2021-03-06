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

import org.lwjgl.glfw.GLFW;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventSendPacket;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.render.ClickGUIModule;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.utils.ColorUtils;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class Criticals extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Packet", "Packet");
	
	public Criticals() {
		super("Criticals", "Attacks select surrounding entities", Category.COMBAT);
		addSettings(mode);
	}
	
	@EventTarget
    public void onSendPacket(EventSendPacket event) {
		ModuleManager.INSTANCE.getModule(ClickGUIModule.class).setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
        String var10001 = ColorUtils.gray;
        this.setDisplayName("Criticals " + var10001 + this.mode.getSelected());
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket && mc.player.isOnGround() && !mc.player.isInLava() && !mc.player.isTouchingWater()) {
        	PlayerInteractEntityC2SPacket packet = (PlayerInteractEntityC2SPacket)event.getPacket();
            if (getInteractType(packet) == Criticals.InteractType.ATTACK && getEntity(packet) instanceof LivingEntity) {
                this.sendPacket(0.0625D);
                this.sendPacket(0.0D);
            }
        }

    }

    private void sendPacket(double height) {
        double x = mc.player.getX();
        double y = mc.player.getY();
        double z = mc.player.getZ();
        PlayerMoveC2SPacket.PositionAndOnGround packet = new PlayerMoveC2SPacket.PositionAndOnGround(x, y + height, z, false);
        mc.getNetworkHandler().sendPacket(packet);
    }

    public static Entity getEntity(PlayerInteractEntityC2SPacket packet) {
        PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
        packet.write(packetBuf);
        return mc.world.getEntityById(packetBuf.readVarInt());
    }

    public static InteractType getInteractType(PlayerInteractEntityC2SPacket packet) {
    	PacketByteBuf packetBuf = new PacketByteBuf(Unpooled.buffer());
        packet.write(packetBuf);
        packetBuf.readVarInt();
        return (InteractType)packetBuf.readEnumConstant(InteractType.class);
    }
	
	public enum InteractType {
	    INTERACT,
	    ATTACK,
	    INTERACT_AT;
	}
}
