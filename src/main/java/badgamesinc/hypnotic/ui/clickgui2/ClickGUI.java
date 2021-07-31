package badgamesinc.hypnotic.ui.clickgui2;

import java.util.ArrayList;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.ui.clickgui2.frame.Frame;
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
			frames.add(new Frame(25 + offset, 20, 125, 20, category));
			offset+=130;
		}
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		for (Frame frame : frames) {
			frame.render(matrices, mouseX, mouseY);
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
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
	}
}
