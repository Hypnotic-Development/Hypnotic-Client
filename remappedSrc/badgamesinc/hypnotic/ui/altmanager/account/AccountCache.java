/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client/).
 * Copyright (c) 2021 Meteor Development.
 */

package badgamesinc.hypnotic.ui.altmanager.account;

import badgamesinc.hypnotic.utils.ISerializable;
import badgamesinc.hypnotic.utils.NbtException;
import net.minecraft.nbt.NbtCompound;

public class AccountCache implements ISerializable<AccountCache> {
    public String username = "";
    public String uuid = "";

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        tag.putString("username", username);
        tag.putString("uuid", uuid);

        return tag;
    }

    @Override
    public AccountCache fromTag(NbtCompound tag) {
        if (!tag.contains("username") || !tag.contains("uuid")) throw new NbtException();

        username = tag.getString("username");
        uuid = tag.getString("uuid");

        return this;
    }
}
