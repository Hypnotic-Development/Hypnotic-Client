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
