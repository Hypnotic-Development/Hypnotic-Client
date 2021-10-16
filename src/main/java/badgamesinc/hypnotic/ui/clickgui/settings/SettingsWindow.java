package badgamesinc.hypnotic.ui.clickgui.settings;

import java.awt.Color;
import java.util.ArrayList;

import badgamesinc.hypnotic.settings.Setting;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.ui.clickgui.ModuleButton;
import badgamesinc.hypnotic.utils.font.FontManager;
import net.minecraft.client.util.math.MatrixStack;

public class SettingsWindow {

	public ModuleButton parent;
	private ArrayList<Setting> settings;
	private ArrayList<Component> components;
	
	public SettingsWindow(ModuleButton parent) {
		this.parent = parent;
		
		if (parent.mod.getSettings() != null) {
        	settings = parent.mod.getSettings();
        }
		
		components = new ArrayList<Component>();
		
		if (settings != null) {
			int count = 0;
			int x = 300;
			int y = 240;
			for (Setting setting : settings) {
				if (setting instanceof BooleanSetting) {
					this.components.add(new CheckBox(x, y + count, this, setting));
				} else if (setting instanceof ModeSetting) {
					this.components.add(new ComboBox(x, y + count, this, setting));
				} else if (setting instanceof NumberSetting) {
					this.components.add(new Slider(x, y + count, this, setting));
				} else if (setting instanceof ColorSetting) {
					this.components.add(new ColorBox(x, y + count, this, setting));
				}
				count+=20;
			}
		}
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		FontManager.roboto.drawWithShadow(matrices, parent.mod.getDescription(), 260, 135, new Color(0, 0, 0).getRGB());
		for (Component component : components) {
			if (!components.isEmpty())
				component.render(matrices, mouseX, mouseY);
		}
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		for (Component c : components) {
			c.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		for (Component c : components) {
			c.mouseReleased(mouseX, mouseY, button);
		}
	}
	
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
	}
}
