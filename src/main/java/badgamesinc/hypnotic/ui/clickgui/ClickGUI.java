package badgamesinc.hypnotic.ui.clickgui;

import java.awt.Color;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.config.SaveLoad;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.movement.FlightBlink;
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
	public ArrayList<ModuleButton> buttons;
	double offset = 0;
    double lastOffset = 0;
    
    public boolean dragging;
	
	int fadeIn = 0;
	
	public int x, y, pWidth, pHeight, dragX, dragY;
	
	public ClickGUI() {
		frame = new Frame(this.width, this.height);
		categories = new ArrayList<CategoryButton>();
		buttons = new ArrayList<ModuleButton>();
		int modCount = 0;
		this.x = 200;
		this.y = 100;
		this.pWidth = 560;
		this.pHeight = 290;

		int count = 50;
		for (Category category : Category.values()) {
			categories.add(new CategoryButton(200, 109 + count, category, this));
			count+=30;
		}
		
		for (Mod mod : ModuleManager.INSTANCE.getModulesInCategory(currentCategory)) {
			if (!(mod instanceof FlightBlink)) {
				ModuleButton mb = new ModuleButton(mod, currentCategory, x + 120, y + modCount);
				buttons.add(mb);
				mb.startY = modCount;
				modCount+=30;
			}
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
		if (dragging) {
			x = (int) (mouseX - dragX);
			y = (int) (mouseY - dragY);
		}
		MenuBar.INSTANCE.renderMenuBar(matrices, mouseX, mouseY, width, height);
		double dist = RenderUtils.distanceTo(lastOffset, offset);
	    if (dist != 0) lastOffset += dist / 10;
		for (ModuleButton button : buttons) {
			if (button.open) {
				button.settingsWindow.render(matrices, mouseX, mouseY, delta);
				return;
			}
		}
		
		RenderUtils.startScissor(x, y, pWidth, pHeight);
		for (CategoryButton c : categories) {
			if (c.category == currentCategory) currentButton = c;
			c.render(matrices, mouseX, mouseY);
		}
		
		this.renderPanel(matrices);
		int color = new Color(45, 45, 45, fadeIn).getRGB();
		
		RenderUtils.sideGradientFill(matrices, x + 110, y, x + 100, y + height, color, 0, false);
		FontManager.robotoBig.drawWithShadow(matrices, Hypnotic.fullName, x + font.getStringWidth(Hypnotic.fullName) / 6, y + 20, -1);
		if (currentButton != null) {
			if (!dragging) {
				double distance = RenderUtils.distanceTo(animTicks, currentButton.y);
				if (distance != 0) {
					animTicks+= distance / 10;
				}
				RenderUtils.fill(matrices, currentButton.x, animTicks, currentButton.x + currentButton.width, animTicks + currentButton.height, ColorUtils.defaultClientColor);
			} else {
				animTicks = currentButton.y;
			}
		}
		int count = 50;
		for (CategoryButton catButton : categories) {
			catButton.x = x;
			catButton.y = y + count;
			count+=30;
			catButton.render(matrices, mouseX, mouseY);
		}
		
		for (ModuleButton button : buttons) {
			button.y = (int) ((y + 20) + button.startY - this.lastOffset);
			button.x = x + 125;
			button.render(matrices, mouseX, mouseY);
		}
		RenderUtils.endScissor();
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	public void renderPanel(MatrixStack matrices) {
		if (fadeIn < 255)
			fadeIn+=5;
		RenderUtils.fill(matrices, x, y, x + pWidth, y + pHeight, new Color(50, 50, 50, fadeIn).getRGB());
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (isButtonOpen()) {
			for (ModuleButton modButton : buttons) {
				if (modButton.open) modButton.settingsWindow.mouseClicked(mouseX, mouseY, button);
			}
		} else {
			if (mouseX >= x && mouseX <= x + 100 && mouseY >= y && mouseY <= y + 50) {
				if (button == 0) {
					dragging = true;
					dragX = ((int) (mouseX - x));
					dragY = ((int) (mouseY - y));
				}
			}
		}
		MenuBar.INSTANCE.mouseClicked((int)mouseX, (int)mouseY, button);
		if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height && !isButtonOpen()) {
			frame.mouseClicked(button, button, button);
			for (CategoryButton catButton : categories) {
				catButton.mouseClicked(mouseX, mouseY, button);
				if (catButton.hovered(mouseX, mouseY)) {
					buttons.clear();
					int count = 0;
					for (Mod mod : ModuleManager.INSTANCE.getModulesInCategory(currentCategory)) {
						if (!(mod instanceof FlightBlink)) {
							ModuleButton mb = new ModuleButton(mod, currentCategory, x + 120, y + count);
							buttons.add(mb);
							mb.startY = count;
							count+=30;
						}
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
			button.open = false;;
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
		for (ModuleButton button : buttons) {
			if (button.open) button.settingsWindow.mouseScrolled(amount);
		}
		return super.mouseScrolled(mouseX, mouseY, amount);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (isButtonOpen() && keyCode == GLFW.GLFW_KEY_ESCAPE) {
			for (ModuleButton button : buttons) {
				if (button.open) {
					button.open = false;
					return false;
				}
			}
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (ModuleButton modButton : buttons) {
			modButton.mouseReleased((int)mouseX, (int)mouseY, button);
		}
		if (button == 0) {
			dragging = false;
			SaveLoad.INSTANCE.save();
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return !isButtonOpen();
	}
	
	public boolean isButtonOpen() {
		for (ModuleButton button : buttons) {
			if (button.open) {
				return true;
			}
		}
		return false;
	}
}
