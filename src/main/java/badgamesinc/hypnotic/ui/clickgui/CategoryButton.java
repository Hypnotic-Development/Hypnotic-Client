package badgamesinc.hypnotic.ui.clickgui;

import java.awt.Color;

import badgamesinc.hypnotic.module.Category;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class CategoryButton {

	int x, y, width, height, fadeIn;
	Category category;
	ClickGUI parent;
	
	@SuppressWarnings("resource")
	public CategoryButton(int x, int y, Category category, ClickGUI parent) {
        this.x = x;
        this.y = y;
        this.category = category;
        this.parent = parent;
        this.width = 80;
        this.height = 21 + MinecraftClient.getInstance().textRenderer.fontHeight;
        this.fadeIn = 0;
    }
	
	public void init() {
		fadeIn = 0;
	}
	
	@SuppressWarnings("resource")
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		if (fadeIn < 255)
			fadeIn+=5;
		if (hovered(mouseX, mouseY))
			Screen.fill(matrices, x, y, x + width, y + height, new Color(150, 150, 150, fadeIn).getRGB());
		if (this.category == parent.currentCategory)
			Screen.fill(matrices, x, y, x + width, y + height, new Color(100, 100, 100, fadeIn).getRGB());
		MinecraftClient.getInstance().textRenderer.draw(matrices, category.name, x + 20, y + 11, new Color(0, 0, 0, fadeIn).getRGB());
		
	}
	
	public boolean hovered(double mouseX, double mouseY) {
		return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (hovered(mouseX, mouseY) && button == 0)
			parent.currentCategory = category;
	}
}
