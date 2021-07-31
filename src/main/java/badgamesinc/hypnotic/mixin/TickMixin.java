package badgamesinc.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.utils.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class TickMixin {
	@SuppressWarnings("resource")
	@Inject(at = @At("HEAD"), method = "tick()V")
	private void init(CallbackInfo info) {
		for (Mod mod : ModuleManager.INSTANCE.getEnabledModules()) {
			if (MinecraftClient.getInstance().player != null) mod.onTick();
		}
		RenderUtils.INSTANCE.onTick();
	}
}
