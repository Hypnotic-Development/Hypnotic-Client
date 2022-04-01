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
package dev.hypnotic.ui.clickgui2;

import java.util.ArrayList;

import dev.hypnotic.module.Category;
import dev.hypnotic.ui.HypnoticScreen;
import dev.hypnotic.ui.clickgui2.frame.Frame;
import dev.hypnotic.ui.clickgui2.frame.button.Button;
import net.minecraft.client.util.math.MatrixStack;

public class ClickGUI extends HypnoticScreen {

	public static ClickGUI INSTANCE = new ClickGUI();
	public ArrayList<Frame> frames;
	private MenuBar menuBar;
	
	private ClickGUI() {
		frames = new ArrayList<Frame>();
		
		int offset = 0;
		for (Category category : Category.values()) {
			frames.add(new Frame(25 + offset, 20, 120, 14, category));
			offset+=126;
		}
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		
		for (Frame frame : frames) {
			frame.setWidth(120);
			frame.render(matrices, mouseX, mouseY);
			frame.updatePosition(mouseX, mouseY);
			frame.updateButtons();
		}
		
		menuBar.renderMenuBar(matrices, mouseX, mouseY, this.width, this.height);
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (Frame frame : frames) {
			frame.mouseClicked(mouseX, mouseY, button);
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		for (Frame frame : frames) {
			frame.mouseReleased(button);
		}
		menuBar.mouseClicked((int)mouseX, (int)mouseY, button);
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean shouldPause() {
		return false;
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		for (Frame frame : frames) {
			if (amount > 0) frame.setY((int) (frame.getY() + 5));
			else if (amount < 0) frame.setY((int) (frame.getY() - 5));
			for (Button button : frame.buttons) {
				if (amount > 0) button.setY((int) (button.getY() + 5));
				else if (amount < 0) button.setY((int) (button.getY() - 5));
			}
		}
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	protected void init() {
		menuBar = MenuBar.INSTANCE;
		super.init();
	}
	
	@Override
	public void close() {
		mouseReleased(mc.mouse.getX() * mc.getWindow().getScaleFactor(), mc.mouse.getY() * mc.getWindow().getScaleFactor(), 0);
		super.close();
	}
}
