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
package dev.hypnotic.module.movement;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.utils.input.KeyUtils;
import net.minecraft.client.gui.screen.ChatScreen;

public class InvMove extends Mod {

	public BooleanSetting space = new BooleanSetting("Jump", true);
	public BooleanSetting shift = new BooleanSetting("Sneak", true);

	public InvMove() {
        super("InvMove", "Allows you to move inside of you inventory", Category.MOVEMENT);
        addSettings(space, shift);
    }

    @Override
    public void onTick() {
    	if (mc.currentScreen != null && !(mc.currentScreen instanceof ChatScreen)) {
    		mc.options.keyForward.setPressed(KeyUtils.isPressed(mc.options.keyForward));
            mc.options.keyBack.setPressed(KeyUtils.isPressed(mc.options.keyBack));
            mc.options.keyLeft.setPressed(KeyUtils.isPressed(mc.options.keyLeft));
            mc.options.keyRight.setPressed(KeyUtils.isPressed(mc.options.keyRight));
            if (space.isEnabled()) mc.options.keyJump.setPressed(KeyUtils.isPressed(mc.options.keyJump));
            if (shift.isEnabled()) mc.options.keySneak.setPressed(KeyUtils.isPressed(mc.options.keySneak));
    	}
    	super.onTick();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
