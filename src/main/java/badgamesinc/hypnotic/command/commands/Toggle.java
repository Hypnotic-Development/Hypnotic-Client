package badgamesinc.hypnotic.command.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import badgamesinc.hypnotic.command.argtypes.ModuleArgumentType;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.utils.ColorUtils;
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
