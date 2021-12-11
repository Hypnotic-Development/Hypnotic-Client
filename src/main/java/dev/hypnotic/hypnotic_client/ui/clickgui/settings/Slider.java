package dev.hypnotic.hypnotic_client.ui.clickgui.settings;

import java.awt.Color;

import dev.hypnotic.hypnotic_client.settings.Setting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.NumberSetting;
import dev.hypnotic.hypnotic_client.ui.HypnoticScreen;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.font.FontManager;
import dev.hypnotic.hypnotic_client.utils.math.MathUtils;
import dev.hypnotic.hypnotic_client.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class Slider extends Component {

	private NumberSetting numSet = (NumberSetting)setting;
	private boolean sliding = false;
	
	public Slider(int x, int y, SettingsWindow parent, Setting setting) {
		super(x, y, parent, setting);
		this.numSet = (NumberSetting)setting;
	}
	
	double anim;
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		Color color = ColorUtils.defaultClientColor();
		double diff = Math.min(100, Math.max(0, mouseX - (x + 80)));

		double min = numSet.getMin();
		double max = numSet.getMax();
		
		double renderWidth = (100) * (numSet.getValue() - min) / (max - min);
		
		if (sliding) {
			if (diff == 0) {
				numSet.setValue(numSet.getMin());
			}
			else {
				double newValue = MathUtils.round(((diff / 100) * (max - min) + min), 2);
				numSet.setValue(newValue);
			}
		}
		double distance = RenderUtils.distanceTo(anim, renderWidth);
		if (distance != 0) {
			anim+=distance / 6;
		}

		RenderUtils.fill(matrices, x + 81, y + 5, x + 179, y + 7, color.darker().getRGB());
		RenderUtils.drawFilledCircle(matrices, x + 80, y + 5, 2, ColorUtils.defaultClientColor());
		RenderUtils.fill(matrices, x + 81, y + 5, (x + 80 + anim), y + 7, color.getRGB());
		RenderUtils.drawFilledCircle(matrices, x + 178, y + 5, 2, ColorUtils.defaultClientColor().darker());
		RenderUtils.drawFilledCircle(matrices, x + 80 + anim - 3, y + 3, 6, color);
		HypnoticScreen.fontMed.drawWithShadow(matrices, numSet.name, x - 10, y, -1);
		HypnoticScreen.fontSmall.drawWithShadow(matrices, numSet.getValue() + "", x + 120, y - 4, ColorUtils.transparent(-1, 180));
		super.render(matrices, mouseX, mouseY);
	}
	
	@Override
	public boolean hovered(int mouseX, int mouseY) {
		return mouseX >= x + 80 && mouseX <= x + 180 && mouseY >= y + 2 && mouseY <= y + 8;
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (hovered((int)mouseX, (int)mouseY) && button == 0) {
			this.sliding = true;
		}
		super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		sliding = false;
		super.mouseReleased(mouseY, mouseY, button);
	}
}
