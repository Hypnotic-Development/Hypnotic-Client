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
package dev.hypnotic.module.misc;

import java.util.ArrayList;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.utils.player.FakePlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class FakePlayer extends Mod {

	public ArrayList<FakePlayerEntity> fakePlayers = new ArrayList<>();
	private PlayerEntity playerEntity;
	
	public FakePlayer() {
		super("FakePlayer", "Spawns a fake player to test combat modules on", Category.MISC);
	}

	@Override
	public void onEnable() {
		if (mc.player != null) {
			playerEntity = new FakePlayerEntity(mc.world, new GameProfile(UUID.randomUUID(), mc.player.getName().asString()));
    		playerEntity.copyFrom(mc.player);
    		playerEntity.copyPositionAndRotation(mc.player);
			mc.world.addEntity(1000000, playerEntity);
			fakePlayers.add((FakePlayerEntity) playerEntity);
		}
		super.onEnable();
	}
	
	@Override
	public void onDisable() {
		if (playerEntity != null) {
    		playerEntity.setPos(0, Double.NEGATIVE_INFINITY, 0);
    		if (mc.world != null) mc.world.removeEntity(1000000, Entity.RemovalReason.DISCARDED);
    		playerEntity = null;
    	}
		if (fakePlayers.contains((FakePlayerEntity) playerEntity)) fakePlayers.remove((FakePlayerEntity) playerEntity);
		super.onDisable();
	}
}
