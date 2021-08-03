package badgamesinc.hypnotic.command.commands;

import java.util.Locale;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import badgamesinc.hypnotic.config.SaveLoad;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import net.minecraft.client.util.InputUtil;
import net.minecraft.command.CommandSource;

public class Bind extends Command {

	public Bind() {
		super("bind", "Binds a module to the specified key", new String[] {"bind"});
	}
	
	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("module", ModuleArgumentType.module()).then(argument("key", StringArgumentType.string())).executes(c -> {
			Mod mod = c.getArgument("module", Mod.class);
			String keyName = StringArgumentType.getString(c, "key");
			mod.setKey(InputUtil.fromTranslationKey("key.keyboard." + keyName.toLowerCase(Locale.ENGLISH)).getCode());
			return SINGLE_SUCCESS;
		}));
	}

	/*@Override
	public void onCommand(String command, String[] args) throws Exception {
		if (args.length == 2) {
				String moduleName = args[0];
				String keyName = args[1];

			for(Mod m : ModuleManager.INSTANCE.modules) {
				m.getName().replaceAll(" ", "");
				if(m.getName().equalsIgnoreCase(moduleName)) {
					if (args[1].length() == 1) {
						m.setKey(InputUtil.fromTranslationKey("key.keyboard." + keyName.toLowerCase(Locale.ENGLISH)).getCode());
						SaveLoad.INSTANCE.save();
//						mc.player.sendChatMessage(String.format("Bound %s to %s", ColorUtils.white + m.getName() + ColorUtils.gray, ColorUtils.white + GLFW.glfwGetKeyName(m.getKey(), GLFW.glfwGetKeyScancode(m.getKey()))) + "     ");
						if (args[1].length() != 1) {
							mc.player.sendChatMessage(String.format(args[1]));
						
						}
					} else if (args[1].equalsIgnoreCase("none")){
						m.setKey(0);
						mc.player.sendChatMessage("Unbound " + m.getName());
					}
				}
			}
		}

		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("clear")) {
				for(Mod m : ModuleManager.INSTANCE.modules) {
					m.setKey(0);
				}
//				Wrapper.tellPlayer("Cleared all binds");
			} else {}
//				Wrapper.tellPlayer(args[0] + " is not a module");
		}

		if(args[0] == null) {
//			Wrapper.tellPlayer("Usage: " + getSyntax());
		}

	}*/

}
