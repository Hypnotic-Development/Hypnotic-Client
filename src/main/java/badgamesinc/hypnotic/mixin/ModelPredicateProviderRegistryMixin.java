package badgamesinc.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.render.Freecam;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

import static badgamesinc.hypnotic.utils.MCUtils.mc;

@Mixin(targets = "net.minecraft.client.item.ModelPredicateProviderRegistry$2")
public class ModelPredicateProviderRegistryMixin {
    @Redirect(method = "unclampedCall(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/entity/LivingEntity;I)F", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getYaw()F"))
    private float callLivingEntityGetYaw(LivingEntity entity) {
        if (ModuleManager.INSTANCE.getModule(Freecam.class).isEnabled()) return mc.gameRenderer.getCamera().getYaw();
        return entity.getYaw();
    }

    @Inject(method = "getAngleToPos(Lnet/minecraft/util/math/Vec3d;Lnet/minecraft/entity/Entity;)D", at = @At("HEAD"), cancellable = true)
    private void onGetAngleToPos(Vec3d pos, Entity entity, CallbackInfoReturnable<Double> info) {
        if (ModuleManager.INSTANCE.getModule(Freecam.class).isEnabled()) {
            Camera camera = mc.gameRenderer.getCamera();
            info.setReturnValue(Math.atan2(pos.getZ() - camera.getPos().z, pos.getX() - camera.getPos().x));
        }
    }
}
