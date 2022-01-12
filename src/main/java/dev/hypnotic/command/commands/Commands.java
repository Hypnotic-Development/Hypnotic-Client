package dev.hypnotic.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
import dev.hypnotic.command.CommandManager;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Wrapper;
import net.minecraft.command.CommandSource;

public class Commands extends Command {

	public Commands() {
		super("commands", "Gives you a list of all of the commands");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.executes(context -> {
			for (Command cmd : CommandManager.get().getAll()) {
				Wrapper.tellPlayerRaw(ColorUtils.red + "Command" + ColorUtils.gray + ": " + cmd.getName());
				Wrapper.tellPlayerRaw(ColorUtils.gray + cmd.getDescription());
			}
			return SINGLE_SUCCESS;
		});
	}
}
