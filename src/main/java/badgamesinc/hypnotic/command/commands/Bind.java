package badgamesinc.hypnotic.command.commands;

import java.util.Locale;

import badgamesinc.hypnotic.command.Command;
import badgamesinc.hypnotic.config.SaveLoad;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import net.minecraft.client.util.InputUtil;

public class Bind extends Command {

	@Override
	public String getAlias() 
	{
		return "bind";
	}

	@Override
	public String getDescription() 
	{
		return "Binds modules to a specified key";
	}

	@Override
	public String getSyntax() 
	{
		return ".bind (key) (module)";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		System.out.println("hell");
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

	}

}
