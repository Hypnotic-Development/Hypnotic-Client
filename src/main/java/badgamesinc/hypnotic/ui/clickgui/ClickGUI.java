package badgamesinc.hypnotic.ui.clickgui;

import java.awt.Color;
import java.util.ArrayList;

import com.mojang.blaze3d.platform.GlStateManager;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.ui.clickgui.frame.Frame;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class ClickGUI extends Screen {
	
	public static ClickGUI INSTANCE = new ClickGUI();
	private Frame frame;
	public Category currentCategory = Category.COMBAT;
	private ArrayList<CategoryButton> categories;
	private ArrayList<ModuleButton> buttons;
	
	int fadeIn = 0;
	
	public ClickGUI() {
		super(new LiteralText("ClickGUI"));
		frame = new Frame(this.width, this.height);
		categories = new ArrayList<CategoryButton>();
		buttons = new ArrayList<ModuleButton>();
		int modCount = 0;
		

		int count = 50;
		for (Category category : Category.values()) {
			categories.add(new CategoryButton(200, 109 + count, category, this));
			/*inBounds = mouseX >= 200 && mouseX <= 280 && mouseY >= 110 + count && mouseY <= 130 + count;
			
			if (inBounds) {
				fill(matrices, 200, 109 + count, 280, 130 + count + textRenderer.fontHeight, new Color(150, 150, 150).getRGB());
			}
			
			if (category == this.currentCategory) {
				fill(matrices, 200, 109 + count, 280, 130 + count + textRenderer.fontHeight, new Color(100, 100, 100, 200).getRGB());
			}
			
			switch(category) {
				case COMBAT:
					hoveredCombat = inBounds;
					break;
				case MOVEMENT:
					hoveredMovement = inBounds;
					break;
				case RENDER:
					hoveredRender = inBounds;
					break;
			}
			textRenderer.draw(matrices, category.name, 220, 120 + count, new Color(0, 0, 0, fadeIn).getRGB());*/
			
			count+=30;
		}
		
		for (Mod mod : ModuleManager.INSTANCE.getModulesInCategory(currentCategory)) {
			buttons.add(new ModuleButton(mod, currentCategory, 250, 120 + modCount));
			modCount+=20;
		}
	}
	
	@Override
	protected void init() {
		fadeIn = 0;
		super.init();
	}
	
	public boolean hoveredCombat, hoveredMovement, hoveredRender, inBounds;
	
	@SuppressWarnings("static-access")
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		
		for (ModuleButton button : buttons) {
			if (button.settingsWindow != null) {
				button.settingsWindow.render(matrices, mouseX, mouseY);
			}
		}
		
		if (ModuleButton.settingsWindow == null) {
			GlStateManager._enableScissorTest();
			GlStateManager._scissorBox(200, 100, this.width + 360, this.height + 209);
			this.renderPanel(matrices);
			
			fill(matrices, 200, this.height - 100, 280, 100, new Color(255, 255, 255, fadeIn).getRGB());
			
			textRenderer.draw(matrices, Hypnotic.fullName, 202, 120, 0);
			
			for (CategoryButton catButton : categories) {
				catButton.render(matrices, mouseX, mouseY);
			}
			
			for (ModuleButton button : buttons) {
				button.render(matrices, mouseX, mouseY);
			}
	
			GlStateManager._disableScissorTest();
		}
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	public void renderPanel(MatrixStack matrices) {
		if (fadeIn < 255)
			fadeIn+=5;
		Screen.fill(matrices, 200, 100, this.width - 200, this.height - 100, new Color(200, 200, 200, fadeIn).getRGB());
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		frame.mouseClicked(button, button, button);
		if (hoveredCombat && button == 0)
			this.currentCategory = Category.COMBAT;
		else if (hoveredMovement && button == 0)
			this.currentCategory = Category.MOVEMENT;
		else if (hoveredRender && button == 0)
			this.currentCategory = Category.RENDER;

		for (CategoryButton catButton : categories) {
			catButton.mouseClicked(mouseX, mouseY, button);
			if (catButton.hovered(mouseX, mouseY)) {
				buttons.clear();
				int count = 0;
				for (Mod mod : ModuleManager.INSTANCE.getModulesInCategory(currentCategory)) {
					buttons.add(new ModuleButton(mod, currentCategory, 300, 120 + count));
					count+=20;
				}
			}
		}
		
		for (ModuleButton modButton : buttons) {
			modButton.mouseClicked((int) mouseX, (int) mouseY, button);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
	
	@Override
	public void onClose() {
		for (ModuleButton button : buttons) {
			button.settingsWindow = null;
		}
		super.onClose();
	}

}
