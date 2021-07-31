package badgamesinc.hypnotic.command;

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
		mc.player.setPosition(mc.player.getX(), mc.player.getY() + Double.parseDouble(args[0]), mc.player.getZ());
	}

}
