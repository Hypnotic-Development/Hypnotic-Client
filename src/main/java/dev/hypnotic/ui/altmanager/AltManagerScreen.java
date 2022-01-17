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
package dev.hypnotic.ui.altmanager;

import java.awt.Color;
import java.io.File;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.ui.altmanager.account.Account;
import dev.hypnotic.ui.altmanager.account.Accounts;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class AltManagerScreen extends Screen {
	
	private String status;
	@SuppressWarnings("unused")
	private boolean loggingIn = false;
	
	protected AltManagerScreen() {
		super(new LiteralText("AltManager"));
	}
	
	public static AltManagerScreen INSTANCE = new AltManagerScreen();
	public File altsFile = new File(Hypnotic.hypnoticDir, "alts.txt");
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		fill(matrices, 100, 70, width - 100, height - 70, new Color(0, 0, 0, 100).getRGB());
		drawStringWithShadow(matrices, textRenderer, MinecraftClient.getInstance().getSession().getProfile().getName(), 20, 20, -1);
		RenderUtils.drawCenteredStringWithShadow(matrices, textRenderer, status, width / 2, 20, -1);
		((ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, height - 50, 200, 20, new LiteralText("Add alt"), (button) -> {
	         MinecraftClient.getInstance().setScreen(new AddAltScreen(this));
	    }))).active = true;
		int offset = 0;
		RenderSystem.enableScissor(10, 10, 1000, 1000);
//		GL11.glScissor(10, 10, 100000, 10000);
		for (Account<?> alt : Accounts.get()) {
			((ButtonWidget)this.addDrawableChild(new ButtonWidget(100, height - 50 + offset, 200, 20, new LiteralText("Login"), (button) -> {
		         MinecraftClient.getInstance().setScreen(new AddAltScreen(this));
		    }))).active = true;
			drawStringWithShadow(matrices, textRenderer, alt.getUsername(), 100 + 5, offset, -1);
			drawStringWithShadow(matrices, textRenderer, alt.getType().name(), 100 + 5, offset + 20, -1);
			offset+=50;
		}
		RenderSystem.disableScissor();
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	protected void init() {
		status = "Idle...";
		AltsFile.INSTANCE.loadAlts();
		super.init();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
