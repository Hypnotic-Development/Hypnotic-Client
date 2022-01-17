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
package dev.hypnotic.ui.altmanager.account;

import static dev.hypnotic.utils.MCUtils.mc;

import com.mojang.authlib.yggdrasil.YggdrasilEnvironment;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;

import dev.hypnotic.mixin.MinecraftClientAccessor;
import dev.hypnotic.utils.ISerializable;
import dev.hypnotic.utils.NbtException;
import net.minecraft.client.util.Session;
import net.minecraft.nbt.NbtCompound;

// Credits to meteor client (https://github.com/MeteorDevelopment/meteor-client)
public abstract class Account<T extends Account<?>> implements ISerializable<T> {
    protected AccountType type;
    protected String name;

    protected final AccountCache cache;

    public Account(AccountType type, String name) {
        this.type = type;
        this.name = name;
        this.cache = new AccountCache();
    }

    public abstract boolean fetchInfo();

    public boolean login() {
        YggdrasilMinecraftSessionService service = (YggdrasilMinecraftSessionService) mc.getSessionService();
        AccountUtils.setBaseUrl(service, YggdrasilEnvironment.PROD.getEnvironment().getSessionHost() + "/session/minecraft/");
        AccountUtils.setJoinUrl(service, YggdrasilEnvironment.PROD.getEnvironment().getSessionHost() + "/session/minecraft/join");
        AccountUtils.setCheckUrl(service, YggdrasilEnvironment.PROD.getEnvironment().getSessionHost() + "/session/minecraft/hasJoined");

        return true;
    }

    public String getUsername() {
        if (cache.username.isEmpty()) return name;
        return cache.username;
    }

    public AccountType getType() {
        return type;
    }

    public AccountCache getCache() {
        return cache;
    }

    protected void setSession(Session session) {
        ((MinecraftClientAccessor) mc).setSession(session);
        mc.getSessionProperties().clear();
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        tag.putString("type", type.name());
        tag.putString("name", name);
        tag.put("cache", cache.toTag());

        return tag;
    }

    @SuppressWarnings("unchecked")
	@Override
    public T fromTag(NbtCompound tag) {
        if (!tag.contains("name") || !tag.contains("cache")) throw new NbtException();

        name = tag.getString("name");
        cache.fromTag(tag.getCompound("cache"));

        return (T) this;
    }
}
