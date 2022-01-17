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

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {

	@Accessor
	public abstract Framebuffer getEntityOutlinesFramebuffer();
	
	@Accessor
	public abstract void setEntityOutlinesFramebuffer(Framebuffer framebuffer);
	
	@Accessor
	public abstract ShaderEffect getEntityOutlineShader();
	
	@Accessor
	public abstract void setEntityOutlineShader(ShaderEffect shaderEffect);
	
	@Accessor
	public abstract Frustum getFrustum();
	
	@Accessor
	public abstract void setFrustum(Frustum frustum);
}
