package badgamesinc.hypnotic.command.commands;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import badgamesinc.hypnotic.command.Command;
import badgamesinc.hypnotic.config.SaveLoad;
import badgamesinc.hypnotic.config.friends.Friend;
import badgamesinc.hypnotic.config.friends.FriendManager;
import badgamesinc.hypnotic.utils.ChatUtils;
import net.minecraft.command.CommandSource;

public class FriendCmd extends Command {

	public FriendCmd() {
		super("friend", "Adds the specified player to your friends list", new String[] {"f", "friend"});
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(literal("add").then(argument("friend", FriendArgumentType.friend())
	                .executes(context -> {
	                    Friend friend = FriendArgumentType.getFriend(context, "friend");
	
	                    if (!FriendManager.INSTANCE.isFriend(friend.name)) {
	                    	FriendManager.INSTANCE.add(friend);
	                    	info("Added (highlight)%s (default)to friends.", friend.name);
	                    }
	                    else error("That person is already your friend.");
	
	                    SaveLoad.INSTANCE.save();
	                    return SINGLE_SUCCESS;
	                })
				)
		);
		
		builder.then(literal("remove").then(argument("friend", FriendArgumentType.friend())
		                .executes(context -> {
		                    Friend friend = FriendArgumentType.getFriend(context, "friend");
		                    FriendManager.INSTANCE.remove(friend);
		                    if (FriendManager.INSTANCE.isFriend(friend.name)) {
		                    	for (Friend friend1 : FriendManager.INSTANCE.friends) {
		                    		if (friend.name == friend1.name) {
		                    			FriendManager.INSTANCE.friends.remove(friend1);
		                    		}
		                    	}
		                    	
		                    	info("Removed (highlight)%s (default)from friends.", friend.name);
		                    }
		                    else error("That person is not your friend.");
		
		                    return SINGLE_SUCCESS;
		                })
		        )
		);
		
		builder.then(literal("list").executes(context -> {
		            info("--- Friends ((highlight)%s(default)) ---", FriendManager.INSTANCE.friends.size());
		            for (Friend friend : FriendManager.INSTANCE.friends) {
		            	ChatUtils.info("(highlight)" + friend.name);
					}
		            return SINGLE_SUCCESS;
		        })
		);
		
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
				Wrapper.tellPlayer("�n�lFriends: " + "(" + FriendManager.INSTANCE.getFriends().size() + ")");
				Wrapper.tellPlayer("");
				for (String friend : FriendManager.INSTANCE.getFriends()) {
					Wrapper.tellPlayer(ColorUtils.green + friend);
				}
			} else {
				Wrapper.tellPlayer("�c�l�oYou have no friends...");
			}
		} else if (args[0] == null || args[1] == null) {
			
		}
		
	}*/
	
	private static class FriendArgumentType implements ArgumentType<Friend> {

        public static FriendArgumentType friend() {
            return new FriendArgumentType();
        }

        @Override
        public Friend parse(StringReader reader) throws CommandSyntaxException {
            return new Friend(reader.readString());
        }

        public static Friend getFriend(CommandContext<?> context, String name) {
            return context.getArgument(name, Friend.class);
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return CommandSource.suggestMatching(mc.getNetworkHandler().getPlayerList().stream()
                    .map(entry -> entry.getProfile().getName()).collect(Collectors.toList()), builder);
        }

        @Override
        public Collection<String> getExamples() {
            return Arrays.asList("seasnail8169", "MineGame159");
        }
    }

}
