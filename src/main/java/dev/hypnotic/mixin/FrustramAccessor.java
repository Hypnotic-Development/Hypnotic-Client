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
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Vector4f;

@Mixin(Frustum.class)
public interface FrustramAccessor {
	
	@Accessor
	public abstract Vector4f[] getHomogeneousCoordinates();
	
	@Accessor
	public abstract void setHomogeneousCoordinates(Vector4f[] vector4f);
	
	@Accessor
	public abstract double getX();
	
	@Accessor
	public abstract void setX(double x);
	
	@Accessor
	public abstract double getY();
	
	@Accessor
	public abstract void setY(double y);
	
	@Accessor
	public abstract double getZ();
	
	@Accessor
	public abstract void setZ(double z);
}
