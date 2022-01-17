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
package dev.hypnotic.utils.input;

import org.lwjgl.glfw.GLFW;

import dev.hypnotic.mixin.KeyBindingAccessor;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class Keys {
    private static final String CATEGORY = "Hypnotic Client";

    public static KeyBinding OPEN_CLICK_GUI = new KeyBinding("key.hypnotic-client.open-click-gui", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, CATEGORY);

    public static int getKey(KeyBinding bind) {
        return ((KeyBindingAccessor) bind).getKey().getCode();
    }
}
