package badgamesinc.hypnotic.ui;

import org.lwjgl.glfw.GLFW;

import badgamesinc.hypnotic.config.SaveLoad;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.render.ClickGUIModule;
import badgamesinc.hypnotic.utils.Wrapper;
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
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		((ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 - 50, height / 2 + 100, 100, 20, new LiteralText("Back"), (button) -> {
	         MinecraftClient.getInstance().setScreen(prevScreen);
	    }))).active = true;
		((ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 - 50, height / 2 + 80, 100, 20, new LiteralText("Re-bind"), (button) -> {
	         mod.setBinding(true);
	    }))).active = !mod.isBinding();
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
			SaveLoad.INSTANCE.save();
			if (keyCode != GLFW.GLFW_KEY_ESCAPE && keyCode != GLFW.GLFW_KEY_UNKNOWN) {
				mod.setKey(keyCode);
				mod.setBinding(false);
			} else {
				if (mod instanceof ClickGUIModule) { 
					Wrapper.tellPlayer("You cannot unbind the ClickGUI"); 
					return false;
				} else {
					mod.setKey(0);
					mod.setBinding(false);
				}
			}
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public void onClose() {
		mod.setBinding(false);
		super.onClose();
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
}
