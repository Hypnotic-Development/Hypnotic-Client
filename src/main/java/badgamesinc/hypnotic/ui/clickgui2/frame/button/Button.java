package badgamesinc.hypnotic.ui.clickgui2.frame.button;

import java.awt.Color;
import java.util.ArrayList;

import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.Setting;
import badgamesinc.hypnotic.settings.settingtypes.*;
import badgamesinc.hypnotic.ui.clickgui2.frame.Frame;
import badgamesinc.hypnotic.ui.clickgui2.frame.button.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class Button {

	public Mod mod;
	public Frame parent;
	private int x, y, width, height;
	private boolean extended;
	private ArrayList<Component> components;
	
	public Button(Mod mod, int x, int y, Frame parent) {
		this.mod = mod;
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.width = parent.getWidth();
		this.height = parent.getHeight();
		this.extended = false;
		this.components = new ArrayList<Component>();
		
		int count = height;
		for (Setting setting : mod.getSettings()) {
			if (setting instanceof BooleanSetting) {
				components.add(new CheckBox(x, y + count, this, setting));
			} else if (setting instanceof ModeSetting) {
				components.add(new ComboBox(x, y + count, setting, this));
			} else if (setting instanceof NumberSetting) {
				components.add(new Slider(x, y + count, this, setting));
			}
			count+=height;
		}
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		Screen.fill(matrices, x, y, x + width, y + height, hovered(mouseX, mouseY) ? new Color(20, 20, 20, 230).getRGB() : new Color(0, 0, 0, 230).getRGB());
		
		int nameColor = -1;
		
		if (!mod.isEnabled())
			nameColor = -1;
		else
			nameColor = new Color(150, 150, 150).getRGB();
		Screen.drawStringWithShadow(matrices, MinecraftClient.getInstance().textRenderer, mod.getName(), x + 4, y + 6, nameColor);
		Screen.drawStringWithShadow(matrices, MinecraftClient.getInstance().textRenderer, extended ? "-" : "+", x + width - 10, y + 6, nameColor);
		for (Component component : components) {
			if (this.extended) {
				component.render(matrices, mouseX, mouseY);
			}
		}
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (hovered(mouseX, mouseY)) {
			if (button == 0) {
				mod.toggle();
			} else if (button == 1) {
				this.extended = !this.extended;
				parent.updateButtons();
			}
		}
		
		for (Component component : components) {
			component.mouseClicked(mouseX, mouseY, button);
		}
	}
	
	public void mouseReleased(int button) {
		for (Component component : components) {
			component.mouseReleased(button);
		}
	}
	
	public boolean hovered(double mouseX, double mouseY) {
		return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public boolean isExtended() {
		return extended;
	}

	public void setExtended(boolean extended) {
		this.extended = extended;
	}
}
