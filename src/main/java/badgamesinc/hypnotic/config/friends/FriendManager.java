package badgamesinc.hypnotic.config.friends;

import java.util.ArrayList;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class FriendManager {

	public static FriendManager INSTANCE = new FriendManager();
	public ArrayList<String> friends = new ArrayList<>();
	
	public FriendManager() {
		
	}
	
	public ArrayList<String> getFriends() {
		return friends;
	}
	
	public void addFriend(String friend) {
		friends.add(friend);
	}
	
	public boolean isFriend(LivingEntity friend) {
		for (String name : friends) {
			if (friend instanceof PlayerEntity)
	            if (friend.getName().asString().equalsIgnoreCase(name))
	                return true;
        }
		return false;
	}
	
	public boolean isFriend(String friend) {
		for (String name : friends) {
			friend = name;
			for (Entity e : MinecraftClient.getInstance().world.getEntities()) {
				if (e instanceof PlayerEntity)
					if (e.getName().asString().equalsIgnoreCase(name))
			            return true;
			}
        }
		return false;
	}
}
