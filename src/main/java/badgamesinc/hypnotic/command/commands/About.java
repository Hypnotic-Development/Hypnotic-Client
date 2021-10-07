package badgamesinc.hypnotic.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.command.Command;
import badgamesinc.hypnotic.command.CommandManager;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.Wrapper;
import net.minecraft.command.CommandSource;

public class About extends Command {

	public About() {
		super("about", "For people who need help");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.executes(context -> {
			Wrapper.tellPlayerRaw(ColorUtils.red + "\n\u00A7l\u00A7nHypnotic\n");
			Wrapper.tellPlayerRaw(ColorUtils.red + "Current build" + ColorUtils.gray + ": " + Hypnotic.version);
			Wrapper.tellPlayerRaw(ColorUtils.red + "Modules" + ColorUtils.gray + ": " + ModuleManager.INSTANCE.modules.size());
			Wrapper.tellPlayerRaw(ColorUtils.red + "Commands" + ColorUtils.gray + ": " + CommandManager.get().getAll().size());
			Wrapper.tellPlayerRaw("\n\n\u00A7c                 .,;;;,.                \n"
					+ "                .xXNNN0:    .....       \n"
					+ "               .lXMMMNd.   ;OXXKd.      \n"
					+ "               :KWMWNk'   ,OWMM0:       \n"
					+ "              'kWMW0d;   .dNMMNo.       \n"
					+ "             .oNMMXl..   cXMMWx.        \n"
					+ "            .cKMMWk,.  .;OWMW0:.        \n"
					+ "  ..;ldxkkxxOXWMMWKkkxkOXWMMWXOxxxxxxxdo\n"
					+ ".:kXWWXOdc:oKMMMKo:;;;l0WMMNkc;;;;;;;;,.\n"
					+ "ONMWKd,.  .dXMMKc.    ;0MMWO'           \n"
					+ "MMMXc.   .dNMWO:     'OWMM0:            \n"
					+ "MMWXc...l0NNKo.    ..cKNNKl.            \n"
					+ "OKNWKOkO0Od:.        .','..             \n"
					+ "..;:cc:,..   \n");
			return SINGLE_SUCCESS;
		});
	}
	
}