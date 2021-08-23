package badgamesinc.hypnotic.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import badgamesinc.hypnotic.event.events.EventEntity;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "addEntity", at = @At("RETURN"))
    public void addEntity(int id, Entity entity, CallbackInfo ci) {
        new EventEntity.Spawn(entity).call();
    }

    @SuppressWarnings("resource")
	@Inject(method = "removeEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;onRemoved()V"))
    public void removeEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci) {
    	new EventEntity.Remove(MinecraftClient.getInstance().world.getEntityById(entityId)).call();
    }
}
