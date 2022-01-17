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
package dev.hypnotic.module.hud.elements;

import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.math.MathUtils;
import dev.hypnotic.utils.math.TPSUtils;
import net.minecraft.client.util.math.MatrixStack;

public class TPS extends HudModule {

	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	
	public TPS() {
		super("Tps Display", "Renders the servers esimated tps", 5, 1050, (int)FontManager.robotoMed.getStringWidth("TPS 20"), (int)FontManager.robotoMed.getStringHeight("TPS 20"));
		addSetting(color);
	}
	
	@Override
	public void onEnable() {
		this.setDefaultX(5);
		this.setDefaultY(mc.getWindow().getScaledHeight() - 30);
		if (this.getX() > mc.getWindow().getScaledWidth()) this.setX(this.getDefaultX());
		if (this.getY() > mc.getWindow().getScaledHeight()) this.setY(this.getDefaultY());
		super.onEnable();
	}

	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		String tpsString = "TPS " + ColorUtils.gray + MathUtils.round(TPSUtils.INSTANCE.getAverageTPS(), 2);
		this.setDefaultX(5);
		this.setDefaultY(scaledHeight - 30);
		this.setWidth((int) font.getStringWidth(tpsString));
		this.setHeight((int) font.getStringHeight(tpsString));
		font.drawWithShadow(matrices, tpsString, this.getX(), this.getY(), color.getRGB());
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
}
