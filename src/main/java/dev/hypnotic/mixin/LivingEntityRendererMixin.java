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
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.render.Freecam;
import dev.hypnotic.utils.RotationUtils;
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
