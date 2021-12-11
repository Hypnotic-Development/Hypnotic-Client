package dev.hypnotic.hypnotic_client.ui.clickgui.settings;

import dev.hypnotic.hypnotic_client.settings.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class Component {

	MinecraftClient mc = MinecraftClient.getInstance();
	protected TextRenderer tr = mc.textRenderer;
	
	int x, y;
	public Setting setting;
	public SettingsWindow parent;
	
	public Component(int x, int y, SettingsWindow parent, Setting setting) {
		this.x = x;
		this.y = y;
		this.parent = parent;
		this.setting = setting;
	}
	
	
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
	}
	
	public void mouseReleased(double mouseX, double mouseY, int button) {
		
	}
	
	public boolean hovered(int mouseX, int mouseY) {
		return false;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
