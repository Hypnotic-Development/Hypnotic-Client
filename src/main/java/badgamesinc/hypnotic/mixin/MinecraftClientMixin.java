package badgamesinc.hypnotic.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.event.events.EventTick;
import badgamesinc.hypnotic.utils.world.BlockIterator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow @Final private RenderTickCounter renderTickCounter;
	
	@Inject(at = @At("TAIL"), method = "updateWindowTitle()V")
	public void updateWindowTitle(CallbackInfo info) {
		MinecraftClient.getInstance().getWindow().setTitle(Hypnotic.fullName);
	}
	
	@Inject(at = @At("HEAD"), method = "tick")
    private void onPreTick(CallbackInfo info) {
		BlockIterator.INSTANCE.onTick();
		EventTick event = new EventTick();
		event.call();
	}
	
	@Inject(at = @At("TAIL"), method = "stop")
	public void onShutdown(CallbackInfo ci) {
		Hypnotic.INSTANCE.shutdown();
	}
}
