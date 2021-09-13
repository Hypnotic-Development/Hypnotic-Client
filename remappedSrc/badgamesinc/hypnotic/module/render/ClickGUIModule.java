package badgamesinc.hypnotic.module.render;

import org.lwjgl.glfw.GLFW;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.ui.HudEditorScreen;
import badgamesinc.hypnotic.ui.clickgui2.ClickGUI;
import badgamesinc.hypnotic.ui.clickgui2.MenuBar;
import badgamesinc.hypnotic.utils.ColorUtils;

public class ClickGUIModule extends Mod 
{
	public ModeSetting customColor = new ModeSetting("Color Mode", "Category", "Category", "Custom");
	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	
	public ClickGUIModule() {
		super("ClickGUI", "The clickable gui", Category.RENDER);
		this.setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
		addSettings(customColor, color);
	}
	
	@Override
	public void onEnable() {
		switch(MenuBar.INSTANCE.getCurrentTab()) {
			case CLICKGUI:
				MenuBar.INSTANCE.setCurrentTab(MenuBar.Tab.CLICKGUI);
				mc.setScreen(ClickGUI.INSTANCE);
				break;
			case HUDEDITOR:
				MenuBar.INSTANCE.setCurrentTab(MenuBar.Tab.HUDEDITOR);
				mc.setScreen(HudEditorScreen.INSTANCE);
				break;
			case OPTIONS:
				break;
			case TERMINAL:
				break;
			default:
				break;
		}
		
		this.toggleSilent();
		super.onEnable();
	}

}
