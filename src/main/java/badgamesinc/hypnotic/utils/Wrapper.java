package badgamesinc.hypnotic.utils;

import badgamesinc.hypnotic.Hypnotic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class Wrapper {

	public static MinecraftClient mc = MinecraftClient.getInstance();
	
	public static void tellPlayer(String message) {
        Text textComponentString = new LiteralText(ColorUtils.gray + message);
        mc.inGameHud.getChatHud().addMessage(new LiteralText(Hypnotic.chatPrefix).append(textComponentString));
    }
	
	public static void tellPlayerRaw(String message) {
		mc.inGameHud.getChatHud().addMessage(new LiteralText(message));
	}
}
