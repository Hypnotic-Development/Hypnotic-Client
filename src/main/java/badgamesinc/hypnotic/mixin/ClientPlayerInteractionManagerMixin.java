package badgamesinc.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import badgamesinc.hypnotic.utils.IClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerInteractionManager;

@Mixin({ClientPlayerInteractionManager.class})
public class ClientPlayerInteractionManagerMixin implements IClientPlayerInteractionManager {
    @Shadow
    private float currentBreakingProgress;
    @Shadow
    private int blockBreakingCooldown;

    public void setBlockBreakProgress(float progress) {
        this.currentBreakingProgress = progress;
    }

    public void setBlockBreakingCooldown(int cooldown) {
        this.blockBreakingCooldown = cooldown;
    }

    public float getBlockBreakProgress() {
        return this.currentBreakingProgress;
    }
}