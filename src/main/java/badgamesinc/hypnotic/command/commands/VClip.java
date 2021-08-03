package badgamesinc.hypnotic.command.commands;

import badgamesinc.hypnotic.command.Command;

public class VClip extends Command {

	@Override
	public String getAlias() {
		return "vclip";
	}

	@Override
	public String getDescription() {
		return "Clip vertically";
	}

	@Override
	public String getSyntax() {
		return ".vclip (distance)";
	}

	@Override
	public void onCommand(String command, String[] args) throws Exception {
		Double v = Double.parseDouble(args[0]);
		mc.player.setPosition(mc.player.getX(), mc.player.getY() + v, mc.player.getZ());
		mc.player.sendChatMessage("Clipped " + v + " blocks vertically");
	}

}
