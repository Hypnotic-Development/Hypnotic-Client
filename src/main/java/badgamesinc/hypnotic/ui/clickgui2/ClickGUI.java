package badgamesinc.hypnotic.ui.clickgui2;

import java.util.ArrayList;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.ui.clickgui2.frame.Frame;
import badgamesinc.hypnotic.ui.clickgui2.frame.button.Button;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class ClickGUI extends Screen {

	public static ClickGUI INSTANCE = new ClickGUI();
	private ArrayList<Frame> frames;
	
	
	private ClickGUI() {
		super(new LiteralText("ClickGUI2"));
		frames = new ArrayList<Frame>();
		
		int offset = 0;
		for (Category category : Category.values()) {
			frames.add(new Frame(25 + offset, 20, 96, 14, category));
			offset+=100;
		}
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		for (Frame frame : frames) {
			frame.render(matrices, mouseX, mouseY);
			frame.updatePosition(mouseX, mouseY);
			frame.updateButtons();
		}
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
		// TODO Auto-generated method stub
		super.init();
	}
}
