/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client/).
 * Copyright (c) 2021 Meteor Development.
 */

package dev.hypnotic.hypnotic_client.ui.altmanager.account.types;

import dev.hypnotic.hypnotic_client.ui.altmanager.account.Account;
import dev.hypnotic.hypnotic_client.ui.altmanager.account.AccountType;
import dev.hypnotic.hypnotic_client.ui.altmanager.account.MicrosoftLogin;
import net.minecraft.client.util.Session;

public class MicrosoftAccount extends Account<MicrosoftAccount> {
    public MicrosoftAccount(String refreshToken) {
        super(AccountType.Microsoft, refreshToken);
    }

    @Override
    public boolean fetchInfo() {
        return auth() != null;
    }

    @Override
    public boolean login() {
        super.login();

        String token = auth();
        if (token == null) return false;

        setSession(new Session(cache.username, cache.uuid, token, null, null, Session.AccountType.MOJANG));
        return true;
    }

    private String auth() {
        MicrosoftLogin.LoginData data = MicrosoftLogin.login(name);
        if (!data.isGood()) return null;

        name = data.newRefreshToken;
        cache.username = data.username;
        cache.uuid = data.uuid;

        return data.mcToken;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MicrosoftAccount)) return false;
        return ((MicrosoftAccount) o).name.equals(this.name);
    }
}
