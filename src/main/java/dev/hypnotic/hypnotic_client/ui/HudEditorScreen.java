package dev.hypnotic.hypnotic_client.ui;

import dev.hypnotic.hypnotic_client.module.hud.HudManager;
import dev.hypnotic.hypnotic_client.module.hud.HudModule;
import dev.hypnotic.hypnotic_client.ui.clickgui2.MenuBar;
import dev.hypnotic.hypnotic_client.ui.clickgui2.frame.Frame;
import net.minecraft.client.util.math.MatrixStack;

public class HudEditorScreen extends HypnoticScreen {

	public static HudEditorScreen INSTANCE = new HudEditorScreen();
	private MenuBar menuBar;
	public Frame frame;
	
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
			//If its off the screen put it back on the screen
			if (element.getX() > mc.getWindow().getScaledWidth() - 1 || element.getX() < 1) {
				element.setX(100);
			}
			if (element.getY() > mc.getWindow().getScaledHeight() - 1 || element.getY() < 1) {
				element.setY(100);
			}
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
		super.init();
	}
	
	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
