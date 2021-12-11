package dev.hypnotic.hypnotic_client.module.render;

import org.lwjgl.glfw.GLFW;

import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.settings.settingtypes.BooleanSetting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ColorSetting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ModeSetting;
import dev.hypnotic.hypnotic_client.ui.HudEditorScreen;
import dev.hypnotic.hypnotic_client.ui.OptionsScreen;
import dev.hypnotic.hypnotic_client.ui.WaypointManagerScreen;
import dev.hypnotic.hypnotic_client.ui.clickgui.ClickGUI;
import dev.hypnotic.hypnotic_client.ui.clickgui2.MenuBar;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;

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
				mc.setScreen(mode.is("New") ? ClickGUI.INSTANCE : dev.hypnotic.hypnotic_client.ui.clickgui2.ClickGUI.INSTANCE);
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
