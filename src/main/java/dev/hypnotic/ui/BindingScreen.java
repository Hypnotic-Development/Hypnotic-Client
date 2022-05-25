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

import static dev.hypnotic.utils.MCUtils.mc;

import org.lwjgl.glfw.GLFW;

import dev.hypnotic.config.PositionsConfig;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.render.ClickGUIModule;
import dev.hypnotic.utils.ChatUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class BindingScreen extends Screen {
	private Mod mod;
	private Screen prevScreen;
	
	public BindingScreen(Mod mod, Screen prevScreen) {
		super(new LiteralText("BindingScreen"));
		this.mod = mod;
		this.prevScreen = prevScreen;
	}
	
	@Override
	protected void init() {
		super.init();
		((ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 - 50, height / 2 + 100, 100, 20, new LiteralText("Back"), (button) -> {
			 mod.setBinding(false);
	         MinecraftClient.getInstance().setScreen(prevScreen);
	    }))).active = true;
		((ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 - 50, height / 2 + 80, 100, 20, new LiteralText("Re-bind"), (button) -> {
	         mod.setBinding(true);
	    }))).active = !mod.isBinding();
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		matrices.push();
		matrices.translate(width / 2, height / 2 - 100, 0);
		matrices.scale(2.5f, 2.5f, 0);
		
		Screen.drawCenteredText(matrices, textRenderer, "Binding " + mod.getName(), 0, 0, -1);
		matrices.pop();
		if (!mod.isBinding()) {
			matrices.push();
			matrices.translate(width / 2, height / 2 - 50, 0);
			matrices.scale(1.5f, 1.5f, 0);
			if (mod.getKey() != GLFW.GLFW_KEY_ESCAPE && mod.getKey() != GLFW.GLFW_KEY_UNKNOWN && mod.getKey() != 0) {
				Screen.drawCenteredText(matrices, textRenderer, "Bound " + mod.getName() + " to " + (GLFW.glfwGetKeyName(mod.getKey(), GLFW.glfwGetKeyScancode(mod.getKey())).toUpperCase()), 0, 0, -1);
			} else {
				Screen.drawCenteredText(matrices, textRenderer, "Unbound " + mod.getName(), 0, 0, -1);
			}
			matrices.pop();
		}
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (mod.isBinding()) {
			//Set bind to key pressed or unbind if the key is escape
			PositionsConfig.INSTANCE.save();
			if (keyCode != GLFW.GLFW_KEY_ESCAPE && keyCode != GLFW.GLFW_KEY_UNKNOWN) {
				mod.setKey(keyCode);
				mod.setBinding(false);
			} else {
				if (mod instanceof ClickGUIModule) { 
					ChatUtils.tellPlayer("You cannot unbind the ClickGUI"); 
					return false;
				} else {
					mod.setKey(0);
					mod.setBinding(false);
				}
			}
		} else {
			if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
				mc.setScreen(null);
			}
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public void close() {
		mod.setBinding(false);
		super.close();
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
