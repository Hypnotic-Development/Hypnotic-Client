package badgamesinc.hypnotic.command;

import net.minecraft.client.MinecraftClient;

public abstract class Command {

	public MinecraftClient mc = MinecraftClient.getInstance();
	public abstract String getAlias();
	public abstract String getDescription();
	public abstract String getSyntax();
	public abstract void onCommand(String command, String[] args) throws Exception;
}
