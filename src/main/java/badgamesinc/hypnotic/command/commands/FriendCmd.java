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
		super("friend", "Adds the specified player to your friends list", "f", "friend");
	}
	
	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(literal("add").then(argument("friend", FriendArgumentType.friend())
	                .executes(context -> {
	                    Friend friend = FriendArgumentType.getFriend(context, "friend");
	
	                    if (!FriendManager.INSTANCE.isFriend(friend.name)) {
	                    	FriendManager.INSTANCE.add(friend);
	                    	info("Added (highlight)" + friend.name + " (default)to your friends list.");
	                    }
	                    else info("That person is already your friend.");
	
	                    SaveLoad.INSTANCE.save();
	                    return SINGLE_SUCCESS;
	                })
				)
		);
		
		builder.then(literal("remove").then(argument("friend", FriendArgumentType.friend())
		                .executes(context -> {
		                    Friend friend = FriendManager.INSTANCE.getFriendByName(FriendArgumentType.getFriend(context, "friend").name);
		                    if (friend != null && FriendManager.INSTANCE.isFriend(friend.name)) {
		                    	FriendManager.INSTANCE.friends.remove(friend);
		                    	info("Removed (highlight)" + friend.name + " (default)from your friends list.");
		                    }
		                    else info("That person is not your friend.");
		
		                    return SINGLE_SUCCESS;
		                })
		        )
		);
		
		builder.then(literal("list").executes(context -> {
		            info("--- Friends ((highlight)" + FriendManager.INSTANCE.friends.size() + "(default)) ---");
		            for (Friend friend : FriendManager.INSTANCE.friends) {
		            	ChatUtils.info("(highlight)" + friend.name);
					}
		            return SINGLE_SUCCESS;
		        })
		);
		
	}
	
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
            return Arrays.asList("BadGamesInc", "PCPinger");
        }
    }

}
