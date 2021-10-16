package badgamesinc.hypnotic.ui.clickgui.settings;

import badgamesinc.hypnotic.settings.Setting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import net.minecraft.client.util.math.MatrixStack;

public class Slider extends Component {

	private NumberSetting numSet = (NumberSetting)setting;
	private boolean sliding = false;
	
	public Slider(int x, int y, SettingsWindow parent, Setting setting) {
		super(x, y, parent, setting);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		// TODO Auto-generated method stub
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
	
	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		sliding = false;
		super.mouseReleased(mouseY, mouseY, button);
	}
}
