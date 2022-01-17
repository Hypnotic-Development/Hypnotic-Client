/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.ui.clickgui;

import java.awt.Color;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.lwjgl.glfw.GLFW;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.config.SaveLoad;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.movement.FlightBlink;
import dev.hypnotic.module.render.ClickGUIModule;
import dev.hypnotic.ui.HypnoticScreen;
import dev.hypnotic.ui.clickgui.frame.Frame;
import dev.hypnotic.ui.clickgui2.MenuBar;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class ClickGUI extends HypnoticScreen {
	
	public static ClickGUI INSTANCE = new ClickGUI();
	private Frame frame;
	public Category currentCategory = Category.COMBAT;
	public CategoryButton currentButton;
	private ArrayList<CategoryButton> categories;
	public ArrayList<ModuleButton> buttons;
	private ArrayList<String> buttonNames;
	double offset = 0;
    double lastOffset = 0;
    
    public boolean dragging;
	
	int fadeIn = 0;
	double anim1, anim2, aStartX, aStartY;
	
	public int x, y, pWidth, pHeight, dragX, dragY;
	
	public ClickGUI() {
		frame = new Frame(this.width, this.height);
		categories = new ArrayList<CategoryButton>();
		buttons = new ArrayList<ModuleButton>();
		buttonNames = new ArrayList<String>();
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
		Collections.sort(ModuleManager.INSTANCE.getModulesInCategory(currentCategory), Comparator.comparing(Mod::getName));
		for (Mod mod : ModuleManager.INSTANCE.getModulesInCategory(currentCategory)) {
			if (!(mod instanceof FlightBlink)) {
				buttonNames.add(mod.getName());
			}
		}
		buttonNames.sort(Collator.getInstance());
		for (String name : buttonNames) {
			ModuleButton mb = new ModuleButton(ModuleManager.INSTANCE.getModuleByName(name), currentCategory, x + 120, y + modCount);
			buttons.add(mb);
			mb.startY = modCount;
			modCount+=30;
		}
	}
	
	@Override
	protected void init() {
		fadeIn = 0;
		anim1 = 0;
		anim2 = 0;
		aStartX = x;
		aStartY = y;
		super.init();
	}
	
	double animTicks = 0;
	int mouseAnim;
	double mouseAnim2;
	boolean shouldDraw;
	int animStartX, animStartY;
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if (!ModuleManager.INSTANCE.getModule(ClickGUIModule.class).clickAnimation.isEnabled()) shouldDraw = false;
		this.renderBackground(matrices);
		if (dragging) {
			x = (int) (mouseX - dragX);
			y = (int) (mouseY - dragY);
		}
		double dist1 = RenderUtils.distanceTo(anim1, pWidth / 2);
		double dist2 = RenderUtils.distanceTo(anim2, pHeight / 2);
		if (dist1 != 0) {
			anim1+=dist1 / 8;
		}
		if (dist2 != 0) {
			anim2+=dist2 / 8;
		}
		MenuBar.INSTANCE.renderMenuBar(matrices, mouseX, mouseY, width, height);
		
		// Sort the buttons alphabetically
		buttonNames.sort(Collator.getInstance());
//		Collections.sort(Comparator.comparingInt(buttonNames::indexOf));
		
		Collections.sort(buttons, Comparator.comparing(ModuleButton::getName));
		Collections.sort(ModuleManager.INSTANCE.getModulesInCategory(currentCategory), Comparator.comparing(Mod::getName));
		
		for (ModuleButton button : buttons) {
			if (button.open) {
				button.settingsWindow.render(matrices, mouseX, mouseY, delta);
				if (shouldDraw) {
					if (mouseAnim2 < 25) mouseAnim2++;
					RenderUtils.drawOutlineCircle(matrices, animStartX - (mouseAnim2 / 2), animStartY - (mouseAnim2 / 2), (int)mouseAnim2, new Color(255, 255, 255, mouseAnim));
					if (mouseAnim > 0) mouseAnim-=10;
					if (mouseAnim2 >= 25) {
						if (mouseAnim <= 0) {
							mouseAnim2 = 0;
							mouseAnim = 255;
							shouldDraw = false;
						}
					}
				}
				return;
			}
		}
		RenderUtils.fill(matrices, x + (pWidth / 2), y + (pHeight / 2), x + (pWidth / 2) + anim1, y + (pHeight / 2) + anim2, new Color(65, 65, 65).getRGB());
		RenderUtils.fill(matrices, x + (pWidth / 2), y + (pHeight / 2), x + (pWidth / 2) - anim1, y + (pHeight / 2) - anim2, new Color(65, 65, 65).getRGB());
		RenderUtils.fill(matrices, x + (pWidth / 2), y + (pHeight / 2), x + (pWidth / 2) + anim1, y + (pHeight / 2) - anim2, new Color(65, 65, 65).getRGB());
		RenderUtils.fill(matrices, x + (pWidth / 2), y + (pHeight / 2), x + (pWidth / 2) - anim1, y + (pHeight / 2) + anim2, new Color(65, 65, 65).getRGB());
		if (dist1 > 0.7) return;
		double dist = RenderUtils.distanceTo(lastOffset, offset);
	    if (dist != 0) lastOffset += dist / 10;
		RenderUtils.startScissor(x, y, pWidth, pHeight);
		for (CategoryButton c : categories) {
			if (c.category == currentCategory) currentButton = c;
			c.render(matrices, mouseX, mouseY);
		}
		
		this.renderPanel(matrices);
		int color = new Color(45, 45, 45, fadeIn).getRGB();
		
		RenderUtils.sideGradientFill(matrices, x + 110, y, x + 100, y + height, color, 0, false);
		fontBig.drawWithShadow(matrices, Hypnotic.fullName, x + font.getStringWidth(Hypnotic.fullName) / 6, y + 20, -1);
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
		
		if (shouldDraw) {
			if (mouseAnim2 < 25) mouseAnim2++;
			RenderUtils.drawOutlineCircle(matrices, animStartX - (mouseAnim2 / 2), animStartY - (mouseAnim2 / 2), (int)mouseAnim2, new Color(255, 255, 255, mouseAnim));
			if (mouseAnim > 0) mouseAnim-=10;
			if (mouseAnim2 >= 25) {
				if (mouseAnim <= 0) {
					mouseAnim2 = 0;
					mouseAnim = 255;
					shouldDraw = false;
				}
			}
		}
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	public void renderPanel(MatrixStack matrices) {
		if (fadeIn < 255)
			fadeIn+=5;
		RenderUtils.fill(matrices, x, y, x + pWidth, y + pHeight, new Color(50, 50, 50, fadeIn).getRGB());
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		shouldDraw = false;
		shouldDraw = true;
		animStartX = (int) mouseX;
		animStartY = (int) mouseY;
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
					buttonNames.clear();
					Collections.sort(ModuleManager.INSTANCE.getModulesInCategory(currentCategory), Comparator.comparing(Mod::getName));
					
					// Sort buttons alphabetically
					
					for (Mod mod : ModuleManager.INSTANCE.getModulesInCategory(currentCategory)) {
						if (!(mod instanceof FlightBlink)) {
							buttonNames.add(mod.getName());
							
						}
					}
					buttonNames.sort(Collator.getInstance());
					Collections.sort(buttons, Comparator.comparing(ModuleButton::getName));
					int count = 0;
					for (String name : buttonNames) {
						ModuleButton mb = new ModuleButton(ModuleManager.INSTANCE.getModuleByName(name), currentCategory, x + 120, y + count);
						buttons.add(mb);
						mb.startY = count;
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
					init();
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
