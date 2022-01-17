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
package dev.hypnotic.ui.altmanager.account.types;

import static dev.hypnotic.utils.MCUtils.mc;

import java.util.Optional;

import com.google.gson.Gson;
import com.mojang.authlib.Agent;
import com.mojang.authlib.Environment;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import dev.hypnotic.mixin.MinecraftClientAccessor;
import dev.hypnotic.ui.altmanager.account.Account;
import dev.hypnotic.ui.altmanager.account.AccountType;
import dev.hypnotic.ui.altmanager.account.AccountUtils;
import net.minecraft.client.util.Session;

// Credits to meteor client (https://github.com/MeteorDevelopment/meteor-client)
public class TheAlteningAccount extends Account<TheAlteningAccount> {
    private static final String AUTH = "http://authserver.thealtening.com";
    private static final String ACCOUNT = "https://api.mojang.com";
    private static final String SESSION = "http://sessionserver.thealtening.com";
    private static final String SERVICES = "https://api.minecraftservices.com";

    @SuppressWarnings("unused")
	private static final Gson GSON = new Gson();

    public TheAlteningAccount(String token) {
        super(AccountType.TheAltening, token);
    }

    @Override
    public boolean fetchInfo() {
        YggdrasilUserAuthentication auth = getAuth();

        try {
            auth.logIn();

            cache.username = auth.getSelectedProfile().getName();
            cache.uuid = auth.getSelectedProfile().getId().toString();

            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    @Override
    public boolean login() {
        YggdrasilMinecraftSessionService service = (YggdrasilMinecraftSessionService) mc.getSessionService();
        AccountUtils.setBaseUrl(service, SESSION + "/session/minecraft/");
        AccountUtils.setJoinUrl(service, SESSION + "/session/minecraft/join");
        AccountUtils.setCheckUrl(service, SESSION + "/session/minecraft/hasJoined");

        YggdrasilUserAuthentication auth = getAuth();

        try {
            auth.logIn();
            setSession(new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), Optional.empty(), Optional.empty(), Session.AccountType.MOJANG));

            cache.username = auth.getSelectedProfile().getName();
            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    private YggdrasilUserAuthentication getAuth() {
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(((MinecraftClientAccessor) mc).getProxy(), "", Environment.create(AUTH, ACCOUNT, SESSION, SERVICES, "The Altening")).createUserAuthentication(Agent.MINECRAFT);

        auth.setUsername(name);
        auth.setPassword("password");

        return auth;
    }
}
