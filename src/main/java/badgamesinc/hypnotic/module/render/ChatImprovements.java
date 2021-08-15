package badgamesinc.hypnotic.module.render;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventSendMessage;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import it.unimi.dsi.fastutil.chars.Char2CharArrayMap;
import it.unimi.dsi.fastutil.chars.Char2CharMap;

public class ChatImprovements extends Mod {

	public ModeSetting mode = new ModeSetting("Fancy Mode", "1", "1", "2", "3");
	public BooleanSetting infinite = new BooleanSetting("Infinite", true);
	public BooleanSetting fancyChat = new BooleanSetting("Fancy Chat", true);
	private final Char2CharMap SMALL_CAPS = new Char2CharArrayMap();
	private final Char2CharMap SMALL_CAPS2 = new Char2CharArrayMap();
	private final String blacklist = "(){}[]|";
	
	public ChatImprovements() {
		super("ChatImprovements", "Improvements for the chat", Category.RENDER);
		addSettings(mode, fancyChat, infinite);
		String[] a = "abcdefghijklmnopqrstuvwxyz".split("");
        String[] b = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴩqʀꜱᴛᴜᴠᴡxyᴢ".split("");
        for (int i = 0; i < a.length; i++) SMALL_CAPS.put(a[i].charAt(0), b[i].charAt(0));
        String[] a2 = "abcdefghijklmnopqrstuvwxyz".split("");
        String[] b2 = "ɐqɔpǝɟɓɥıɾʞlɯuodbɹsʇnʌʍxʎz".split("");
        for (int i = 0; i < a2.length; i++) SMALL_CAPS2.put(a2[i].charAt(0), b2[i].charAt(0));
	}
	
	@Override
	public void onEnable() {
		super.onEnable();
	}
	
	@EventTarget
	public void sendMessage(EventSendMessage event) {
		String message = event.getMessage();
		switch(mode.getSelected()) {
			case "1":
				event.setMessage(convertString(message));
				break;
			case "2":
				event.setMessage(applyFancy(message));
				break;
			case "3":
				event.setMessage(applyUpsidedown(message));
				break;
		}
	}

	private String applyFancy(String message) {
        StringBuilder sb = new StringBuilder();
        
        for (char ch : message.toCharArray()) {
            if (SMALL_CAPS.containsKey(ch)) sb.append(SMALL_CAPS.get(ch));
            else sb.append(ch);
        }
        return sb.toString();
    }
	
	private String applyUpsidedown(String message) {
        StringBuilder sb = new StringBuilder();
        
        for (char ch : message.toCharArray()) {
            if (SMALL_CAPS2.containsKey(ch)) sb.append(SMALL_CAPS2.get(ch));
            else sb.append(ch);
        }
        return sb.toString();
    }
	
	private String convertString(String input)
	{
		String output = "";
		for(char c : input.toCharArray())
			output += convertChar(c);
		
		return output;
	}
	
	private String convertChar(char c)
	{
		if(c < 0x21 || c > 0x80)
			return "" + c;
		
		if(blacklist.contains(Character.toString(c)))
			return "" + c;
		
		return new String(Character.toChars(c + 0xfee0));
	}
}
