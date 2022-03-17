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

import org.lwjgl.glfw.GLFW;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.render.ClickGUIModule;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.player.PlayerUtils;

public class Sprint extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Omni", "Omni", "Vanilla");
	
    public Sprint() {
        super("Sprint", "Makes you sprint all the time", Category.MOVEMENT);
        addSetting(mode);
    }

    @Override
    public void onMotion() {
    	ModuleManager.INSTANCE.getModule(ClickGUIModule.class).setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
    	this.setDisplayName("Sprint " + ColorUtils.gray + mode.getSelected());
    	if (mc.player != null && mc.player.input != null) {
	    	if (mode.is("Vanilla")) mc.options.sprintKey.setPressed(true);
	    	else if (mode.is("Omni") && PlayerUtils.isMoving()) mc.player.setSprinting(true);
    	}
    	super.onMotion();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
