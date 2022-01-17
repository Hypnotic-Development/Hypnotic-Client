/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.mixin;

import static dev.hypnotic.utils.MCUtils.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.render.Freecam;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.Vec3d;

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
