package badgamesinc.hypnotic.ui;

import static badgamesinc.hypnotic.utils.MCUtils.mc;

import java.util.ArrayList;

import badgamesinc.hypnotic.utils.font.FontManager;
import badgamesinc.hypnotic.utils.font.NahrFont;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;

public abstract class HypnoticScreen extends Screen {

	public double mouseX, mouseY;
	public NahrFont font = FontManager.roboto;
	protected ArrayList<Button> buttons = new ArrayList<>();
	
	public HypnoticScreen() {
		super(new LiteralText("Hypnotic-Screen"));
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	public boolean hovered(int x1, int y1, int x2, int y2) {
		return mouseX >= x1 && mouseX <= x2 && mouseY <= y1 && mouseY >= y2;
	}

	public void addButton(Button button) {
		if (!buttons.contains(button)) {
			buttons.add(button);
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (Button b : buttons) {
			if (b.isHovered(mouseX, mouseY)) {
				buttonClicked(b);
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	public void buttonClicked(Button button) {
		mc.getSoundManager().play(PositionedSoundInstance.ambient(SoundEvents.UI_BUTTON_CLICK, 1, 1));
	}
	
	@Override
	public void onClose() {
		this.buttons.clear();
		super.onClose();
	}
}