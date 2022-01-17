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
package dev.hypnotic.utils.render.shader;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OutlineVertexConsumerProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.util.Identifier;

public class OutlineVertexConsumers {

	private static final Identifier nonExistentId = new Identifier("hypnotic", "shader-placeholder-id");

	public static VertexConsumer outlineOnlyConsumer(float r, float g, float b, float a) {
		OutlineVertexConsumerProvider vertexProvider = MinecraftClient.getInstance().getBufferBuilders().getOutlineVertexConsumers();
		vertexProvider.setColor((int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (a * 255));

		return vertexProvider.getBuffer(RenderLayer.getOutline(nonExistentId));
	}

	public static VertexConsumerProvider outlineOnlyProvider(float r, float g, float b, float a) {
		OutlineVertexConsumerProvider vertexProvider = MinecraftClient.getInstance().getBufferBuilders().getOutlineVertexConsumers();
		vertexProvider.setColor((int) (r * 255), (int) (g * 255), (int) (b * 255), (int) (a * 255));

		return new Override(vertexProvider);
	}

	private static class Override implements VertexConsumerProvider {

		private OutlineVertexConsumerProvider parentProvider;

		public Override(OutlineVertexConsumerProvider parent) {
			this.parentProvider = parent;
		}

		public VertexConsumer getBuffer(RenderLayer renderLayer) {
			return parentProvider.getBuffer(RenderLayer.getOutline(nonExistentId));
		}
	}
}
