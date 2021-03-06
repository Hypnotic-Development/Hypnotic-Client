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
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.ui.HypnoticScreen;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class ComboBox extends Component {

	private ModeSetting modeSet = (ModeSetting)setting;
	public boolean expanded;
	
	public ComboBox(int x, int y, SettingsWindow parent, Setting setting) {
		super(x, y, parent, setting);
		this.modeSet = (ModeSetting)setting;
	}
	
	public double anim;
	float anim2;
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		HypnoticScreen.fontMed.drawWithShadow(matrices, modeSet.name, x - 10, y, -1);
		double distance = RenderUtils.distanceTo(anim, 2 + modeSet.getModes().size() * 15);
		double distance2 = RenderUtils.distanceTo(anim, 0);
		if (distance != 0 && expanded) {
			anim+=distance / 6;
		} else if (distance2 != 0 && !expanded) {
			anim+=distance2 / 6;
		}
		RenderUtils.drawRoundedRect(matrices, x + 120, y + 4, x + 174, (float) (y + 4 + anim), 6, new Color(35, 35, 35));
		HypnoticScreen.font.drawWithShadow(matrices, modeSet.getSelected(), x + 118, y - 1, -1);
//		matrices.push();
//		matrices.translate(x + 176, y + 1, 0);
//		if (expanded && anim2 < 90) anim2+=5;
//		else if (!expanded && anim2 > 0) anim2-=5;
//		matrices.multiply(new Quaternion(0, 0, 90, true));
//		matrices.multiply(new Quaternion(0, 0, anim2, true));
//		HypnoticScreen.font.drawWithShadow(matrices, "^", 0, 2 - anim2 / 10, -1);
//		matrices.pop();
		HypnoticScreen.font.drawWithShadow(matrices, expanded ? "-" : "+", x + 170, y - 2, -1);
		int count = 13;
//		RenderUtils.startScissor(x + 118, y + 13, x + 170, (int)anim);
		if (expanded) {
			for (String name : modeSet.getModes()) {
			 	HypnoticScreen.font.drawWithShadow(matrices, name, x + 118, y + count, -1);
				count+=15;
			}
		}
//		RenderUtils.endScissor();
		super.render(matrices, mouseX, mouseY);
	}
	
	@Override
	public boolean hovered(int mouseX, int mouseY) {
		return mouseX >= x + 115 && mouseX <= x + 180 && mouseY >= y - 2 && mouseY <= y + 8;
	}
	
	public boolean hovered(int mouseX, int mouseY, float x1, float y1, float x2, float y2) {
		return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (hovered((int)mouseX, (int)mouseY)) expanded = !expanded; 
		if (expanded) {
			int count = 13;
			for (String name : modeSet.getModes()) {
				
				if (hovered((int)mouseX, (int)mouseY, x + 118, y + count, x + 178, y + count + 9) && !modeSet.getSelected().equalsIgnoreCase(name)) {
					modeSet.setIndex(modeSet.getModes().indexOf(name));
				}
				count+=15;
			}
		}
		super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		super.mouseReleased(mouseX, mouseY, button);
	}
}
