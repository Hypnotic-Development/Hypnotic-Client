package badgamesinc.hypnotic.command.commands;

import org.lwjgl.glfw.GLFW;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

import badgamesinc.hypnotic.command.Command;
import badgamesinc.hypnotic.command.argtypes.ModuleArgumentType;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.Wrapper;
import badgamesinc.hypnotic.utils.input.KeyUtils;
import net.minecraft.command.CommandSource;

public class Bind extends Command {

	public Bind() {
		super("bind", "Binds a specified module");
	}

	@Override
	public void build(LiteralArgumentBuilder<CommandSource> builder) {
		builder.then(argument("mod", ModuleArgumentType.module()).then(argument("key", StringArgumentType.string()).executes(context -> {
			Mod mod = context.getArgument("mod", Mod.class);
			String key = context.getArgument("key", String.class);
			mod.setKey(KeyUtils.getKey(key));
			Wrapper.tellPlayer("Bound " + ColorUtils.white + mod.getName() + ColorUtils.gray + " to " + ColorUtils.white + (KeyUtils.getKey(key) != -1 ? GLFW.glfwGetKeyName(KeyUtils.getKey(key), KeyUtils.getKeyScanCode(key)).toUpperCase() : "NONE"));
			return SINGLE_SUCCESS;
		}))); 
	}
	
}
