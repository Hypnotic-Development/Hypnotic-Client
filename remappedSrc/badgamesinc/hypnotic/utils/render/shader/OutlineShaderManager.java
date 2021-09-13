package badgamesinc.hypnotic.utils.render.shader;

import badgamesinc.hypnotic.mixin.WorldRendererAccessor;
import net.minecraft.client.gl.ShaderEffect;

import static badgamesinc.hypnotic.utils.MCUtils.mc;

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
