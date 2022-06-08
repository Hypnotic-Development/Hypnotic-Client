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
package dev.hypnotic.command.argtypes;

import static dev.hypnotic.utils.MCUtils.mc;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class PlayerArgumentType implements ArgumentType<PlayerEntity> {

    private static Collection<String> EXAMPLES;

    static {
        if (mc.world != null) {
            EXAMPLES = mc.world.getPlayers()
                .stream()
                .limit(3)
                .map(PlayerEntity::getEntityName)
                .collect(Collectors.toList());
        }
    }

    private static final DynamicCommandExceptionType NO_SUCH_PLAYER = new DynamicCommandExceptionType(o ->
            Text.literal("Player with name " + o + " doesn't exist."));

    public static PlayerArgumentType player() {
        return new PlayerArgumentType();
    }

    public static PlayerEntity getPlayer(CommandContext<?> context, String argument) {
        return context.getArgument(argument, PlayerEntity.class);
    }

    @Override
    public PlayerEntity parse(StringReader reader) throws CommandSyntaxException {
        String argument = reader.readString();
        PlayerEntity playerEntity = null;
        for (PlayerEntity p : mc.world.getPlayers()) {
            if (p.getEntityName().equalsIgnoreCase(argument)) {
                playerEntity = p;
                break;
            }
        }
        if (playerEntity == null) throw NO_SUCH_PLAYER.create(argument);
        return playerEntity;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return CommandSource.suggestMatching(mc.world.getPlayers().stream().map(PlayerEntity::getEntityName), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}