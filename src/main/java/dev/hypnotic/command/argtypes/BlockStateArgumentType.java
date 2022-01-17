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

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.BlockArgumentParser;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.tag.BlockTags;

//minecraft one needs server context
public class BlockStateArgumentType implements ArgumentType<BlockStateArgument> {
   private static final Collection<String> EXAMPLES = Arrays.asList("stone", "minecraft:stone", "stone[foo=bar]", "foo{bar=baz}");

   public static BlockStateArgumentType blockState() {
      return new BlockStateArgumentType();
   }

   public BlockStateArgument parse(StringReader stringReader) throws CommandSyntaxException {
      BlockArgumentParser blockArgumentParser = (new BlockArgumentParser(stringReader, false)).parse(true);
      return new BlockStateArgument(blockArgumentParser.getBlockState(), blockArgumentParser.getBlockProperties().keySet(), blockArgumentParser.getNbtData());
   }

   public static BlockStateArgument getBlockState(CommandContext<CommandSource> context, String name) {
      return (BlockStateArgument)context.getArgument(name, BlockStateArgument.class);
   }
   
   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
      StringReader stringReader = new StringReader(builder.getInput());
      stringReader.setCursor(builder.getStart());
      BlockArgumentParser blockArgumentParser = new BlockArgumentParser(stringReader, false);

      try {
         blockArgumentParser.parse(true);
      } catch (CommandSyntaxException var6) {
      }

      return blockArgumentParser.getSuggestions(builder, BlockTags.getTagGroup());
   }

   public Collection<String> getExamples() {
      return EXAMPLES;
   }
}
