package badgamesinc.hypnotic.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.utils.Wrapper;
import net.minecraft.command.CommandSource;

public class Modules extends Command {

	public Modules() {
		super("modules", "Gives you a list of all of the modules");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.executes(context -> {
			for (Mod mod : ModuleManager.INSTANCE.modules) {
				Wrapper.tellPlayerRaw(mod.getName());
				Wrapper.tellPlayerRaw(mod.getDescription());
			}
			return SINGLE_SUCCESS;
		});
	}
}