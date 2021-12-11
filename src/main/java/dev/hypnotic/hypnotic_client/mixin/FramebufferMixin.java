package dev.hypnotic.hypnotic_client.mixin;

import org.lwjgl.opengl.GL30;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.minecraft.client.gl.Framebuffer;

@Mixin (Framebuffer.class)
public class FramebufferMixin {
	@ModifyArgs (method = "initFbo",
			at = @At (value = "INVOKE",
					target = "Lcom/mojang/blaze3d/platform/GlStateManager;_texImage2D(IIIIIIIILjava/nio/IntBuffer;)V",
					ordinal = 0))
	public void init(Args args) {
		args.set(2, GL30.GL_DEPTH32F_STENCIL8);
		args.set(6, GL30.GL_DEPTH_STENCIL);
		args.set(7, GL30.GL_FLOAT_32_UNSIGNED_INT_24_8_REV);
	}

	@ModifyArgs (method = "initFbo",
			at = @At (value = "INVOKE", target = "Lcom/mojang/blaze3d/platform/GlStateManager;_glFramebufferTexture2D(IIIII)V", ordinal = 1))
	public void init2(Args args) {
		args.set(1, GL30.GL_DEPTH_STENCIL_ATTACHMENT);
	}
}
