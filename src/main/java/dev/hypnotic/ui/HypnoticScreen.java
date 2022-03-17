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
package dev.hypnotic.ui;

import java.util.ArrayList;

import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Utils;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.font.NahrFont;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public abstract class HypnoticScreen extends Screen {

	public double mouseX, mouseY;
	public static NahrFont font = FontManager.roboto;
	public static NahrFont fontMed = FontManager.robotoMed;
	public static NahrFont fontSmall = FontManager.robotoSmall;
	public static NahrFont fontBig = FontManager.robotoBig;
	protected ArrayList<Button> buttons = new ArrayList<>();
	protected MinecraftClient mc = MinecraftClient.getInstance();
	
	public HypnoticScreen() {
		super(new LiteralText("Hypnotic-Screen"));
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if (font.mcFont != (OptionsScreen.INSTANCE.forceCFont.isEnabled() ? false : FontManager.mcFont)) {
			font = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), "assets/hypnotic/fonts/Roboto-Regular.ttf"), 18, 1, OptionsScreen.INSTANCE.forceCFont.isEnabled() ? false : FontManager.mcFont);
			fontMed = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), "assets/hypnotic/fonts/Roboto-Regular.ttf"), 20, 1, OptionsScreen.INSTANCE.forceCFont.isEnabled() ? false : FontManager.mcFont);
			fontSmall = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), "assets/hypnotic/fonts/Roboto-Regular.ttf"), 16, 1, OptionsScreen.INSTANCE.forceCFont.isEnabled() ? false : FontManager.mcFont);
			fontBig = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), "assets/hypnotic/fonts/Roboto-Regular.ttf"), 25, 1, OptionsScreen.INSTANCE.forceCFont.isEnabled() ? false : FontManager.mcFont);
		}
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	public boolean hovered(int x1, int y1, int x2, int y2) {
		return mouseX >= x1 && mouseX <= x2 && mouseY <= y1 && mouseY >= y2;
	}

	public void addButton(Button button) {
		if (!buttons.contains(button)) {
			buttons.add(button);
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (Button b : buttons) {
			if (b.isHovered(mouseX, mouseY)) {
				b.onClick();
				return false;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void renderBackground(MatrixStack matrices) {
		if (OptionsScreen.INSTANCE.disableGradient.isEnabled()) this.renderBackground(matrices, 0);
		else RenderUtils.gradientFill(matrices, 0, 0, width, height, ColorUtils.transparent(100), ColorUtils.defaultClientColor);
	}
	
	@Override
	public void close() {
		this.buttons.clear();
		super.close();
	}
}
