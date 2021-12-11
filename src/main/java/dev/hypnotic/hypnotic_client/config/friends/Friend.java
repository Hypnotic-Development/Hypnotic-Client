package dev.hypnotic.hypnotic_client.config.friends;

import net.minecraft.entity.player.PlayerEntity;

public class Friend {
	public String name;


    public Friend(String name) {
        this.name = name;
    }

    public Friend(PlayerEntity player) {
        this(player.getEntityName());
    }
}
