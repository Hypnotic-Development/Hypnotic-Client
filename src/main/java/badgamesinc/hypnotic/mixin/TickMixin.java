package badgamesinc.hypnotic.mixin;

import static badgamesinc.hypnotic.utils.MCUtils.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.hud.HudManager;
import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.ui.BindingScreen;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import baritone.api.BaritoneAPI;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class TickMixin {
	int checkTicks;

	@Inject(at = @At("HEAD"), method = "tick()V")
	private void init(CallbackInfo info) {
		for (Mod mod : ModuleManager.INSTANCE.modules) {
			if (mc.player != null) {
				if (mod.isBinding() && mc.currentScreen == null) {
					try {
						mc.setScreen(new BindingScreen(mod, null));
					} catch(Exception e) {
						
					}
				}
				if (mod.isEnabled()) mod.onTick();
				mod.onTickDisabled();
			}
		}
		for (HudModule mod : HudManager.INSTANCE.hudModules) {
			if (mc.player != null) {
				if (mod.isEnabled()) mod.onTick();
				mod.onTickDisabled();
			}
		}
		RenderUtils.INSTANCE.onTick();
		if (mc.world != null) BaritoneAPI.getSettings().chatControl.value = false;
		
		if (mc.getNetworkHandler() != null) {
			for (PlayerListEntry player : mc.getNetworkHandler().getPlayerList()) {
				if (player.getDisplayName() != null)
				Hypnotic.setHypnoticUser(player.getProfile().getName(), player.getLatency() == -1000);
			}
		}
	}
}
