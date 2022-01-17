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

import dev.hypnotic.ui.clickgui2.MenuBar;
import dev.hypnotic.ui.clickgui2.frame.Frame;
import dev.hypnotic.waypoint.Waypoint;
import dev.hypnotic.waypoint.WaypointManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

public class WaypointManagerScreen extends HypnoticScreen {

	public static WaypointManagerScreen INSTANCE = new WaypointManagerScreen();

	private MenuBar menuBar;
	public Frame frame;
	private Button addButton = new Button("Add waypoint", 4321, 4, height - 25, 100, 20, false);
	
	public WaypointManagerScreen() {
		this.frame = new Frame("Waypoints", 100, 100, 200, 20);
	}
	
	@Override
	protected void init() {
		menuBar = MenuBar.INSTANCE;
		this.addButton(addButton);
		int offset = 0;
		frame.buttons.clear();
		for (Waypoint waypoint : WaypointManager.INSTANCE.waypoints) {
			frame.buttons.add(new dev.hypnotic.ui.clickgui2.frame.button.Button(waypoint, 20, 20, offset, frame));
			offset+=20;
		}
		super.init();
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		frame.render(matrices, mouseX, mouseY);
		frame.updateButtons();
		frame.updatePosition(mouseX, mouseY);
		frame.setHeight(20);
		frame.setWidth(200);
		menuBar.renderMenuBar(matrices, mouseX, mouseY, width, height);
		addButton.setY(20);
		addButton.setX(width - 120);
		addButton.render(matrices, mouseX, mouseY, delta);
		
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		frame.mouseClicked(mouseX, mouseY, button);
		menuBar.mouseClicked((int)mouseX, (int)mouseY, button);
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		frame.mouseReleased(button);
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public void buttonClicked(Button button) {
		if (button.getId() == 4321) {
			Waypoint waypoint = new Waypoint("New Waypoint", BlockPos.ORIGIN);
			WaypointManager.INSTANCE.waypoints.add(waypoint);
			mc.setScreen(new WaypointScreen(waypoint));
		}
		super.buttonClicked(button);
	}
	
	@Override
	public void onClose() {
		frame.buttons.clear();
		super.onClose();
	}
}
