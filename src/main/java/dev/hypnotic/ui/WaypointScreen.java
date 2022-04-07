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

import java.awt.Color;

import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.utils.render.RenderUtils;
import dev.hypnotic.waypoint.Waypoint;
import dev.hypnotic.waypoint.WaypointManager;
import net.minecraft.client.util.math.MatrixStack;

@SuppressWarnings("unused")
public class WaypointScreen extends HypnoticScreen {

	private Waypoint waypoint;
	private TextBox nameBox, xBox, yBox, zBox;
	private Button doneButton, removeButton;
	private ColorPicker colorPicker;
	private int x = 300, y = 300, dragX, dragZ;
	private boolean dragging = false;
	
	public WaypointScreen(Waypoint waypoint) {
		this.waypoint = waypoint;
	}
	
	@Override
	protected void init() {
		xBox = new TextBox(width / 2 + 10, height / 2 - 100, 100, 10, waypoint.getX() + "");
		yBox = new TextBox(width / 2 + 10, height / 2 - 75, 100, 10, waypoint.getX() + "");
		zBox = new TextBox(width / 2 + 10, height / 2 - 50, 100, 10, waypoint.getX() + "");
		xBox.setText(mc.player.getX() + "");
		yBox.setText(mc.player.getY() + "");
		zBox.setText(mc.player.getZ() + "");
		nameBox = new TextBox(width / 2 - 110, height / 2 - 140, 220, 10, "Name");
		colorPicker = new ColorPicker("Color", width / 2 - 115, height / 2 - 120, 120, 20, Color.WHITE);
		doneButton = new Button("Done", width / 2 + 10, height / 2 - 20, 100, 20, false, this::addWaypoint);
		if (Double.parseDouble(xBox.getText()) != 0) xBox.setText(waypoint.getX() + "");
		if (Double.parseDouble(yBox.getText()) != 0) yBox.setText(waypoint.getY() + "");
		if (Double.parseDouble(zBox.getText()) != 0) zBox.setText(waypoint.getZ() + "");
		nameBox.setText(waypoint.getName());
		this.buttons.add(doneButton);
		super.init();
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		RenderUtils.drawRoundedRect(matrices, width / 2 - 120, height / 2 - 150, width / 2 + 120, height / 2 + 150, 10, new Color(50, 50, 50));
		xBox.render(matrices, mouseX, mouseY, delta);
		if (xBox.getText().isEmpty()) font.drawWithShadow(matrices, "X", width / 2 + 13, height / 2 - 100, Color.gray.getRGB());
		yBox.render(matrices, mouseX, mouseY, delta);
		if (yBox.getText().isEmpty()) font.drawWithShadow(matrices, "Y",width / 2 + 13, height / 2 - 75, Color.gray.getRGB());
		zBox.render(matrices, mouseX, mouseY, delta);
		if (zBox.getText().isEmpty()) font.drawWithShadow(matrices, "Z", width / 2 + 13, height / 2 - 50, Color.gray.getRGB());
		nameBox.render(matrices, mouseX, mouseY, delta);
		if (nameBox.getText().isEmpty()) font.drawWithShadow(matrices, "Name", width / 2 - 107, height / 2 - 140, Color.gray.getRGB());
		colorPicker.render(matrices, mouseX, mouseY);
		doneButton.render(matrices, mouseX, mouseY, delta);
		for (Waypoint wp : WaypointManager.INSTANCE.waypoints) {
		}
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		colorPicker.mouseClicked(mouseX, mouseY, button);
		xBox.mouseClicked(mouseX, mouseY, button);
		yBox.mouseClicked(mouseX, mouseY, button);
		zBox.mouseClicked(mouseX, mouseY, button);
		nameBox.mouseClicked(mouseX, mouseY, button);
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	private void addWaypoint() {
		if (!nameBox.getText().isEmpty() && !xBox.getText().isEmpty() && !yBox.getText().isEmpty() && !zBox.getText().isEmpty()) {
			try {
				waypoint = new Waypoint(nameBox.getText(), Double.parseDouble(xBox.getText()), Double.parseDouble(yBox.getText()), Double.parseDouble(zBox.getText()), colorPicker.getColor());
				WaypointManager.INSTANCE.waypoints.add(waypoint);
				mc.setScreen(WaypointManagerScreen.INSTANCE);
			} catch(NumberFormatException e) {
				System.out.println("Invalid x y or z!");
			}
		}
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		colorPicker.mouseReleased(button);
		dragging = false;
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers) {
		xBox.charTyped(chr, modifiers);
		yBox.charTyped(chr, modifiers);
		zBox.charTyped(chr, modifiers);
		nameBox.charTyped(chr, modifiers);
		return super.charTyped(chr, modifiers);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		xBox.keyPressed(keyCode, scanCode, modifiers);
		yBox.keyPressed(keyCode, scanCode, modifiers);
		zBox.keyPressed(keyCode, scanCode, modifiers);
		nameBox.keyPressed(keyCode, scanCode, modifiers);
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		xBox.keyReleased(keyCode, scanCode, modifiers);
		yBox.keyReleased(keyCode, scanCode, modifiers);
		zBox.keyReleased(keyCode, scanCode, modifiers);
		nameBox.keyReleased(keyCode, scanCode, modifiers);
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
}
