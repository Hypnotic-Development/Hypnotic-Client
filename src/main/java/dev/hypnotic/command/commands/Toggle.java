package dev.hypnotic.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import dev.hypnotic.command.Command;
import dev.hypnotic.command.argtypes.ModuleArgumentType;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.utils.ColorUtils;
import net.minecraft.command.CommandSource;

public class Toggle extends Command {

	public Toggle() {
		super("toggle", "Toggles a specified module", "t", "toggle");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("message", ModuleArgumentType.module()).executes(context -> {
			Mod mod = context.getArgument("message", Mod.class);
			for (Mod m : ModuleManager.INSTANCE.modules) {
				if (m.equals(mod)) {
					m.toggle();
					info("Toggled " + ColorUtils.gray + m.getName() + (m.isEnabled() ? ColorUtils.green + " on" : ColorUtils.red + " off"));
				}
			}
			return SINGLE_SUCCESS;
		})); 
	}
	
}
