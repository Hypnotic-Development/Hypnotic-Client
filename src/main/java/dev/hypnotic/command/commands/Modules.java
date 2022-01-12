package dev.hypnotic.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Wrapper;
import net.minecraft.command.CommandSource;

public class Modules extends Command {

	public Modules() {
		super("modules", "Gives you a list of all of the modules");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.executes(context -> {
			for (Mod mod : ModuleManager.INSTANCE.modules) {
				Wrapper.tellPlayerRaw(ColorUtils.red + "Module" + ColorUtils.gray + ": " + mod.getName());
				Wrapper.tellPlayerRaw(ColorUtils.gray +  mod.getDescription());
			}
			return SINGLE_SUCCESS;
		});
	}
}