package badgamesinc.hypnotic.module.render;

import org.lwjgl.glfw.GLFW;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.ui.clickgui2.ClickGUI;

public class ClickGUIModule extends Mod {

	public ClickGUIModule() {
		super("ClickGUI", "The clickable gui", Category.RENDER);
		this.setKey(GLFW.GLFW_KEY_RIGHT_SHIFT);
	}
	
	@Override
	public void onEnable() {
		mc.setScreen(ClickGUI.INSTANCE);
		System.out.println(mc.getSession().getProfile());
		this.toggleSilent();
		super.onEnable();
	}

}
