package dev.hypnotic.mixin;

import static dev.hypnotic.utils.MCUtils.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.event.events.EventEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "addEntity", at = @At("RETURN"))
    public void addEntity(int id, Entity entity, CallbackInfo ci) {
    	if (entity instanceof PlayerEntity) {
    		System.out.println("Sdfsdfs");
    	}
        new EventEntity.Spawn(entity).call();
    }

	@Inject(method = "removeEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;onRemoved()V"))
    public void removeEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo ci) {
    	if (mc.world.getEntityById(entityId) instanceof PlayerEntity) {
    		System.out.println("sdfsdfs");
    	}
    	new EventEntity.Remove(mc.world.getEntityById(entityId)).call();
    }
}
