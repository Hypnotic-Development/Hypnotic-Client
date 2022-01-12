package dev.hypnotic.mixin;

import static dev.hypnotic.utils.MCUtils.mc;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.event.events.EventRenderGUI;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.render.NoRender;
import dev.hypnotic.utils.TimeHelper;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Mixin(InGameHud.class)
public class InGameHudMixin {

	@Shadow @Final private BossBarHud bossBarHud;
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
	
	@Inject(method = "renderOverlay", at = @At("HEAD"), cancellable = true)
	public void renderOverlay(Identifier texture, float opacity, CallbackInfo ci) {
		if (texture.equals(new Identifier("textures/misc/pumpkinblur.png")) && ModuleManager.INSTANCE.getModule(NoRender.class).isEnabled() && ModuleManager.INSTANCE.getModule(NoRender.class).pumpkin.isEnabled()) ci.cancel();
	}
	
	@Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
	public void renderPortalOverlay(float nauseaStrength, CallbackInfo ci) {
		if (ModuleManager.INSTANCE.getModule(NoRender.class).isEnabled() && ModuleManager.INSTANCE.getModule(NoRender.class).portal.isEnabled()) {
			mc.player.lastNauseaStrength = -1;
			mc.player.nextNauseaStrength = -1;
			ci.cancel();
		}
	}
	
	@Inject(method = "tick", at = @At("HEAD"))
	public void onTick(CallbackInfo ci) {
		EventRenderGUI.Tick event = new EventRenderGUI.Tick();
		event.call();
	}
}