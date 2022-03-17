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
    		mc.options.forwardKey.setPressed(KeyUtils.isPressed(mc.options.forwardKey));
            mc.options.backKey.setPressed(KeyUtils.isPressed(mc.options.backKey));
            mc.options.leftKey.setPressed(KeyUtils.isPressed(mc.options.leftKey));
            mc.options.rightKey.setPressed(KeyUtils.isPressed(mc.options.rightKey));
            if (space.isEnabled()) mc.options.jumpKey.setPressed(KeyUtils.isPressed(mc.options.jumpKey));
            if (shift.isEnabled()) mc.options.sneakKey.setPressed(KeyUtils.isPressed(mc.options.sneakKey));
    	}
    	super.onTick();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
