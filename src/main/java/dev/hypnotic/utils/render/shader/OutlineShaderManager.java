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

import static dev.hypnotic.utils.MCUtils.mc;

import dev.hypnotic.mixin.WorldRendererAccessor;
import net.minecraft.client.gl.ShaderEffect;

public class OutlineShaderManager {

	public static void loadShader(ShaderEffect shader) {
		if (getCurrentShader() != null) {
			getCurrentShader().close();
		}

		((WorldRendererAccessor) mc.worldRenderer).setEntityOutlineShader(shader);
		((WorldRendererAccessor) mc.worldRenderer).setEntityOutlinesFramebuffer(shader.getSecondaryTarget("final"));
	}

	public static void loadDefaultShader() {
		mc.worldRenderer.loadEntityOutlineShader();
	}

	public static ShaderEffect getCurrentShader() {
		return ((WorldRendererAccessor) mc.worldRenderer).getEntityOutlineShader();
	}
}
