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
package dev.hypnotic.utils.player;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.Identifier;

public class FakePlayerEntity extends AbstractClientPlayerEntity {

    public FakePlayerEntity(ClientWorld world, GameProfile profile) {
        super(world, profile, null);
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public Identifier getSkinTexture() {
        if (hasSkinTexture())
            return super.getSkinTexture();
        else
            return MCAPIHelper.INSTANCE.getPlayerSkin(MinecraftClient.getInstance().getSession().getProfile() == null ? this.uuid : MinecraftClient.getInstance().getSession().getProfile().getId());
    }
}
