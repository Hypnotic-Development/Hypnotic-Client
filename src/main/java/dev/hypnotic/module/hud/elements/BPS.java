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

import dev.hypnotic.mixin.MinecraftClientAccessor;
import dev.hypnotic.mixin.RenderTickCounterAccessor;
import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.math.MathUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BPS extends HudModule {

	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	public BPS() {
		super("Blocks/s Display", "Renders your blocks/s", (int) FontManager.roboto.getStringWidth("Ping 80") + 5, 1050, (int)FontManager.roboto.getStringWidth("Ping 80"), (int)FontManager.roboto.getStringHeight("Ping 80"));
		addSetting(color);
	}
	
	@Override
	public void onEnable() {
		this.setDefaultX((int) FontManager.roboto.getStringWidth("Ping 80"));
		this.setDefaultY(mc.getWindow().getScaledHeight() - 20);
		if (this.getX() > mc.getWindow().getScaledWidth()) this.setX(this.getDefaultX());
		if (this.getY() > mc.getWindow().getScaledHeight()) this.setY(this.getDefaultY());
		super.onEnable();
	}

	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		String bpsString = "Blocks/s " + ColorUtils.gray + MathUtils.round(moveSpeed() * (50f / ((RenderTickCounterAccessor)((MinecraftClientAccessor)mc).getRenderTickCounter()).getTickTime()), 2);
		this.setDefaultX((int) FontManager.roboto.getStringWidth("TPS 20"));
		this.setDefaultY(scaledHeight - 18);
		this.setWidth((int) font.getStringWidth(bpsString));
		this.setHeight((int) font.getStringHeight(bpsString));
		font.drawWithShadow(matrices, bpsString, this.getX(), this.getY(), color.getRGB());
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
	
	
	private double moveSpeed() {
        Vec3d move = new Vec3d(mc.player.getX() - mc.player.prevX, 0, mc.player.getZ() - mc.player.prevZ).multiply(20);

        return Math.abs(length2D(move)) ;
    }
	
	public double length2D(Vec3d vec3d) {
        return MathHelper.sqrt((float)(vec3d.x * vec3d.x + vec3d.z * vec3d.z));
    }
}
