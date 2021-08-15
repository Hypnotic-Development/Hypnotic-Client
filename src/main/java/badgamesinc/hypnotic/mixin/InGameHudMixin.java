package badgamesinc.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import badgamesinc.hypnotic.event.events.EventRenderGUI;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameHud.class)
public class InGameHudMixin {

	@Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    
	@Inject(method = "render", at = @At("RETURN"), cancellable = true) 
	public void onRender (MatrixStack matrices, float tickDelta, CallbackInfo info) {
		EventRenderGUI event = new EventRenderGUI(matrices, tickDelta);
		event.call();
	}

}