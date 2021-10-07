package badgamesinc.hypnotic.mixin;

import static badgamesinc.hypnotic.utils.MCUtils.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import badgamesinc.hypnotic.utils.RotationUtils;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

	@ModifyVariable(method = "render", ordinal = 5, at = @At(value = "STORE", ordinal = 3))
    public float changePitch(float oldValue, LivingEntity entity) {
        if (entity.equals(mc.player) && RotationUtils.isCustomPitch) return RotationUtils.serverPitch;
        return oldValue;
    }
}