package badgamesinc.hypnotic.module.player;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.Setting;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.IClientPlayerInteractionManager;
import net.minecraft.entity.effect.StatusEffects;

public class SpeedMine extends Mod {
    public NumberSetting speed = new NumberSetting("Speed", 1.0D, 0.0D, 10.0D, 0.1D);

    public SpeedMine() {
        super("SpeedMine", "Mine blocks faster", Category.PLAYER);
        this.addSettings(new Setting[]{this.speed});
    }

    public void onTick() {
        String var10001 = ColorUtils.gray;
        this.setDisplayName("SpeedMine " + var10001 + this.speed.getValue());
        IClientPlayerInteractionManager minething = (IClientPlayerInteractionManager)mc.interactionManager;
        if (mc.player.hasStatusEffect(StatusEffects.HASTE)) {
            mc.player.hasStatusEffect(StatusEffects.HASTE);
        }

        if (minething != null && (double)minething.getBlockBreakProgress() > 1.0D - this.speed.getValue() * 0.1D) {
            minething.setBlockBreakProgress(1.0F);
        }

        if (minething != null)
        minething.setBlockBreakingCooldown(0);
        super.onTick();
    }
}
