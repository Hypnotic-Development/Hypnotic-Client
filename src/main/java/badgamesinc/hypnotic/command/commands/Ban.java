package badgamesinc.hypnotic.command.commands;

import badgamesinc.hypnotic.command.Command;
import badgamesinc.hypnotic.config.friends.FriendManager;
import net.minecraft.entity.player.PlayerEntity;

public class Ban extends Command {
    public String getAlias() {
        return "ban";
    }

    public String getDescription() {
        return "Bans everyone but you and your friends if you are an operator";
    }

    public String getSyntax() {
        return ".ban";
    }

    public void onCommand(String command, String[] args) throws Exception {
    	System.out.println("hi");
    	for (PlayerEntity player : mc.world.getPlayers()) {
    		if (FriendManager.INSTANCE.isFriend(player) || player == mc.player) {
    			continue;
    		}
			System.out.println("sdf");
			mc.player.sendChatMessage("ban " + player.getName() + " You are sending too many packets! :(");
    	}
    }
}
