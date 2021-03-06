/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.utils;

import org.jetbrains.annotations.Nullable;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.command.CommandManager;
import dev.hypnotic.mixin.ChatHudAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ChatUtils {

	private static MinecraftClient mc = MinecraftClient.getInstance();
	
	public static void tellPlayerRaw(Text message) {
		mc.inGameHud.getChatHud().addMessage(message);
	}
	
	public static void tellPlayer(Text message) {
		tellPlayerRaw(Text.literal((Hypnotic.chatPrefix)).append(Text.literal(ColorUtils.gray + message.getString())));
	}
	
	public static void tellPlayer(String message) {
        tellPlayer(Text.literal(message));
    }
	
	public static void tellPlayerRaw(String message) {
		tellPlayerRaw(Text.literal(message));
	}
	
	public static Text clickableText(String text, ClickEvent clickEvent, HoverEvent hoverEvent) {
		return Text.literal(text).setStyle(Style.EMPTY.withClickEvent(clickEvent).withHoverEvent(hoverEvent));
	}
	
	public static Text clickableText(String text, ClickEvent clickEvent) {
		return Text.literal(text).setStyle(Style.EMPTY.withClickEvent(clickEvent));
	}
	
    // Default
    public static void info(String message, Object... args) {
        sendMsg(Formatting.GRAY, message, args);
    }

    public static void info(String prefix, String message, Object... args) {
        sendMsg(0, prefix, Formatting.LIGHT_PURPLE, Formatting.GRAY, message, args);
    }

    // Warning
    public static void warning(String message, Object... args) {
        sendMsg(Formatting.YELLOW, message, args);
    }

    public static void warning(String prefix, String message, Object... args) {
        sendMsg(0, prefix, Formatting.LIGHT_PURPLE, Formatting.YELLOW, message, args);
    }

    // Error
    public static void error(String message, Object... args) {
        sendMsg(Formatting.RED, message, args);
    }

    public static void error(String prefix, String message, Object... args) {
        sendMsg(0, prefix, Formatting.LIGHT_PURPLE, Formatting.RED, message, args);
    }

    // Misc
    public static void sendMsg(Text message) {
        sendMsg(null, message);
    }

    public static void sendMsg(String prefix, Text message) {
        sendMsg(0, prefix, Formatting.LIGHT_PURPLE, message);
    }

    public static void sendMsg(Formatting color, String message, Object... args) {
        sendMsg(0, null, null, color, message, args);
    }

    public static void sendMsg(int id, Formatting color, String message, Object... args) {
        sendMsg(id, null, null, color, message, args);
    }

    public static void sendMsg(int id, @Nullable String prefixTitle, @Nullable Formatting prefixColor, Formatting messageColor, String messageContent, Object... args) {
        sendMsg(id, prefixTitle, prefixColor, formatMsg(messageContent, messageColor, args), messageColor);
    }

    public static void sendMsg(int id, @Nullable String prefixTitle, @Nullable Formatting prefixColor, String messageContent, Formatting messageColor) {
        MutableText message = Text.literal(messageContent);
        message.setStyle(message.getStyle().withFormatting(messageColor));
        sendMsg(id, prefixTitle, prefixColor, message);
    }

    public static void sendMsg(int id, @Nullable String prefixTitle, @Nullable Formatting prefixColor, Text msg) {
        if (mc.world == null) return;

        MutableText message = Text.literal("");
        message.append(CommandManager.INSTANCE.getPrefix());
        if (prefixTitle != null) message.append(CommandManager.INSTANCE.getPrefix());
        message.append(msg);

        id = 0;

        ((ChatHudAccessor) mc.inGameHud.getChatHud()).add(message, id);
    }
    
    private static String formatMsg(String format, Formatting defaultColor, Object... args) {
        String msg = String.format(format, args);
        msg = msg.replaceAll("\\(default\\)", defaultColor.toString());
        msg = msg.replaceAll("\\(highlight\\)", Formatting.WHITE.toString());
        msg = msg.replaceAll("\\(underline\\)", Formatting.UNDERLINE.toString());

        return msg;
    }
}
