/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client/).
 * Copyright (c) 2021 Meteor Development.
 */

package dev.hypnotic.hypnotic_client.ui.altmanager.account;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;

import dev.hypnotic.hypnotic_client.ui.altmanager.account.types.CrackedAccount;
import dev.hypnotic.hypnotic_client.ui.altmanager.account.types.MicrosoftAccount;
import dev.hypnotic.hypnotic_client.ui.altmanager.account.types.PremiumAccount;
import dev.hypnotic.hypnotic_client.ui.altmanager.account.types.TheAlteningAccount;
import dev.hypnotic.hypnotic_client.utils.ISerializable;
import dev.hypnotic.hypnotic_client.utils.NbtException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class Accounts extends System<Accounts> implements Iterable<Account<?>> {
    private List<Account<?>> accounts = new ArrayList<>();

    public Accounts() {
        super("accounts");
    }

    public static Accounts get() {
        return new Accounts();
    }

    @Override
    public void init() {}

    public void add(Account<?> account) {
        accounts.add(account);
        save();
    }

    public boolean exists(Account<?> account) {
        return accounts.contains(account);
    }

    public void remove(Account<?> account) {
        if (accounts.remove(account)) {
            save();
        }
    }

    public int size() {
        return accounts.size();
    }

    @Override
    public Iterator<Account<?>> iterator() {
        return accounts.iterator();
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = new NbtCompound();

        tag.put("accounts", listToTag(accounts));

        return tag;
    }

    @Override
    public Accounts fromTag(NbtCompound tag) {
    	Executors.newSingleThreadExecutor().execute(() -> accounts = listFromTag(tag.getList("accounts", 10), tag1 -> {
            NbtCompound t = (NbtCompound) tag1;
            if (!t.contains("type")) return null;

            AccountType type = AccountType.valueOf(t.getString("type"));

            try {
                @SuppressWarnings("unused")
				Account<?> account = switch (type) {
                    case Cracked ->    new CrackedAccount(null).fromTag(t);
                    case Premium ->    new PremiumAccount(null, null).fromTag(t);
                    case Microsoft ->  new MicrosoftAccount(null).fromTag(t);
                    case TheAltening -> new TheAlteningAccount(null).fromTag(t);
                };

            } catch (NbtException e) {
                return null;
            }

            return null;
        }));

        return this;
    }
    
    public static <T extends ISerializable<?>> NbtList listToTag(Iterable<T> list) {
        NbtList tag = new NbtList();
        for (T item : list) tag.add(item.toTag());
        return tag;
    }
    
    public static <T> List<T> listFromTag(NbtList tag, ToValue<T> toItem) {
        List<T> list = new ArrayList<>(tag.size());
        for (NbtElement itemTag : tag) {
            T value = toItem.toValue(itemTag);
            if (value != null) list.add(value);
        }
        return list;
    }
    
    public interface ToValue<T> {
        T toValue(NbtElement tag);
    }
}
