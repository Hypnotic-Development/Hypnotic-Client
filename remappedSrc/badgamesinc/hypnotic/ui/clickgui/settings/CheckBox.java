package badgamesinc.hypnotic.ui.clickgui.settings;

import badgamesinc.hypnotic.settings.Setting;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class CheckBox extends Component {

	@SuppressWarnings("unused")
	private BooleanSetting boolSet = (BooleanSetting)setting;
	
	public CheckBox(int x, int y, SettingsWindow parent, Setting setting) {
		super(x, y, parent, setting);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		Screen.fill(matrices, x, y, x + 10, y + 10, -1);
		super.render(matrices, mouseX, mouseY);
	}
	
	@Override
	public boolean hovered(int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		return super.hovered(mouseX, mouseY);
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		// TODO Auto-generated method stub
		super.mouseClicked(mouseX, mouseY, button);
	}
}
