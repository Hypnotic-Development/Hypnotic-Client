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
package dev.hypnotic.event.events;

import dev.hypnotic.event.Event;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class EventEntityRender extends Event {

	public static class Single extends EventEntityRender {

		protected Entity entity;
		protected MatrixStack matrices;
		protected VertexConsumerProvider vertex;

		public Entity getEntity() {
			return entity;
		}

		public MatrixStack getMatrix() {
			return matrices;
		}

		public VertexConsumerProvider getVertex() {
			return vertex;
		}

		public static class Pre extends Single {

			public Pre(Entity entity, MatrixStack matrices, VertexConsumerProvider vertex) {
				this.entity = entity;
				this.matrices = matrices;
				this.vertex = vertex;
			}

			public void setMatrix(MatrixStack matrices) {
				this.matrices = matrices;
			}

			public void setVertex(VertexConsumerProvider vertex) {
				this.vertex = vertex;
			}

			public void setEntity(Entity entity) {
				this.entity = entity;
			}
		}

		public static class Post extends Single {

			public Post(Entity entity, MatrixStack matrices, VertexConsumerProvider vertex) {
				this.entity = entity;
				this.matrices = matrices;
				this.vertex = vertex;
			}
		}

		public static class Label extends Single {

			public Label(Entity entity, MatrixStack matrices, VertexConsumerProvider vertex) {
				this.entity = entity;
				this.matrices = matrices;
				this.vertex = vertex;
			}

			public void setMatrix(MatrixStack matrices) {
				this.matrices = matrices;
			}

			public void setVertex(VertexConsumerProvider vertex) {
				this.vertex = vertex;
			}
		}
	}

	public static class PreAll extends EventEntityRender {
	}

	public static class PostAll extends EventEntityRender {
	}
}
