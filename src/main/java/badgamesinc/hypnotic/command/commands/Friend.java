package badgamesinc.hypnotic.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import badgamesinc.hypnotic.config.SaveLoad;
import badgamesinc.hypnotic.config.friends.FriendManager;
import net.minecraft.command.CommandSource;

public class Friend extends Command {

	public Friend() {
		super("friend", "Adds the specified player to your friends list", new String[] {"f", "friend"});
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(literal("add").then(argument("friend", StringArgumentType.word())).executes(c -> {
			System.out.println(c.getInput());
			FriendManager.INSTANCE.addFriend(StringArgumentType.getString(c, "friend"));
			SaveLoad.INSTANCE.save();
			info("Added " + StringArgumentType.getString(c, "friend") + "to your friends list");
			return SINGLE_SUCCESS;
		}));
		
	}

	/*@Override
	public void onCommand(String command, String[] args) throws Exception {
		if (args[0].equalsIgnoreCase("add")) {
			
			//if (!FriendManager.INSTANCE.isFriend(args[1])) {
				FriendManager.INSTANCE.addFriend(args[1]);
				SaveLoad.INSTANCE.save();
				//} else if (FriendManager.INSTANCE.isFriend(args[1])) {
				//NotificationManager.getNotificationManager().createNotification(ColorUtils.green + args[1] + ColorUtils.white + " is already on your friends list!", "", true, (int) 5, Type.WARNING, Color.RED);
			//}
		} else if (args[0].equalsIgnoreCase("remove")) {
			FriendManager.INSTANCE.friends.remove(args[1]);
			SaveLoad.INSTANCE.save();
		} else if (args[0].equalsIgnoreCase("list")) {
			if (FriendManager.INSTANCE.getFriends().size() != 0) {
				Wrapper.tellPlayer("§n§lFriends: " + "(" + FriendManager.INSTANCE.getFriends().size() + ")");
				Wrapper.tellPlayer("");
				for (String friend : FriendManager.INSTANCE.getFriends()) {
					Wrapper.tellPlayer(ColorUtils.green + friend);
				}
			} else {
				Wrapper.tellPlayer("§c§l§oYou have no friends...");
			}
		} else if (args[0] == null || args[1] == null) {
			
		}
		
	}*/

}
