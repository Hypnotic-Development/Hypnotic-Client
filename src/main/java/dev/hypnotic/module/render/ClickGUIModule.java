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
package dev.hypnotic.module.render;

import org.lwjgl.glfw.GLFW;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.ui.HudEditorScreen;
import dev.hypnotic.ui.OptionsScreen;
import dev.hypnotic.ui.WaypointManagerScreen;
import dev.hypnotic.ui.clickgui.ClickGUI;
import dev.hypnotic.ui.clickgui2.MenuBar;
import dev.hypnotic.utils.ColorUtils;

public class ClickGUIModule extends Mod {
	
	public ModeSetting mode = new ModeSetting("Style", "New", "New", "Dropdown");
	public ModeSetting customColor = new ModeSetting("Color Mode", "Custom", "Category", "Custom");
	public BooleanSetting clickAnimation = new BooleanSetting("Click Animation", true);
	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	
	public ClickGUIModule() {
		super("ClickGUI", "The clickable gui", Category.RENDER);
		this.setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
		addSettings(mode, clickAnimation, customColor, color);
	}
	
	@Override
	public void onEnable() {
		switch(MenuBar.INSTANCE.getCurrentTab()) {
			case CLICKGUI:
				MenuBar.INSTANCE.setCurrentTab(MenuBar.Tab.CLICKGUI);
				mc.setScreen(mode.is("New") ? ClickGUI.INSTANCE : dev.hypnotic.ui.clickgui2.ClickGUI.INSTANCE);
				break;
			case HUDEDITOR:
				MenuBar.INSTANCE.setCurrentTab(MenuBar.Tab.HUDEDITOR);
				mc.setScreen(HudEditorScreen.INSTANCE);
				break;
			case OPTIONS:
				MenuBar.INSTANCE.setCurrentTab(MenuBar.Tab.OPTIONS);
				mc.setScreen(OptionsScreen.INSTANCE);
				break;
			case WAYPOINT:
				MenuBar.INSTANCE.setCurrentTab(MenuBar.Tab.WAYPOINT);
				mc.setScreen(WaypointManagerScreen.INSTANCE);
				break;
			default:
				break;
		}
		
		this.toggleSilent();
		super.onEnable();
	}

}
