package dev.hypnotic.hypnotic_client.utils.mixin;

public interface IClientPlayerInteractionManager {
    void setBlockBreakProgress(float var1);

    void setBlockBreakingCooldown(int var1);

    float getBlockBreakProgress();
    
    void syncSelected();
}
