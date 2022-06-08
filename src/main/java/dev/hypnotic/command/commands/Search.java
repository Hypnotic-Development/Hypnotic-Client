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

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
import dev.hypnotic.command.argtypes.BlockStateArgumentType;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.utils.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;

public class Search extends Command {

	dev.hypnotic.module.render.Search search = ModuleManager.INSTANCE.getModule(dev.hypnotic.module.render.Search.class);
	public Search() {
		super("search", "Add blocks to search", "search");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(
                literal("add").then(argument("block", BlockStateArgumentType.blockState(registryAccess)).executes(context -> {
                    Block block = BlockStateArgumentType.getBlockState(context, "block").getBlockState().getBlock();

                    if(block == null) error("Block not Found!");
                    else {
                        if(!search.blocks.contains(block)) {
                        	search.blocks.add(block);
                            info("Added " + ColorUtils.purple + block.getName().getString() + ColorUtils.white + " to search list!");
                        }
                            else error("Block is already in search list!");
                    }

                    return 1;
                }))).then(
                literal("del").then(argument("block", BlockStateArgumentType.blockState(registryAccess)).executes(c -> {
                    Block block = BlockStateArgumentType.getBlockState(c, "block").getBlockState().getBlock();

                    if(block == null) error("Block not Found!");
                    else {
                        if(search.blocks.contains(block)) {
                        	search.blocks.remove(block);
                            info("Deleted " + ColorUtils.purple + block.getName().getString() + ColorUtils.white + " from search list!");
                        } else info("Block isn't in search list!");
                    }

                    return 1;
                }))).then(
                literal("list").executes(c -> {
                    search.blocks.forEach(block -> {
                    	info(block.asItem().getName());
                    });
                    return 1;
                })
        );
		
	}

}
