package dev.hypnotic.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
import dev.hypnotic.command.argtypes.ModuleArgumentType;
import dev.hypnotic.module.Mod;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Wrapper;
import net.minecraft.command.CommandSource;

public class Module extends Command {

	public Module() {
		super("module", "Tells you more about a specific module");
	}
	
	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("module", ModuleArgumentType.module()).executes(context -> {
			Mod mod = context.getArgument("module", Mod.class);
			Wrapper.tellPlayerRaw(ColorUtils.red + "Module" + ColorUtils.gray + ": " + mod.getName());
			Wrapper.tellPlayerRaw(ColorUtils.gray +  mod.getDescription());
			return SINGLE_SUCCESS;
		}));
	}
}
