package badgamesinc.hypnotic.ui.clickgui2.frame.button.settings;

import java.awt.Color;

import badgamesinc.hypnotic.settings.Setting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.ui.clickgui2.frame.button.Button;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class ComboBox extends Component {

	private ModeSetting modeSet = (ModeSetting)setting;
	private Button parent;
	
	public ComboBox(int x, int y, Setting setting, Button parent) {
		super(x, y, setting, parent);
		this.setting = setting;
		this.modeSet = (ModeSetting)setting;
		this.parent = parent;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		Screen.fill(matrices, parent.getX(), parent.getY() + parent.mod.settings.indexOf(modeSet) * parent.getHeight() + parent.getHeight(), parent.getX() + parent.getWidth(), parent.getY() + parent.mod.settings.indexOf(modeSet) * parent.getHeight() + parent.getHeight() * 2, new Color(30, 30, 30, 230).getRGB());
		Screen.drawStringWithShadow(matrices, tr, modeSet.name + ": " + modeSet.getSelected(), parent.getX() + 8, parent.getY() + parent.mod.settings.indexOf(modeSet) * parent.getHeight() + parent.getHeight() + 6, -1);
		super.render(matrices, mouseX, mouseY);
	}
	
	@Override
	public boolean hovered(int mouseX, int mouseY) {
		return mouseX >= parent.getX() && mouseX <= parent.getX() + parent.getWidth() && mouseY >= parent.getY() + parent.mod.settings.indexOf(modeSet) * parent.getHeight() + parent.getHeight() && mouseY <= parent.getY() + parent.mod.settings.indexOf(modeSet) * parent.getHeight() + parent.getHeight() * 2;
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (hovered((int)mouseX, (int)mouseY)) {
			if (button == 0) {
				modeSet.cycle();
			}
		}
			
		super.mouseClicked(mouseX, mouseY, button);
	}
}
