package badgamesinc.hypnotic.ui;

import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import badgamesinc.hypnotic.waypoint.Waypoint;
import net.minecraft.client.util.math.MatrixStack;

public class WaypointScreen extends HypnoticScreen {

	private Waypoint waypoint;
	private TextBox nameBox, x, y, z;
	private Button doneButton, removeButton;
	private ColorPicker colorPicker;
	private ColorSetting color = new ColorSetting("Color", 255, 255, 255, true);
	
	public WaypointScreen(Waypoint waypoint) {
		this.waypoint = waypoint;
	}
	
	@Override
	protected void init() {
		x = new TextBox(width / 2, height / 2, 100, 10, waypoint.getX() + "");
		y = new TextBox(width / 2, height / 2 + 20, 100, 10, waypoint.getX() + "");
		z = new TextBox(width / 2, height / 2 - 20, 100, 10, waypoint.getX() + "");
		colorPicker = new ColorPicker(100, 100, 120, 20, color);
		super.init();
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		RenderUtils.drawRoundedRect(matrices, width / 2, height - 100, width / 2 + 100, 100, 10, ColorUtils.defaultClientColor());
		x.render(matrices, mouseX, mouseY, delta);
		y.render(matrices, mouseX, mouseY, delta);
		z.render(matrices, mouseX, mouseY, delta);
		colorPicker.render(matrices, mouseX, mouseY);
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		colorPicker.mouseClicked(mouseX, mouseY, button);
		x.mouseClicked(mouseX, mouseY, button);
		y.mouseClicked(mouseX, mouseY, button);
		z.mouseClicked(mouseX, mouseY, button);
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		colorPicker.mouseReleased(button);
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers) {
		x.charTyped(chr, modifiers);
		y.charTyped(chr, modifiers);
		z.charTyped(chr, modifiers);
		return super.charTyped(chr, modifiers);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		x.keyPressed(keyCode, scanCode, modifiers);
		y.keyPressed(keyCode, scanCode, modifiers);
		z.keyPressed(keyCode, scanCode, modifiers);
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		x.keyReleased(keyCode, scanCode, modifiers);
		y.keyReleased(keyCode, scanCode, modifiers);
		z.keyReleased(keyCode, scanCode, modifiers);
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
}
