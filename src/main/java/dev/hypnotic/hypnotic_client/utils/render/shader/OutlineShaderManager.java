package dev.hypnotic.hypnotic_client.utils.render.shader;

import static dev.hypnotic.hypnotic_client.utils.MCUtils.mc;

import dev.hypnotic.hypnotic_client.mixin.WorldRendererAccessor;
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
