package dev.hypnotic.hypnotic_client.utils.render.shader;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import dev.hypnotic.hypnotic_client.utils.render.Matrix4x4;
import dev.hypnotic.hypnotic_client.utils.render.shader.shaders.PosColorShader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.Shader;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public enum ShaderUtils {
    INSTANCE;
	private static MinecraftClient mc = MinecraftClient.getInstance();
    public Framebuffer storageFBO;
    public ShaderEffect storageShader;
    public Framebuffer boxOutlineFBO;
    public ShaderEffect boxOutlineShader;
    public Identifier identifier_1 = new Identifier("hypnotic", "shaders/entity_outline.json");

    private static Shader rainbowEnchantShader;
    private static Shader translucentShader;
    private static Shader testShader;

    public Matrix4x4 projectionMatrix;
    public Matrix4x4 modelViewMatrix;
    private PosColorShader posColorShader = new PosColorShader();
    //private OutlineShader outlineShader = new OutlineShader();

    public void drawStorageFBO() {
        if (canDrawFBO()) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
            storageFBO.draw(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), false);
            RenderSystem.disableBlend();
        }
    }

    public void drawBoxOutlineFBO() {
        if (canDrawFBO()) {
            RenderSystem.enableBlend();
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
            boxOutlineFBO.draw(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), false);
            RenderSystem.disableBlend();
        }
    }

    public void onResized(int int_1, int int_2) {
        if (storageShader != null) {
            storageShader.setupDimensions(int_1, int_2);
        }
        if (boxOutlineShader != null) {
            boxOutlineShader.setupDimensions(int_1, int_2);
        }
    }
    public boolean canDrawFBO() {
        return storageFBO != null && storageShader != null && mc.player != null;
    }

    public void load()
    {
        if (storageShader != null) {
            storageShader.close();
        }
        if (boxOutlineShader != null) {
            boxOutlineShader.close();
        }
        try {
            storageShader = new ShaderEffect(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), identifier_1);
            storageShader.setupDimensions(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
            storageFBO = storageShader.getSecondaryTarget("final");
            System.out.println(storageFBO != null);
            boxOutlineShader = new ShaderEffect(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), identifier_1);
            boxOutlineShader.setupDimensions(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight());
            boxOutlineFBO = boxOutlineShader.getSecondaryTarget("final");
        } catch (Exception var3) {
        	var3.printStackTrace();
            storageShader = null;
            storageFBO = null;
        }

    }

    public static void loadCustomMCShaders() {
        try {
            rainbowEnchantShader = new Shader(mc.getResourcePackProvider().getPack(), "hypnotic:rainbow_enchant", VertexFormats.POSITION_TEXTURE);
            translucentShader = new Shader(mc.getResourcePackProvider().getPack(), "hypnotic:translucent", VertexFormats.POSITION_COLOR_TEXTURE_LIGHT_NORMAL);
            testShader = new Shader(mc.getResourcePackProvider().getPack(), "hypnotic:test", VertexFormats.POSITION_COLOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (storageShader != null) {
            storageShader.close();
        }
        if (boxOutlineShader != null) {
            boxOutlineShader.close();
        }
    }

    public Matrix4x4 getProjectionMatrix() {
        return projectionMatrix;
    }

    public void setProjectionMatrix(Matrix4x4 projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    public Matrix4x4 getModelViewMatrix() {
        return modelViewMatrix;
    }

    public void setModelViewMatrix(Matrix4x4 modelViewMatrix) {
        this.modelViewMatrix = modelViewMatrix;
    }

    public static Shader getRainbowEnchantShader() {
        return rainbowEnchantShader;
    }

    public static Shader getTranslucentShader() {
        return translucentShader;
    }

    public static Shader getTestShader() {
        return testShader;
    }

    public PosColorShader getPosColorShader() {
        return posColorShader;
    }

}
