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

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.event.events.EventEntityRender;
import dev.hypnotic.event.events.EventRenderNametags;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    
	@Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    public void renderLabel(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (entity instanceof LivingEntity) {
            EventRenderNametags eventRenderNametags = new EventRenderNametags((LivingEntity) entity, matrices, vertexConsumers);
            eventRenderNametags.call();
            if (eventRenderNametags.isCancelled())
                ci.cancel();
        }
    }
    
    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
	public void renderLabelIfPresent(T entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
		EventEntityRender.Single.Label event = new EventEntityRender.Single.Label(entity, matrices, vertexConsumers);
		event.call();

		if (event.isCancelled()) {
			ci.cancel();
		}
	}
}
