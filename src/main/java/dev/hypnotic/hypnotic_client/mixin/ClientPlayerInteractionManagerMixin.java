package dev.hypnotic.hypnotic_client.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.hypnotic.hypnotic_client.event.events.EventDestroyBlock;
import dev.hypnotic.hypnotic_client.utils.mixin.IClientPlayerInteractionManager;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;

@Mixin({ClientPlayerInteractionManager.class})
public abstract class ClientPlayerInteractionManagerMixin implements IClientPlayerInteractionManager {
   
	@Shadow private float currentBreakingProgress;
    @Shadow private int blockBreakingCooldown;
    
    @Shadow protected abstract void syncSelectedSlot();

    @Inject(method = "breakBlock", at = @At("RETURN"))
    public void breakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        new EventDestroyBlock(pos).call();
    }
    
    public void setBlockBreakProgress(float progress) {
        this.currentBreakingProgress = progress;
    }

    public void setBlockBreakingCooldown(int cooldown) {
        this.blockBreakingCooldown = cooldown;
    }

    public float getBlockBreakProgress() {
        return this.currentBreakingProgress;
    }

	@Override
	public void syncSelected() {
		syncSelectedSlot();
	}
}