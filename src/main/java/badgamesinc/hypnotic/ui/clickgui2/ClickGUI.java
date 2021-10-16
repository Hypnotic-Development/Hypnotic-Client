package badgamesinc.hypnotic.ui.clickgui2;

import java.util.ArrayList;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.ui.HypnoticScreen;
import badgamesinc.hypnotic.ui.clickgui2.frame.Frame;
import badgamesinc.hypnotic.ui.clickgui2.frame.button.Button;
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
	public boolean isPauseScreen() {
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
	public void onClose() {
		mouseReleased(mc.mouse.getX() * mc.getWindow().getScaleFactor(), mc.mouse.getY() * mc.getWindow().getScaleFactor(), 0);
		super.onClose();
	}
}
