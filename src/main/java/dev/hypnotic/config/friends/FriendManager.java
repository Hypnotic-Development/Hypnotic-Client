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
package dev.hypnotic.config.friends;

import java.util.ArrayList;

import dev.hypnotic.config.SaveLoad;
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
	
	public boolean isFriend(Friend friend) {
		boolean is = false;
		for (Friend f : friends) {
			if (friend == f) {
				is = true;
				break;
			}
		}
		return is;
	}
	
	public boolean isFriend(String friend) {
		for (Friend name : friends) {
			if (name.name.equalsIgnoreCase(friend))
				return true;
        }
		return false;
	}
	
	public boolean remove(Friend friend) {
        if (isFriend(friend)) {
        	getFriends().remove(friend);
            SaveLoad.INSTANCE.save();
            return true;
        }

        return false;
    }
	
	public boolean add(Friend friend) {
        if (friend.name.isEmpty()) return false;

        if (!isFriend(friend)) {
            friends.add(friend);
            SaveLoad.INSTANCE.save();

            return true;
        }

        return false;
    }
	
	public Friend getFriendByName(String name) {
		for (Friend friend : friends) {
			if (friend.name.equals(name)) {
				
				return friend;
			}
		}
		return null;
	}
}
