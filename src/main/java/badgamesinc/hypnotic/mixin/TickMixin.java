package badgamesinc.hypnotic.mixin;

import static badgamesinc.hypnotic.utils.MCUtils.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.hud.HudManager;
import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import baritone.api.BaritoneAPI;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntity.class)
public class TickMixin {
	int checkTicks;

	@Inject(at = @At("HEAD"), method = "tick()V")
	private void init(CallbackInfo info) {
		BaritoneAPI.getSettings().chatControl.value = false;
		for (Mod mod : ModuleManager.INSTANCE.modules) {
			if (mc.player != null) {
//				try {
					if (mod.isEnabled()) mod.onTick();
					mod.onTickDisabled();
//				} catch(Exception e) {
//					e.printStackTrace();
//				}
			}
		}
		for (HudModule mod : HudManager.INSTANCE.hudModules) {
			if (mc.player != null) {
				if (mod.isEnabled()) mod.onTick();
				mod.onTickDisabled();
			}
		}
		RenderUtils.INSTANCE.onTick();
//		if (mc.world != null) BaritoneAPI.getSettings().chatControl.value = false;
//		if (checkTicks < 10000) {
//			checkTicks++;
//		} else {
//			System.out.println("e");
//			for (PlayerListEntry player : mc.getNetworkHandler().getPlayerList()) {
//				System.out.println(player.getProfile().getName());
//				Hypnotic.setHypnoticUser(player.getProfile().getName(), Hypnotic.INSTANCE.api.checkOnline(player.getProfile().getName()));
//			}
//			checkTicks=0;
//		}
	}
}
