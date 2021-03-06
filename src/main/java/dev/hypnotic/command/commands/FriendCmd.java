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
package dev.hypnotic.command.commands;

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

import dev.hypnotic.command.Command;
import dev.hypnotic.config.PositionsConfig;
import dev.hypnotic.config.friends.Friend;
import dev.hypnotic.config.friends.FriendManager;
import dev.hypnotic.utils.ChatUtils;
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
	
	                    PositionsConfig.INSTANCE.save();
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
