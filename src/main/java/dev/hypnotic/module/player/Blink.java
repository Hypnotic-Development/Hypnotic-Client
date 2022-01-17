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

import java.util.ArrayList;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventSendPacket;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.player.FakePlayerEntity;
import dev.hypnotic.utils.player.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.c2s.play.KeepAliveC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Blink extends Mod {

	public BooleanSetting buffer = new BooleanSetting("Buffer Packets", true);
	public NumberSetting amount = new NumberSetting("Send Amount PT", 25, 5, 50, 1);
	
	private ArrayList<Packet<?>> packets = new ArrayList<>();
	
	private double startX, startY, startZ;
	
	public static PlayerEntity playerEntity;
	private boolean stopCatching;
	
    public Blink() {
        super("Blink", "Spoofs extreme lag so you visiblly teleport", Category.PLAYER);
        addSettings(buffer, amount);
    }
    
    @Override
    public void onEnable() {
    	stopCatching = false;
    	if (mc.player != null) {
    		playerEntity = new FakePlayerEntity(mc.world, new GameProfile(UUID.randomUUID(), mc.player.getName().asString()));
    		playerEntity.copyFrom(mc.player);
    		playerEntity.copyPositionAndRotation(mc.player);
			mc.world.addEntity(1000000, playerEntity);
		}
    	startX = mc.player.getX();
    	startY = mc.player.getY();
    	startZ = mc.player.getZ();
    	super.onEnable();
    }

    @Override
    public void onTick() {
    	if (stopCatching && !packets.isEmpty()) {
			for (int i = 0; i < amount.getValue(); i++) {
				mc.getNetworkHandler().sendPacket(packets.get(i));
			}
		}
        super.onTick();
    }
    
    @EventTarget
    private void onSendPacket(EventSendPacket event) {
    	if (mc.player == null || (packets.isEmpty() && stopCatching)) {
			packets.clear();
			this.setEnabled(false);
			return;
		}
		if (!stopCatching && !(event.getPacket() instanceof KeepAliveC2SPacket)) {
			if (event.getPacket() instanceof PlayerMoveC2SPacket) {
				packets.add(event.getPacket());
			}
			event.setCancelled(true);;
		}
    }
    
    @Override
    public void onDisable() {
    	stopCatching = true;
		if (!buffer.isEnabled() || packets.isEmpty())
			super.onDisable();
		if (!buffer.isEnabled())
			packets.forEach(packet -> {
				mc.getNetworkHandler().sendPacket(packet);
			});
		packets.clear();
    	if (playerEntity != null) {
    		playerEntity.setPos(0, Double.NEGATIVE_INFINITY, 0);
    		if (mc.world != null) mc.world.removeEntity(1000000, Entity.RemovalReason.DISCARDED);
    		playerEntity = null;
    	}
    	PlayerUtils.blinkToPos(new Vec3d(startX, startY, startZ), new BlockPos(mc.player.getX(), mc.player.getY(), mc.player.getZ()), 0, new double[] {1, 2});
    	super.onDisable();
    }
}

