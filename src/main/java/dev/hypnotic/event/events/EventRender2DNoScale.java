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
import net.minecraft.client.util.math.MatrixStack;

public class EventRender2DNoScale extends Event {

	private MatrixStack matrices;
	private float tickDelta;
	
	public EventRender2DNoScale(MatrixStack matrices, float tickDelta) {
		this.matrices = matrices;
		this.tickDelta = tickDelta;
	}
	
	public MatrixStack getMatrices() {
		return matrices;
	}
	
	public float getTickDelta() {
		return tickDelta;
	}
}
