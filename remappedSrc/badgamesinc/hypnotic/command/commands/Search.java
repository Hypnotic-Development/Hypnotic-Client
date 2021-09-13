package badgamesinc.hypnotic.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import badgamesinc.hypnotic.command.argtypes.BlockStateArgumentType;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.utils.ColorUtils;
import net.minecraft.block.Block;
import net.minecraft.command.CommandSource;

public class Search extends Command {

	badgamesinc.hypnotic.module.render.Search search = ModuleManager.INSTANCE.getModule(badgamesinc.hypnotic.module.render.Search.class);
	public Search() {
		super("search", "Add blocks to search", "search");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(
                literal("add").then(argument("block", BlockStateArgumentType.blockState()).executes(context -> {
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
                literal("del").then(argument("block", BlockStateArgumentType.blockState()).executes(c -> {
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
