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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.event.events.EventBlockEntityRender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;

@Mixin(BlockEntityRenderDispatcher.class)
public class BlockEntityRenderDispatcherMixin {

	@Shadow private static <T extends BlockEntity> void render(BlockEntityRenderer<T> renderer, T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {}

	@SuppressWarnings("unchecked")
	@Redirect(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BlockEntityRenderDispatcher;render(Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V"))
	private static <T extends BlockEntity> void render_render(BlockEntityRenderer<T> renderer, T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers) {
		EventBlockEntityRender.Single.Pre event = new EventBlockEntityRender.Single.Pre(blockEntity, matrices, vertexConsumers);
		event.call();

		if (!event.isCancelled()) {
			render(renderer, (T) event.getBlockEntity(), tickDelta, event.getMatrices(), event.getVertexConsumers());
		}
	}
	
	@Inject(method = "render(Lnet/minecraft/client/render/block/entity/BlockEntityRenderer;Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", at = @At("RETURN"))
	private static <T extends BlockEntity> void render(BlockEntityRenderer<T> renderer, T blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
		EventBlockEntityRender.Single.Post event = new EventBlockEntityRender.Single.Post(blockEntity, matrices, vertexConsumers);
		event.call();
	}
}
