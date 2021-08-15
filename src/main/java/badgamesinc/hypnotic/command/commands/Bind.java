package badgamesinc.hypnotic.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import badgamesinc.hypnotic.utils.Wrapper;
import net.minecraft.command.CommandSource;

public class Bind extends Command {

	public Bind() {
		super("bind", "Brings you to the module binding screen", new String[] {"bind"});
	}
	
	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("module", ModuleArgumentType.module()).executes(c -> {
			Wrapper.tellPlayer("Please use the binding screen from the ClickGUI by middle clicking the module");
			return SINGLE_SUCCESS;
		}));
	}
}
