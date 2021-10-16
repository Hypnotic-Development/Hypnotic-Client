package badgamesinc.hypnotic.ui;

import badgamesinc.hypnotic.ui.clickgui2.MenuBar;
import net.minecraft.client.util.math.MatrixStack;

public class OptionsScreen extends HypnoticScreen {

	public static OptionsScreen INSTANCE = new OptionsScreen();
	private MenuBar menuBar;
	public Frame frame;
	
	public OptionsScreen() {
		this.frame = new Frame("Options", 100, 100, 100, 100);
	}
	
	@Override
	protected void init() {
		menuBar = MenuBar.INSTANCE;
		super.init();
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		menuBar.renderMenuBar(matrices, mouseX, mouseY, width, height);
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		menuBar.mouseClicked((int)mouseX, (int)mouseY, button);
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
