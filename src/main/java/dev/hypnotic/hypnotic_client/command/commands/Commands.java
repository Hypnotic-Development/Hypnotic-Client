package dev.hypnotic.hypnotic_client.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.hypnotic_client.command.Command;
import dev.hypnotic.hypnotic_client.command.CommandManager;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.Wrapper;
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
