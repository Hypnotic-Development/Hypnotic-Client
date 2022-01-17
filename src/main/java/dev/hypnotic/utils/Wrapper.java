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

import dev.hypnotic.Hypnotic;
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
