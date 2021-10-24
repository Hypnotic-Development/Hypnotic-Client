package badgamesinc.hypnotic.ui.clickgui.settings;

import java.awt.Color;

import badgamesinc.hypnotic.settings.Setting;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class CheckBox extends Component {

	private BooleanSetting boolSet = (BooleanSetting)setting;
	
	public CheckBox(int x, int y, SettingsWindow parent, Setting setting) {
		super(x, y, parent, setting);
		this.boolSet = (BooleanSetting)setting;
	}
	
	int anim = 0;
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		FontManager.robotoMed.drawWithShadow(matrices, boolSet.name, x - 10, y, -1);
		RenderUtils.drawOutlineCircle(matrices, x + 170, y, 10, new Color(170, 170, 170));
		if (anim < 255 && boolSet.isEnabled()) anim+=5; 
		if (anim > 0 && !boolSet.isEnabled()) anim-=5; 
		RenderUtils.drawFilledCircle(matrices, x + 171.8, y + 1.8, 6.5f, new Color(ColorUtils.defaultClientColor().getRed(), ColorUtils.defaultClientColor().getGreen(), ColorUtils.defaultClientColor().getBlue(), anim));
		
		super.render(matrices, mouseX, mouseY);
	}
	
	@Override
	public boolean hovered(int mouseX, int mouseY) {
		if (mouseX >= x - 10 && mouseX <= x + 180 && mouseY >= y && mouseY <= y + 8) return true;
		return super.hovered(mouseX, mouseY);
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (hovered((int)mouseX, (int)mouseY) && button == 0) boolSet.toggle();
		super.mouseClicked(mouseX, mouseY, button);
	}
}
