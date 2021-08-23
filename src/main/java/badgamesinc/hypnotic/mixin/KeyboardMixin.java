package badgamesinc.hypnotic.mixin;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import badgamesinc.hypnotic.event.events.EventKeyPress;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.utils.KeyUtils;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Shadow @Final private MinecraftClient client;

	@SuppressWarnings("resource")
	@Inject(method = "onKey", at = @At("HEAD"), cancellable = true)
    public void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo info) {
        if (key != GLFW.GLFW_KEY_UNKNOWN) {
        	/*
        	 * action == GLFW.GLFW_PRESS is important
        	 * so the module does not toggle twice
        	 */
        	KeyUtils.setKeyState(key, action != GLFW.GLFW_RELEASE);
            for (Mod mod : ModuleManager.INSTANCE.modules) {
                if (mod.getKey() == key && action == GLFW.GLFW_PRESS && MinecraftClient.getInstance().currentScreen == null)
                    mod.toggle();
            }
            EventKeyPress event = new EventKeyPress(key, scancode, action);
            event.call();
            
            if (MinecraftClient.getInstance().currentScreen == null && key == GLFW.GLFW_KEY_PERIOD && action == GLFW.GLFW_PRESS) MinecraftClient.getInstance().setScreen(new ChatScreen(""));
        }
    }
}
