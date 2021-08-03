package badgamesinc.hypnotic.command.commands;

import badgamesinc.hypnotic.command.Command;

public class VClip extends Command {
    public String getAlias() {
        return "vclip";
    }

    public String getDescription() {
        return "Clip vertically";
    }

    public String getSyntax() {
        return ".vclip (distance)";
    }

    public void onCommand(String command, String[] args) throws Exception {
        mc.player.setPosition(this.mc.player.getX(), this.mc.player.getY() + Double.parseDouble(args[0]), this.mc.player.getZ());
    }
}
