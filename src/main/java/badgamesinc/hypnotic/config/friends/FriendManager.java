package badgamesinc.hypnotic.config.friends;

import java.util.ArrayList;

import badgamesinc.hypnotic.config.SaveLoad;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

public class FriendManager {

	public static FriendManager INSTANCE = new FriendManager();
	public ArrayList<Friend> friends = new ArrayList<>();
	
	public FriendManager() {
		
	}
	
	public ArrayList<Friend> getFriends() {
		return friends;
	}
	
	public void addFriend(Friend friend) {
		friends.add(friend);
	}
	
	public boolean isFriend(LivingEntity friend) {
		for (Friend f : friends) {
			if (friend instanceof PlayerEntity)
	            if (f.name.equalsIgnoreCase(friend.getName().asString()))
	                return true;
        }
		return false;
	}
	
	public boolean isFriend(String friend) {
		for (Friend name : friends) {
			if (name.name.equalsIgnoreCase(friend));
				return true;
        }
		return false;
	}
	
	public boolean remove(Friend friend) {
        if (friends.remove(friend)) {
            SaveLoad.INSTANCE.save();
            return true;
        }

        return false;
    }
	
	public boolean add(Friend friend) {
        if (friend.name.isEmpty()) return false;

        if (!friends.contains(friend)) {
            friends.add(friend);
            SaveLoad.INSTANCE.save();

            return true;
        }

        return false;
    }
}
