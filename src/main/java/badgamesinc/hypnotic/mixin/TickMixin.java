package badgamesinc.hypnotic.mixin;

import java.io.IOException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import baritone.api.BaritoneAPI;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class TickMixin {
	int checkTicks;
	@SuppressWarnings("resource")
	@Inject(at = @At("HEAD"), method = "tick()V")
	private void init(CallbackInfo info) {
		for (Mod mod : ModuleManager.INSTANCE.getEnabledModules()) {
			if (MinecraftClient.getInstance().player != null) mod.onTick();
		}
		RenderUtils.INSTANCE.onTick();
		if (MinecraftClient.getInstance().world != null) BaritoneAPI.getSettings().chatControl.value = false;
		if (checkTicks < 10000) {
			checkTicks++;
		} else {
			System.out.println("e");
			for (PlayerListEntry player : MinecraftClient.getInstance().getNetworkHandler().getPlayerList()) {
				try {
					System.out.println(player.getProfile().getName());
					Hypnotic.setHypnoticUser(player.getProfile().getName(), Hypnotic.INSTANCE.api.checkOnline(player.getProfile().getName()));
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
				}
			}
			checkTicks=0;
		}
	}
}
