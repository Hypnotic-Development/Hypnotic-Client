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
package dev.hypnotic.ui;

import dev.hypnotic.module.hud.HudManager;
import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.ui.clickgui2.MenuBar;
import dev.hypnotic.ui.clickgui2.frame.Frame;
import net.minecraft.client.util.math.MatrixStack;

public class HudEditorScreen extends HypnoticScreen {

	public static HudEditorScreen INSTANCE = new HudEditorScreen();
	private MenuBar menuBar;
	public Frame frame;
	private Button hideButton;
	private boolean hide = false;
	
	public HudEditorScreen() {
		frame = new Frame(200, 25, 120, 14, "Hud Modules");
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		frame.render(matrices, mouseX, mouseY);
		frame.setWidth(120);
		frame.updatePosition(mouseX, mouseY);
		frame.updateButtons();
		for (HudModule element : HudManager.INSTANCE.hudModules) {
			element.render(matrices, mc.getWindow().getScaledWidth(), mc.getWindow().getScaledHeight(), delta);
			element.updatePosition(mouseX, mouseY);
		}
		menuBar.renderMenuBar(matrices, mouseX, mouseY, this.width, this.height);
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		frame.mouseClicked(mouseX, mouseY, button);
		for (HudModule element : HudManager.INSTANCE.hudModules) {
			if (element.hovered((int)mouseX, (int)mouseY)) {
				if (button == 0) {
					element.setDragging(true);
					element.setDragX((int) (mouseX - element.getX()));
					element.setDragY((int) (mouseY - element.getY()));
				} else if (button == 2) {
					element.toggle();
				}
			}
			if (element.hovered((int)mouseX, (int)mouseY, element.getX() + element.getWidth(), element.getY() + element.getHeight(), element.getX() + element.getWidth() + 20, element.getY() +  element.getHeight() + 20) && button == 0) {
				// broken
				//				element.setScaling(true, (int)mouseX, (int)mouseY);
			}
		}
		menuBar.mouseClicked((int)mouseX, (int)mouseY, button);
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		frame.mouseReleased(button);
		for (HudModule element : HudManager.INSTANCE.hudModules) {
			if (button == 0) {
				element.setDragging(false);
				element.setScaling(false);
			}
		}
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	protected void init() {
		menuBar = MenuBar.INSTANCE;
		hideButton = new Button("Hide disabled modules", width - 100, 5, 200, 25, true, () -> {
			hide = !hide;
		});
		this.addButton(hideButton);
		super.init();
	}
	
	@Override
	public boolean shouldPause() {
		return false;
	}
}
