package badgamesinc.hypnotic.utils.world;

import badgamesinc.hypnotic.config.friends.FriendManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class EntityUtils {

	public static boolean isAttackable(Entity e, boolean ignoreFriends) {
		return e instanceof LivingEntity
				&& e.isAlive()
				&& e != MinecraftClient.getInstance().player
				&& !e.isConnectedThroughVehicle(MinecraftClient.getInstance().player)
				&& (!ignoreFriends || !FriendManager.INSTANCE.isFriend((LivingEntity)e));
	}
}
