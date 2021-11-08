package badgamesinc.hypnotic.mixin;

import static badgamesinc.hypnotic.utils.MCUtils.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.render.Freecam;
import badgamesinc.hypnotic.utils.RotationUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

	@ModifyVariable(method = "render", ordinal = 5, at = @At(value = "STORE", ordinal = 3))
    public float changePitch(float oldValue, LivingEntity entity) {
        if (entity.equals(mc.player) && RotationUtils.isCustomPitch) return RotationUtils.serverPitch;
        return oldValue;
    }
	
	@ModifyVariable(method = "render", ordinal = 2, at = @At(value = "STORE", ordinal = 0))
	public float changeYaw(float oldValue, LivingEntity entity) {
		if (entity.equals(mc.player) && RotationUtils.isCustomYaw) return RotationUtils.serverYaw;
        return oldValue;
    }

    @ModifyVariable(method = "render", ordinal = 3, at = @At(value = "STORE", ordinal = 0))
    public float changeHeadYaw(float oldValue, LivingEntity entity) {
    	if (entity.equals(mc.player) && RotationUtils.isCustomYaw) return RotationUtils.serverYaw;
        return oldValue;
    }
	
	@Redirect(method = "hasLabel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getCameraEntity()Lnet/minecraft/entity/Entity;"))
    private Entity hasLabelGetCameraEntityProxy(MinecraftClient mc) {
        if (ModuleManager.INSTANCE.getModule(Freecam.class).isEnabled()) return null;
        return mc.getCameraEntity();
    }
}
