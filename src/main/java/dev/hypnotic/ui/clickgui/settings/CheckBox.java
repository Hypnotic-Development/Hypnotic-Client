/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.ui.clickgui.settings;

import java.awt.Color;

import dev.hypnotic.settings.Setting;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.ui.HypnoticScreen;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class CheckBox extends Component {

	private BooleanSetting boolSet = (BooleanSetting)setting;
	
	public CheckBox(int x, int y, SettingsWindow parent, Setting setting) {
		super(x, y, parent, setting);
		this.boolSet = (BooleanSetting)setting;
	}
	
	double anim, anim2;
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		HypnoticScreen.fontMed.drawWithShadow(matrices, boolSet.name, x - 10, y, -1);
		double dist1 = RenderUtils.distanceTo(anim, 5);
		double dist2 = RenderUtils.distanceTo(anim, 0);
		if (dist1 != 0 && boolSet.isEnabled()) anim+=dist1 / 6; 
		if (dist2 != 0 && !boolSet.isEnabled()) anim+=dist2 / 6; 
		RenderUtils.drawRoundedRect(matrices, x + 172, y + 4, x + 173, y + 5, 5, new Color(40, 40, 40));
//		RenderUtils.drawRoundedRect(matrices, x + 171 - 5 - (float)anim, y - 2 - (float)anim, x + 172 + (float)anim, y + 4 + (float)anim, 5, ColorUtils.defaultClientColor());
		if (dist1 <= 4.9) RenderUtils.drawRoundedRect(matrices, x + 172, y + 4, x + 173, y + 5, (float)anim, ColorUtils.defaultClientColor());
		if (boolSet.isEnabled()) FontManager.iconsSmall.draw(matrices, "f", x + 167.8f, y - 4, -1);
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
