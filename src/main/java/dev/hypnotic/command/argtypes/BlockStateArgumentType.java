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

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.minecraft.block.Block;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.CommandRegistryWrapper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.util.registry.Registry;

//minecraft one needs server context
public class BlockStateArgumentType implements ArgumentType<BlockStateArgument> {
   private static final Collection<String> EXAMPLES = Arrays.asList("stone", "minecraft:stone", "stone[foo=bar]", "foo{bar=baz}");
   private final CommandRegistryWrapper<Block> registryWrapper;

   public BlockStateArgumentType(CommandRegistryAccess commandRegistryAccess) {
       this.registryWrapper = commandRegistryAccess.createWrapper(Registry.BLOCK_KEY);
   }

   public static BlockStateArgumentType blockState(CommandRegistryAccess registryAccess) {
      return new BlockStateArgumentType(registryAccess);
   }

   @Override
   public BlockStateArgument parse(StringReader stringReader) throws CommandSyntaxException {
	   BlockArgumentParser.BlockResult blockResult = BlockArgumentParser.block(this.registryWrapper, stringReader, true);
       return new BlockStateArgument(blockResult.blockState(), blockResult.properties().keySet(), blockResult.nbt());
   }

   public static BlockStateArgument getBlockState(CommandContext<CommandSource> context, String name) {
      return (BlockStateArgument)context.getArgument(name, BlockStateArgument.class);
   }
   
   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
	   return BlockArgumentParser.getSuggestions(this.registryWrapper, builder, false, true);
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }
}
