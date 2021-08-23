package badgamesinc.hypnotic.module.world;

import java.util.ArrayList;
import java.util.UUID;

import com.mojang.authlib.GameProfile;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.utils.player.FakePlayerEntity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.player.PlayerEntity;

public class FakePlayer extends Mod {

	public ArrayList<FakePlayerEntity> fakePlayers = new ArrayList<>();
	private PlayerEntity playerEntity;
	
	public FakePlayer() {
		super("FakePlayer", "spawns a friend", Category.WORLD);
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
		mc.world.removeEntity(1000000, RemovalReason.DISCARDED);
		if (fakePlayers.contains((FakePlayerEntity) playerEntity)) fakePlayers.remove((FakePlayerEntity) playerEntity);
		super.onDisable();
	}
}
