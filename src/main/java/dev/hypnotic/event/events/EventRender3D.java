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

public class EventRender3D extends Event {
    private MatrixStack matrices;
    private float tickDelta;
    private double offsetX, offsetY, offsetZ;

    public EventRender3D(MatrixStack matrices, float tickDelta, double offsetX, double offsetY, double offsetZ) {
        this.matrices = matrices;
        this.tickDelta = tickDelta;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }
    
    public MatrixStack getMatrices() {
		return matrices;
	}
    
    public float getTickDelta() {
		return tickDelta;
	}
    
    public double getOffsetX() {
		return offsetX;
	}
    
    public double getOffsetY() {
		return offsetY;
	}
    
    public double getOffsetZ() {
		return offsetZ;
	}
}
