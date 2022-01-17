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

import java.util.Optional;

import dev.hypnotic.ui.altmanager.account.Account;
import dev.hypnotic.ui.altmanager.account.AccountType;
import net.minecraft.client.util.Session;

//Credits to meteor client (https://github.com/MeteorDevelopment/meteor-client)
public class CrackedAccount extends Account<CrackedAccount> {
    public CrackedAccount(String name) {
        super(AccountType.Cracked, name);

    }

    @Override
    public boolean fetchInfo() {
        cache.username = name;
        return true;
    }

    @Override
    public boolean login() {
        super.login();

        setSession(new Session(name, "", "", Optional.empty(), Optional.empty(), Session.AccountType.MOJANG));
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof CrackedAccount)) return false;
        return ((CrackedAccount) o).getUsername().equals(this.getUsername());
    }
}
