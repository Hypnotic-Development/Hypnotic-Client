package badgamesinc.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import badgamesinc.hypnotic.event.events.EventRenderGUI;
import badgamesinc.hypnotic.utils.TimeHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(InGameHud.class)
public class InGameHudMixin {

	@Shadow private int scaledWidth;
    @Shadow private int scaledHeight;
    private float beginTime = TimeHelper.getTime();
    
    @Inject(method = "render", at = @At("HEAD"), cancellable = true) 
	public void onRenderHead (MatrixStack matrices, float tickDelta, CallbackInfo info) {
		float beginTime = TimeHelper.getTime();
		this.beginTime = beginTime;
	}
    
	@Inject(method = "render", at = @At("RETURN"), cancellable = true) 
	public void onRender (MatrixStack matrices, float tickDelta, CallbackInfo info) {
		float endTime = TimeHelper.getTime();
		TimeHelper.setDeltaTime(this.beginTime - endTime);
		this.beginTime = endTime;
		EventRenderGUI event = new EventRenderGUI(matrices, tickDelta);
		event.call();
	}

	@Inject(method = "tick", at = @At("HEAD"))
	public void onTick(CallbackInfo ci) {
		EventRenderGUI.Tick event = new EventRenderGUI.Tick();
		event.call();
	}
}