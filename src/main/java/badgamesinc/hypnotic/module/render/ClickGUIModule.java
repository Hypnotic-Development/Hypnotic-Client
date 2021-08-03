package badgamesinc.hypnotic.module.render;

import org.lwjgl.glfw.GLFW;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.ui.clickgui2.ClickGUI;

public class ClickGUIModule extends Mod {

	public NumberSetting red = new NumberSetting("Red", 255, 0, 255, 1);
	public NumberSetting green = new NumberSetting("Green", 20, 0, 255, 1);
	public NumberSetting blue = new NumberSetting("Blue", 100, 0, 255, 1);
	
	public ClickGUIModule() {
		super("ClickGUI", "The clickable gui", Category.RENDER);
		this.setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
		addSetting(red);
		addSetting(green);
		addSetting(blue);
	}
	
	@Override
	public void onEnable() {
		mc.setScreen(ClickGUI.INSTANCE);
		this.toggleSilent();
		super.onEnable();
	}

}
