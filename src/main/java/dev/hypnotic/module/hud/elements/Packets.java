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

import java.util.ArrayList;
import java.util.List;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventSendPacket;
import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.utils.ColorUtils;
import net.minecraft.client.util.math.MatrixStack;

public class Packets extends HudModule {

	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	
	private List<Long> packets = new ArrayList<>();
	private long lastSent;
	
	public Packets() {
		super("Packets/s", "Shows how many packets a second you are sending", 4, 50, 1, 1);
		addSettings(color);
	}
	
	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		font.drawWithShadow(matrices, "Packets/s " + ColorUtils.gray + getPacketsPerSecond(), this.getX(), this.getY(), color.getRGB());
		this.setWidth(font.getStringWidth("Packets/s " + ColorUtils.gray + getPacketsPerSecond()));
		this.setHeight(font.getStringHeight("Packets/s " + ColorUtils.gray + getPacketsPerSecond()));
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
	
	private int getPacketsPerSecond() {
		try {
			final long time = System.currentTimeMillis();
			packets.removeIf(aLong -> aLong + 1000 < time);
			return this.packets.size();
		} catch(Exception e) {
			return 0;
		}
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		lastSent = System.currentTimeMillis();
		packets.add(lastSent);
	}
}
