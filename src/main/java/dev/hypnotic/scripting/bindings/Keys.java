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
*/
package dev.hypnotic.scripting.bindings;

import static dev.hypnotic.utils.MCUtils.mc;

import dev.hypnotic.utils.input.KeyUtils;
import net.minecraft.client.option.KeyBinding;

/**
* @author BadGamesInc
*/
public class Keys {

	public boolean isKeybindPressed(String keyName) {
		Key key = Enum.valueOf(Key.class, keyName.toUpperCase());
		return KeyUtils.isPressed(key.getBind());
	}
	
	public enum Key {
		JUMP(mc.options.jumpKey),
		ATTACK(mc.options.attackKey),
		SNEAK(mc.options.sneakKey),
		CHAT(mc.options.chatKey),
		DROP(mc.options.dropKey),
		FORWARD(mc.options.forwardKey),
		BACKWARD(mc.options.backKey),
		LEFT(mc.options.leftKey),
		RIGHT(mc.options.rightKey),
		INVENTORY(mc.options.inventoryKey),
		TAB(mc.options.playerListKey),
		SPRINT(mc.options.sprintKey),
		USE(mc.options.useKey);
		
		KeyBinding keybind;
		
		Key(KeyBinding keybind) {
			this.keybind = keybind;
		}
		
		public KeyBinding getBind() {
			return keybind;
		}
	}
}
