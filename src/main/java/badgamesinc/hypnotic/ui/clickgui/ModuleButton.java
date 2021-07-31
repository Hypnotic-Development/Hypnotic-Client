package badgamesinc.hypnotic.ui.clickgui;

import java.awt.Color;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.ui.clickgui.settings.SettingsWindow;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class ModuleButton {

	public Mod mod;
	public int x, y, width, height, animation = 0;
	private MinecraftClient mc = MinecraftClient.getInstance();
	public static SettingsWindow settingsWindow = null;
	public static boolean open = false;
	
	public ModuleButton(Mod mod, Category cateogry, int x, int y) {
		this.mod = mod;
		this.x = x;
		this.y = y;
		this.width = 100;
		this.height = 10;
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		if (settingsWindow == null)
			open = false;
		else
			open = true;
		TextRenderer tr = mc.textRenderer;
		if (animation < 255)
			animation+=5;
		Screen.fill(matrices, x - 3, y - 3, x + width, y + height, hovered(mouseX, mouseY) ? new Color(100, 100, 100).getRGB() : new Color(150, 150, 150).getRGB());
		tr.draw(matrices, mod.getName(), x, y, new Color(0, 0, 0, animation).getRGB());
	}
	
	public boolean hovered(int mouseX, int mouseY) {
		return mouseX >= x - 3 && mouseX <= x + width && mouseY >= y - 3 && mouseY <= y + height;
	}
	
	public void mouseClicked(int mouseX, int mouseY, int button) {
		if (hovered(mouseX, mouseY)) {
			if (button == 0) {
				mod.toggle();
			} else if (button == 1) {
				settingsWindow = new SettingsWindow(this);
			}
		}
	}
	
	public void init() {
		animation = 0;
	}
}
