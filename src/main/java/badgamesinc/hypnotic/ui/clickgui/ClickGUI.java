package badgamesinc.hypnotic.ui.clickgui;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.ui.HypnoticScreen;
import badgamesinc.hypnotic.ui.clickgui.frame.Frame;
import badgamesinc.hypnotic.ui.clickgui2.MenuBar;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class ClickGUI extends HypnoticScreen {
	
	public static ClickGUI INSTANCE = new ClickGUI();
	private Frame frame;
	public Category currentCategory = Category.COMBAT;
	public CategoryButton currentButton;
	private ArrayList<CategoryButton> categories;
	private ArrayList<ModuleButton> buttons;
	double offset = 0;
    double lastOffset = 0;
	
	int fadeIn = 0;
	
	public ClickGUI() {
		frame = new Frame(this.width, this.height);
		categories = new ArrayList<CategoryButton>();
		buttons = new ArrayList<ModuleButton>();
		int modCount = 0;
		

		int count = 50;
		for (Category category : Category.values()) {
			categories.add(new CategoryButton(200, 109 + count, category, this));
			count+=30;
		}
		
		for (Mod mod : ModuleManager.INSTANCE.getModulesInCategory(currentCategory)) {
			buttons.add(new ModuleButton(mod, currentCategory, 325, 120 + modCount));
			modCount+=30;
		}
	}
	
	@Override
	protected void init() {
		fadeIn = 0;
		super.init();
	}
	
	double animTicks = 0;
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);

		double dist = RenderUtils.distanceTo(lastOffset, offset);
	    if (dist != 0) lastOffset += dist / 10;
		for (ModuleButton button : buttons) {
			if (button.settingsWindow != null) {
				button.settingsWindow.render(matrices, mouseX, mouseY, delta);
				return;
			}
		}
		
		RenderUtils.startScissor(200, 100, (this.width - 200) - 200, (this.height - 100) - 100);
		for (CategoryButton c : categories) {
			if (c.category == currentCategory) currentButton = c;
			c.render(matrices, mouseX, mouseY);
		}
		
		this.renderPanel(matrices);
		int color = new Color(45, 45, 45, fadeIn).getRGB();
		
		RenderUtils.fill(matrices, 200, this.height - 100, 300, 100, color);
		RenderUtils.sideGradientFill(matrices, 310, this.height - 100, 300, 100, color, 0, false);
		FontManager.robotoBig.drawWithShadow(matrices, Hypnotic.fullName, 180 + font.getStringWidth(Hypnotic.fullName) / 2, 120, -1);
		if (currentButton != null) {
			double distance = RenderUtils.distanceTo(animTicks, currentButton.y);
			if (distance != 0) {
				animTicks+= distance / 10;
			}
			RenderUtils.fill(matrices, currentButton.x, animTicks, currentButton.x + currentButton.width, animTicks + currentButton.height, ColorUtils.defaultClientColor);
		}
		for (CategoryButton catButton : categories) {
			catButton.render(matrices, mouseX, mouseY);
		}
		
		for (ModuleButton button : buttons) {
			button.y = (int) (button.startY - this.lastOffset);
			button.render(matrices, mouseX, mouseY);
		}
		RenderUtils.endScissor();
		MenuBar.INSTANCE.renderMenuBar(matrices, mouseX, mouseY, width, height);
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	public void renderPanel(MatrixStack matrices) {
		if (fadeIn < 255)
			fadeIn+=5;
		RenderUtils.fill(matrices, 200, 100, this.width - 200, this.height - 100, new Color(50, 50, 50, fadeIn).getRGB());
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		MenuBar.INSTANCE.mouseClicked((int)mouseX, (int)mouseY, button);
		if (mouseX >= 200 && mouseX <= this.width - 200 && mouseY >= 100 && mouseY <= this.height - 100) {
			frame.mouseClicked(button, button, button);
			for (CategoryButton catButton : categories) {
				catButton.mouseClicked(mouseX, mouseY, button);
				if (catButton.hovered(mouseX, mouseY)) {
					buttons.clear();
					int count = 0;
					for (Mod mod : ModuleManager.INSTANCE.getModulesInCategory(currentCategory)) {
						buttons.add(new ModuleButton(mod, currentCategory, 325, 120 + count));
						count+=30;
					}
					this.offset = 0;
				}
			}
			for (ModuleButton modButton : buttons) {
				modButton.mouseClicked((int) mouseX, (int) mouseY, button);
			}
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

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		if (amount < 0) {
            this.offset += 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        } else if (amount > 0) {
            this.offset -= 26;
            if (this.offset < 0) {
                this.offset = 0;
            }
        }
		return super.mouseScrolled(mouseX, mouseY, amount);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (isButtonOpen() && keyCode == GLFW.GLFW_KEY_ESCAPE) {
			System.out.println("e");
			for (ModuleButton button : buttons) {
				if (button.settingsWindow != null) {
					button.settingsWindow = null;
					return false;
				}
			}
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return !isButtonOpen();
	}
	
	public boolean isButtonOpen() {
		for (ModuleButton button : buttons) {
			if (button.settingsWindow != null) {
				return true;
			}
		}
		return false;
	}
}
