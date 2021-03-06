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
package dev.hypnotic.utils.render;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.blaze3d.systems.RenderSystem;

import dev.hypnotic.mixin.FrustramAccessor;
import dev.hypnotic.mixin.WorldRendererAccessor;
import dev.hypnotic.module.render.IItemRenderer;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.mixin.IMatrix4f;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormat.DrawMode;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class RenderUtils {
    
		public static RenderUtils INSTANCE = new RenderUtils();
		static MinecraftClient mc = MinecraftClient.getInstance();
		public int scaledWidth = 0;
		public int scaledHeight = 0;
		
		public void onTick() {
			scaledWidth = mc.getWindow().getScaledWidth();
			scaledHeight = mc.getWindow().getScaledHeight();
		}
		
		public static WorldRenderer worldRenderer = mc.worldRenderer;
		public static Tessellator tessellator = Tessellator.getInstance();

		//took me too long to do this
		 public static void drawFilledCircle(MatrixStack matrices, float xx, float yy, float radius, int sides, Color color) {
			 	int sections = sides;
		        double dAngle = 2 * Math.PI / sections;
		        float x, y, lastX = 0, lastY = 0;
		        
		        for (int i = -1; i < sections; i++) {
					x = (float) (radius * Math.sin((i * dAngle)));
		            y = (float) (radius * Math.cos((i * dAngle)));
		            
		            Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder buffer = tessellator.getBuffer();

					RenderSystem.enableBlend();
					RenderSystem.disableDepthTest();
					RenderSystem.disableCull();
					RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
					RenderSystem.lineWidth(1);

					buffer.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
					Vertexer.vertexTri(matrices, buffer, xx, yy, 0f, xx + x, yy + y, 0f, xx + lastX, yy + lastY, 0f, color);
					tessellator.draw();

					RenderSystem.enableCull();
					RenderSystem.enableDepthTest();

					lastX = x;
					lastY = y;
				}
		    }
		 
		 // Drawing it with the vertexer is way too inconsistent for what I need circles for
		 public static void drawFilledCircle(MatrixStack matrices, double xx, double yy, float radius, Color color) {
			 RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
			 RenderUtils.bindTexture(new Identifier("hypnotic", "textures/circle.png"));
			 RenderSystem.enableBlend();
			 RenderUtils.drawTexture(matrices, (float) xx, (float)yy, radius, radius, 0, 0, radius, radius, radius, radius);
			 RenderSystem.disableBlend();
			 RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		 }
		 
		 public static void drawDropShadow(MatrixStack matrices, double xx, double yy, double width, double height, boolean elipse) {
			 RenderSystem.setShaderColor(1f, 1f, 1f, 0.5f);
			 RenderUtils.bindTexture(new Identifier("hypnotic", "textures/" + (elipse ? "circle" : "") + "shadow.png"));
			 RenderSystem.enableBlend();
			 RenderUtils.drawTexture(matrices, (float) xx, (float)yy, (float)width, (float)height, 0, 0, (float)width, (float)height, (float)width, (float)height);
			 RenderSystem.disableBlend();
			 RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		 }
		 
		 public static void drawOutlineCircle(MatrixStack matrices, double xx, double yy, double radius, Color color) {
			 RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
			 RenderUtils.bindTexture(new Identifier("hypnotic", "textures/outlinecircle.png"));
			 RenderSystem.enableBlend();
			 RenderUtils.drawTexture(matrices, (float) xx,(float) yy, (float)radius, (float)radius, 0, 0, (float)radius,(float) radius,(float) radius,(float) radius);
			 RenderSystem.disableBlend();
			 RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		 }
		 
		 public static void drawCircle(MatrixStack matrices, int xx, int yy, float radius, float width, int sides, Color color) {
			 	int sections = sides;
		        xx *= 2.0f;
		        yy *= 2.0f;
		        float theta = (float)(6.2831852 / (double)sections);
		        float p = (float)Math.cos(theta);
		        float s = (float)Math.sin(theta);
		        float x = radius *= 2.0f;
		        float y = 0.0f;
		        float lastX = x, lastY = 0;
		        for (int ii = -1; ii < sections; ++ii) {
		            Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder buffer = tessellator.getBuffer();

					RenderSystem.enableBlend();
					RenderSystem.disableDepthTest();
					RenderSystem.disableCull();
					RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
					RenderSystem.lineWidth(width);

					buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
					Vertexer.vertexLine(matrices, buffer, x + xx, yy + y, 0f, xx + lastX, yy + lastY, 0f, LineColor.single(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()));
					tessellator.draw();

					RenderSystem.enableCull();
					RenderSystem.enableDepthTest();

					lastX = x;
					lastY = y;
		            
		            float t = x;
		            x = p * x - s * y;
		            y = s * t + p * y;
		        }
		    }
		 
		 public static void drawGradientFilledCircle(MatrixStack matrices, final int xx, final int yy, final float radius, int sides, Color color1, Color color2) {
			 	int sections = sides;
		        double dAngle = 2 * Math.PI / sections;
		        float x, y, lastX = 0, lastY = 0;
		        
		        for (int i = -1; i < sections; i++) {
					x = (float) (radius * Math.sin((i * dAngle)));
		            y = (float) (radius * Math.cos((i * dAngle)));
		            
		            Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder buffer = tessellator.getBuffer();

					RenderSystem.enableBlend();
					RenderSystem.disableDepthTest();
					RenderSystem.disableCull();
					RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
					RenderSystem.lineWidth(1);

					buffer.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
					Vertexer.vertexTri(matrices, buffer, xx, yy, 0f, xx + x, yy + y, 0f, xx + lastX, yy + lastY, 0f, color1, color2);
					tessellator.draw();

					RenderSystem.enableCull();
					RenderSystem.enableDepthTest();

					lastX = x;
					lastY = y;
				}
		 }
		 
		 public static void drawStar(MatrixStack matrices, final int xx, final int yy, final float radius, Color color) {
			 	int sections = 360;
		        double dAngle = 2 * Math.PI / sections;
		        float x, y, lastX = 0;
		        
		        for (int i = 0; i < sections; i++) {
					x = (float) (radius * Math.sin((i * dAngle)));
		            y = (float) (radius * Math.cos((i * dAngle)));
		            
		            Tessellator tessellator = Tessellator.getInstance();
					BufferBuilder buffer = tessellator.getBuffer();

					RenderSystem.disableDepthTest();
					RenderSystem.disableCull();
					RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
					RenderSystem.lineWidth(1);

					buffer.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
					Vertexer.vertexTri(matrices, buffer, xx, yy, 0f, xx, yy + y, 10f, xx + lastX, yy, 0f, color);
					tessellator.draw();

					RenderSystem.enableCull();
					RenderSystem.enableDepthTest();

					lastX = x;
				}
		    }
		 
		 public static double getScaleFactor() {
		        return mc.getWindow().getScaleFactor();
		    }

		    public static int getScaledWidth() {
		        return mc.getWindow().getScaledWidth();
		    }

		    public static int getScaledHeight() {
		        return mc.getWindow().getScaledHeight();
		    }
		 
		 public static void drawRoundedRect(MatrixStack matrices, float left, float top, float right, float bottom, float smooth, Color color){
		        fill(matrices, left - smooth / 2, top - smooth, right + smooth / 2, bottom + smooth, color.getRGB());
		        fill(matrices, left - smooth, top - smooth / 2, right + smooth, bottom + smooth / 2, color.getRGB());
		        drawFilledCircle(matrices, left - smooth, top - smooth, smooth, color);
		        drawFilledCircle(matrices, right, top - smooth, smooth, color);
		        drawFilledCircle(matrices, right, bottom, smooth, color);
		        drawFilledCircle(matrices, left - smooth, bottom, smooth, color);

		    }
		 
		 public static void drawShittyRoundedRect(MatrixStack matrices, int left, int top, int right, int bottom, double length, double smooth, Color color){
		        for (double i = 0; i < length; i+=smooth) {
		        	fill(matrices, left - i, top + i, right + i, bottom - i, color.getRGB());
		        }
		    }
		 
		 public static void drawHLine(MatrixStack matrices, double x, double y, double x1, double y1, float width, int color) {
		        fill(matrices, x, y, x1 + width, y1 + width, color);

		    }
		 
		 public static void drawBorderRect(MatrixStack matrices, double x, double y, double x1, double y1, int color, double lwidth) {
		        fill(matrices, x, y, x1, y + lwidth, color);
		        fill(matrices, x1, y, x1 + lwidth, y1, color);
		        fill(matrices, x, y1, x1, y1 - lwidth, color);
		        fill(matrices, x, y1, x - lwidth, y, color);
		    }
		 
		 public static void drawArrow(MatrixStack matrices, double x, double y, float size, Color color) {
			 RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
			 RenderUtils.bindTexture(new Identifier("hypnotic", "textures/arrow.png"));
			 RenderSystem.enableBlend();
			 RenderUtils.drawTexture(matrices, (float) x, (float)y, size, size, 0, 0, size, size, size, size);
			 RenderSystem.disableBlend();
			 RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
		 }

		 public static void drawCenteredStringWithShadow(MatrixStack matrices, TextRenderer textRenderer, String text, int x, int y, int color) {
			 Screen.drawStringWithShadow(matrices, textRenderer, text, x - textRenderer.getWidth(text) / 2, y, color);
		 }
		 
		 public static Vec3d getRenderPosition(BlockPos blockPos) {
	         double minX = blockPos.getX() - mc.getEntityRenderDispatcher().camera.getPos().x;
	         double minY = blockPos.getY() - mc.getEntityRenderDispatcher().camera.getPos().y;
	         double minZ = blockPos.getZ() - mc.getEntityRenderDispatcher().camera.getPos().z;
	         return new Vec3d(minX, minY, minZ);
		 }
		 
		 public static void setup3DRender(boolean disableDepth) {
		        RenderSystem.setShader(GameRenderer::getPositionColorShader);
		        RenderSystem.disableTexture();
		        RenderSystem.enableBlend();
		        RenderSystem.defaultBlendFunc();
		        if (disableDepth)
		            RenderSystem.disableDepthTest();
		        RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
		        RenderSystem.enableCull();
		    }
		 
		 public static void end3DRender() {
		        RenderSystem.enableTexture();
		        RenderSystem.disableCull();
		        RenderSystem.disableBlend();
		        RenderSystem.enableDepthTest();
		        RenderSystem.depthMask(true);
		        RenderSystem.setShader(GameRenderer::getPositionColorShader);
		    }
		 
		 public static void drawFilledBox(MatrixStack matrixStack, Box bb, Color color, boolean draw) {
		        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
		        Color color1 = color;
		        setup3DRender(true);
		        
		        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		        if (draw)
		        	bufferBuilder.begin(VertexFormat.DrawMode.QUADS/*QUADS*/, VertexFormats.POSITION_COLOR);
		        float minX = (float)bb.minX;
		        float minY = (float)bb.minY;
		        float minZ = (float)bb.minZ;
		        float maxX = (float)bb.maxX;
		        float maxY = (float)bb.maxY;
		        float maxZ = (float)bb.maxZ;

		        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

		        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

		        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

		        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

		        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

		        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        if (draw) {
			        BufferRenderer.drawWithShader(bufferBuilder.end());
		        }
		        end3DRender();
		    }
		 
		 public static void drawOutlineBox(MatrixStack matrixStack, Box bb, Color color, boolean draw) {
		        Color color1 = color;
		        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

		        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		        if (draw)
		        	bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES/*LINES*/, VertexFormats.POSITION_COLOR);

		        VoxelShape shape = VoxelShapes.cuboid(bb);
		        shape.forEachEdge((x1, y1, z1, x2, y2, z2) -> {
		            bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z1).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		            bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z2).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        });
		        if (draw) {
			        BufferRenderer.drawWithShader(bufferBuilder.end());
		        }
		 }

		 public static Vec3d getEntityRenderPosition(Entity entity, double partial) {
		        double x = entity.prevX + ((entity.getX() - entity.prevX) * partial) - mc.getEntityRenderDispatcher().camera.getPos().x;
		        double y = entity.prevY + ((entity.getY() - entity.prevY) * partial) - mc.getEntityRenderDispatcher().camera.getPos().y;
		        double z = entity.prevZ + ((entity.getZ() - entity.prevZ) * partial) - mc.getEntityRenderDispatcher().camera.getPos().z;
		        return new Vec3d(x, y, z);
		 }

	    public static Vec3d getRenderPosition(double x, double y, double z, MatrixStack matrixStack) {
	        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
	        double minX = x - mc.getEntityRenderDispatcher().camera.getPos().x;
	        double minY = y - mc.getEntityRenderDispatcher().camera.getPos().y;
	        double minZ = z - mc.getEntityRenderDispatcher().camera.getPos().z;
	        Vector4f vector4f = new Vector4f((float)minX, (float)minY, (float)minZ, 1.f);
	        vector4f.transform(matrix);
	        return new Vec3d(vector4f.getX(), vector4f.getY(), vector4f.getZ());
	    }
	
	    public static Vec3d getRenderPosition(Vec3d vec3d, MatrixStack matrixStack) {
	        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
	        double minX = vec3d.getX() - mc.getEntityRenderDispatcher().camera.getPos().x;
	        double minY = vec3d.getY() - mc.getEntityRenderDispatcher().camera.getPos().y;
	        double minZ = vec3d.getZ() - mc.getEntityRenderDispatcher().camera.getPos().z;
	        Vector4f vector4f = new Vector4f((float)minX, (float)minY, (float)minZ, 1.f);
	        vector4f.transform(matrix);
	        return new Vec3d(vector4f.getX(), vector4f.getY(), vector4f.getZ());
	    }
	    
	    public static void drawEntityBox(MatrixStack matrixstack, Entity entity, double x, double y, double z, Color color) {
	        setup3DRender(true);
	        matrixstack.translate(x, y, z);
	        matrixstack.multiply(new Quaternion(new Vec3f(0, -1, 0), 0, true));
	        matrixstack.translate(-x, -y, -z);

	        Box bb = new Box(x - entity.getWidth() + 0.25, y, z - entity.getWidth() + 0.25, x + entity.getWidth() - 0.25, y + entity.getHeight() + 0.1, z + entity.getWidth() - 0.25);
	        if (entity instanceof ItemEntity)
	            bb = new Box(x - 0.15, y + 0.1f, z - 0.15, x + 0.15, y + 0.5, z + 0.15);


	        drawFilledBox(matrixstack, bb, new Color(color.getRed(), color.getGreen(), color.getBlue(), 130), true);
	        RenderSystem.lineWidth(1.5f);

	        drawOutlineBox(matrixstack, bb, color, true);

	        end3DRender();
	        matrixstack.translate(x, y, z);
	        matrixstack.multiply(new Quaternion(new Vec3f(0, 1, 0), 0, true));
	        matrixstack.translate(-x, -y, -z);
	    }
	    
	    public static void drawNametag(MatrixStack matrixstack, Entity entity, double x, double y, double z, Color color) {
	        setup3DRender(true);
	        matrixstack.translate(x, y, z);
	        matrixstack.multiply(new Quaternion(new Vec3f(0, -1, 0), 0, true));
	        matrixstack.translate(-x, -y, -z);

	        DrawableHelper.fill(matrixstack, 10, 10, 100, 100, -1);
	        RenderSystem.lineWidth(1.5f);

	        end3DRender();
	        matrixstack.translate(x, y, z);
	        matrixstack.multiply(new Quaternion(new Vec3f(0, 1, 0), 0, true));
	        matrixstack.translate(-x, -y, -z);
	    }
	    
	    public static void setup2DRender(boolean disableDepth) {
	        RenderSystem.enableBlend();
	        RenderSystem.disableTexture();
	        RenderSystem.defaultBlendFunc();
	        if (disableDepth)
	            RenderSystem.disableDepthTest();
	    }

	    public static void end2DRender() {
	        RenderSystem.disableBlend();
	        RenderSystem.enableTexture();
	        RenderSystem.enableDepthTest();
	    }

	    public static void setup2DProjection() {
//	        Matrix4x4 ortho = Matrix4x4.ortho2DMatrix(0, getScaledWidth(), getScaledHeight(), 0, -0.1f, 1000.f);
	    }
	    
	    public static void bindTexture(Identifier identifier) {
	        RenderSystem.setShaderTexture(0, identifier);
	    }
	    
	    public static void shaderColor(int hex) {
	        float alpha = (hex >> 24 & 0xFF) / 255.0F;
	        float red = (hex >> 16 & 0xFF) / 255.0F;
	        float green = (hex >> 8 & 0xFF) / 255.0F;
	        float blue = (hex & 0xFF) / 255.0F;
	        RenderSystem.setShaderColor(red, green, blue, alpha);
	    }
	    
	    public static int getPercentColor(float percent) {
	        if (percent <= 15)
	            return new Color(255, 0, 0).getRGB();
	        else if (percent <= 25)
	            return new Color(255, 75, 92).getRGB();
	        else if (percent <= 50)
	            return new Color(255, 123, 17).getRGB();
	        else if (percent <= 75)
	            return new Color(255, 234, 0).getRGB();
	        return new Color(0, 255, 0).getRGB();
	    }
	    
	    public static void fill(MatrixStack matrixStack, double x1, double y1, double x2, double y2, int color) {
	        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
	        double j;
	        if (x1 < x2) {
	            j = x1;
	            x1 = x2;
	            x2 = j;
	        }

	        if (y1 < y2) {
	            j = y1;
	            y1 = y2;
	            y2 = j;
	        }

	        float f = (float)(color >> 24 & 255) / 255.0F;
	        float g = (float)(color >> 16 & 255) / 255.0F;
	        float h = (float)(color >> 8 & 255) / 255.0F;
	        float k = (float)(color & 255) / 255.0F;
	        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
	        RenderSystem.enableBlend();
	        RenderSystem.disableTexture();
	        RenderSystem.defaultBlendFunc();
	        RenderSystem.setShader(GameRenderer::getPositionColorShader);
	        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
	        bufferBuilder.vertex(matrix, (float)x1, (float)y2, 0.0F).color(g, h, k, f).next();
	        bufferBuilder.vertex(matrix, (float)x2, (float)y2, 0.0F).color(g, h, k, f).next();
	        bufferBuilder.vertex(matrix, (float)x2, (float)y1, 0.0F).color(g, h, k, f).next();
	        bufferBuilder.vertex(matrix, (float)x1, (float)y1, 0.0F).color(g, h, k, f).next();
	        BufferRenderer.drawWithShader(bufferBuilder.end());
	        RenderSystem.enableTexture();
	        RenderSystem.disableBlend();
	    }
	    
	    public static void fillWithScale(MatrixStack matrixStack, double x, double y, double width, double height, Vec2f scale, float orgin, int color) {
	        matrixStack.push();
	    	matrixStack.translate(x, y, 0);
	        matrixStack.scale(scale.x, scale.y, 0.0f);
	        fill(matrixStack, 0, 0, width, height, color);
	        matrixStack.pop();
	    }
	    
	    public static void fill(MatrixStack matrixStack, double x1, double y1, double x2, double y2, Color color) {
	        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
	        double j;
	        if (x1 < x2) {
	            j = x1;
	            x1 = x2;
	            x2 = j;
	        }

	        if (y1 < y2) {
	            j = y1;
	            y1 = y2;
	            y2 = j;
	        }

	        float f = (float)color.getRed() / 255.0F;
	        float g = (float)color.getGreen() / 255.0F;
	        float h = (float)color.getBlue() / 255.0F;
	        float k = (float)color.getAlpha() / 255.0F;
	        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
	        RenderSystem.enableBlend();
	        RenderSystem.disableTexture();
	        RenderSystem.defaultBlendFunc();
	        RenderSystem.setShader(GameRenderer::getPositionColorShader);
	        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
	        bufferBuilder.vertex(matrix, (float)x1, (float)y2, 0.0F).color(g, h, k, f).next();
	        bufferBuilder.vertex(matrix, (float)x2, (float)y2, 0.0F).color(g, h, k, f).next();
	        bufferBuilder.vertex(matrix, (float)x2, (float)y1, 0.0F).color(g, h, k, f).next();
	        bufferBuilder.vertex(matrix, (float)x1, (float)y1, 0.0F).color(g, h, k, f).next();
	        BufferRenderer.drawWithShader(bufferBuilder.end());
	        RenderSystem.enableTexture();
	        RenderSystem.disableBlend();
	    }
	    
	    public static void gradientFill(MatrixStack matrixStack, double x1, double y1, double x2, double y2, int color1, int color2) {
	        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
	        double j;
	        if (x1 < x2) {
	            j = x1;
	            x1 = x2;
	            x2 = j;
	        }

	        if (y1 < y2) {
	            j = y1;
	            y1 = y2;
	            y2 = j;
	        }

	        float f1 = (float)(color1 >> 24 & 255) / 255.0F;
	        float g1 = (float)(color1 >> 16 & 255) / 255.0F;
	        float h1 = (float)(color1 >> 8 & 255) / 255.0F;
	        float k1 = (float)(color1 & 255) / 255.0F;
	        
	        float f2 = (float)(color2 >> 24 & 255) / 255.0F;
	        float g2 = (float)(color2 >> 16 & 255) / 255.0F;
	        float h2 = (float)(color2 >> 8 & 255) / 255.0F;
	        float k2 = (float)(color2 & 255) / 255.0F;
	        
	        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
	        RenderSystem.enableBlend();
	        RenderSystem.disableTexture();
	        RenderSystem.defaultBlendFunc();
	        RenderSystem.setShader(GameRenderer::getPositionColorShader);
	        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
	        bufferBuilder.vertex(matrix, (float)x1, (float)y2, 0.0F).color(g1, h1, k1, f1).next();
	        bufferBuilder.vertex(matrix, (float)x2, (float)y2, 0.0F).color(g1, h1, k1, f1).next();
	        bufferBuilder.vertex(matrix, (float)x2, (float)y1, 0.0F).color(g2, h2, k2, f2).next();
	        bufferBuilder.vertex(matrix, (float)x1, (float)y1, 0.0F).color(g2, h2, k2, f2).next();
	        BufferRenderer.drawWithShader(bufferBuilder.end());
	        RenderSystem.enableTexture();
	        RenderSystem.disableBlend();
	    }
	    
	    public static void sideGradientFill(MatrixStack matrixStack, double x1, double y1, double x2, double y2, int color1, int color2, boolean dir) {
	        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
	        double j;
	        if (x1 < x2) {
	            j = x1;
	            x1 = x2;
	            x2 = j;
	        }

	        if (y1 < y2) {
	            j = y1;
	            y1 = y2;
	            y2 = j;
	        }

	        float f1 = (float)(color1 >> 24 & 255) / 255.0F;
	        float g1 = (float)(color1 >> 16 & 255) / 255.0F;
	        float h1 = (float)(color1 >> 8 & 255) / 255.0F;
	        float k1 = (float)(color1 & 255) / 255.0F;
	        
	        float f2 = (float)(color2 >> 24 & 255) / 255.0F;
	        float g2 = (float)(color2 >> 16 & 255) / 255.0F;
	        float h2 = (float)(color2 >> 8 & 255) / 255.0F;
	        float k2 = (float)(color2 & 255) / 255.0F;
	        
	        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
	        RenderSystem.enableBlend();
	        RenderSystem.disableTexture();
	        RenderSystem.defaultBlendFunc();
	        RenderSystem.setShader(GameRenderer::getPositionColorShader);
	        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
	        bufferBuilder.vertex(matrix, (float)x1, (float)y2, 0.0F).color(!dir ? g2 : g1, !dir ? h2 : h1, !dir ? k2 : k1, !dir ? f2 : f1).next();
	        bufferBuilder.vertex(matrix, (float)x2, (float)y2, 0.0F).color(dir ? g2 : g1, dir ? h2 : h1, dir ? k2 : k1, dir ? f2 : f1).next();
	        bufferBuilder.vertex(matrix, (float)x2, (float)y1, 0.0F).color(dir ? g2 : g1, dir ? h2 : h1, dir ? k2 : k1, dir ? f2 : f1).next();
	        bufferBuilder.vertex(matrix, (float)x1, (float)y1, 0.0F).color(!dir ? g2 : g1, !dir ? h2 : h1, !dir ? k2 : k1, !dir ? f2 : f1).next();
	        BufferRenderer.drawWithShader(bufferBuilder.end());
	        RenderSystem.enableTexture();
	        RenderSystem.disableBlend();
	    }
    
	    public static void drawFace(MatrixStack matrixStack, float x, float y, int renderScale, Identifier id) {
	        try {
	            bindTexture(id);
	            drawTexture(matrixStack, x, y, 8 * renderScale, 8 * renderScale, 8 * renderScale, 8 * renderScale, 8 * renderScale, 8 * renderScale, 64 * renderScale, 64 * renderScale);
	            drawTexture(matrixStack, x, y, 8 * renderScale, 8 * renderScale, 40 * renderScale, 8 * renderScale, 8 * renderScale, 8 * renderScale, 64 * renderScale, 64 * renderScale);
	        }catch (Exception e){}
	    }
	    
	    public static void drawTexture(MatrixStack matrices, float x, float y, float u, float v, float width, float height, int textureWidth, int textureHeight) {
			RenderSystem.enableBlend();
	    	drawTexture(matrices, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
			RenderSystem.disableBlend();
	    }

	    public static void drawTexture(MatrixStack matrices, float x, float y, float width, float height, float u, float v, float regionWidth, float regionHeight, float textureWidth, float textureHeight) {
	        drawTexture(matrices, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
	    }

	    public static void drawTexture(MatrixStack matrices, float x0, float y0, float x1, float y1, int z, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight) {
	        drawTexturedQuad(matrices.peek().getPositionMatrix(), x0, y0, x1, y1, z, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight);
	    }

	    public static void drawTexturedQuad(Matrix4f matrices, float x0, float x1, float y0, float y1, float z, float u0, float u1, float v0, float v1) {
	        RenderSystem.setShader(GameRenderer::getPositionTexShader);
	        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
	        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
	        bufferBuilder.vertex(matrices, (float)x0, (float)y1, (float)z).texture(u0, v1).next();
	        bufferBuilder.vertex(matrices, (float)x1, (float)y1, (float)z).texture(u1, v1).next();
	        bufferBuilder.vertex(matrices, (float)x1, (float)y0, (float)z).texture(u1, v0).next();
	        bufferBuilder.vertex(matrices, (float)x0, (float)y0, (float)z).texture(u0, v0).next();
	        BufferRenderer.drawWithShader(bufferBuilder.end());
	    }
	    
	    public static void drawItem(ItemStack stack, float xPosition, float yPosition) {
	        drawItem(stack, xPosition, yPosition, 1);
	    }
	    public static void drawItem(ItemStack stack, float xPosition, float yPosition, float scale) {
	        String amountText = stack.getCount() != 1 ? stack.getCount() + "" : "";
	        IItemRenderer iItemRenderer = (IItemRenderer) mc.getItemRenderer();
	        iItemRenderer.renderItemIntoGUI(stack, xPosition, yPosition, scale);
	        renderGuiItemOverlay(mc.textRenderer, stack, xPosition - 0.5f, yPosition + 1, scale, amountText);
	    }
	    public static void renderGuiItemOverlay(TextRenderer renderer, ItemStack stack, float x, float y, float scale, @Nullable String countLabel) {
	        if (!stack.isEmpty()) {
	            MatrixStack matrixStack = new MatrixStack();
	            if (stack.getCount() != 1 || countLabel != null) {
	                String string = countLabel == null ? String.valueOf(stack.getCount()) : countLabel;
	                matrixStack.translate(0.0D, 0.0D, (double)(mc.getItemRenderer().zOffset + 200.0F));
	                VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
	                renderer.draw(string, (float)(x + 19 - 2 - renderer.getWidth(string)), (float)(y + 6 + 3), 16777215, true, matrixStack.peek().getPositionMatrix(), immediate, false, 0, 15728880);
	                immediate.draw();
	            }

	            if (stack.isItemBarVisible()) {
	                RenderSystem.disableDepthTest();
	                RenderSystem.disableTexture();
	                RenderSystem.disableBlend();
	                int i = stack.getItemBarStep();
	                int j = stack.getItemBarColor();
	                fill(matrixStack, x + 2, y + 13, x + 2 + 13, y + 13 + 2, 0xff000000);
	                fill(matrixStack, x + 2, y + 13, x + 2 + i, y + 13 + 1, new Color(j >> 16 & 255, j >> 8 & 255, j & 255, 255).getRGB());
	                RenderSystem.enableBlend();
	                RenderSystem.enableTexture();
	                RenderSystem.enableDepthTest();
	            }

	            ClientPlayerEntity clientPlayerEntity = mc.player;
	            float f = clientPlayerEntity == null ? 0.0F : clientPlayerEntity.getItemCooldownManager().getCooldownProgress(stack.getItem(), MinecraftClient.getInstance().getTickDelta());
	            if (f > 0.0F) {
	                RenderSystem.disableDepthTest();
	                RenderSystem.disableTexture();
	                RenderSystem.enableBlend();
	                RenderSystem.defaultBlendFunc();
	                Tessellator tessellator2 = Tessellator.getInstance();
	                BufferBuilder bufferBuilder2 = tessellator2.getBuffer();
	                renderGuiQuad(bufferBuilder2, x, y + MathHelper.floor(16.0F * (1.0F - f)), 16, MathHelper.ceil(16.0F * f), 255, 255, 255, 127);
	                RenderSystem.enableTexture();
	                RenderSystem.enableDepthTest();
	            }

	        }
	    }
	    
	    private static void renderGuiQuad(BufferBuilder buffer, float x, float y, float width, float height, int red, int green, int blue, int alpha) {
	        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
	        buffer.vertex((double) (x + 0), (double) (y + 0), 0.0D).color(red, green, blue, alpha).next();
	        buffer.vertex((double) (x + 0), (double) (y + height), 0.0D).color(red, green, blue, alpha).next();
	        buffer.vertex((double) (x + width), (double) (y + height), 0.0D).color(red, green, blue, alpha).next();
	        buffer.vertex((double) (x + width), (double) (y + 0), 0.0D).color(red, green, blue, alpha).next();
	        Tessellator.getInstance().draw();
	    }
	    
	    public static Vec3d to2D(Vec3d worldPos, MatrixStack matrixStack) {
	        Vec3d bound = getRenderPosition(worldPos, matrixStack);
	        Vec3d twoD = to2D(bound.x, bound.y, bound.z);
	        return new Vec3d(twoD.x, twoD.y, twoD.z);
	    }
	    
	    private static Vec3d to2D(double x, double y, double z) {
	        int displayHeight = mc.getWindow().getHeight();
	        Vector3D screenCoords = new Vector3D();
	        int[] viewport = new int[4];
	        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
	        Matrix4x4 matrix4x4Proj = Matrix4x4.copyFromColumnMajor(RenderSystem.getProjectionMatrix());
	        Matrix4x4 matrix4x4Model = Matrix4x4.copyFromColumnMajor(RenderSystem.getModelViewMatrix());
	        matrix4x4Proj.mul(matrix4x4Model).project((float) x, (float) y, (float) z, viewport, screenCoords);

	        return new Vec3d(screenCoords.x / getScaleFactor(), (displayHeight - screenCoords.y) / getScaleFactor(), screenCoords.z);
	    }
	    
	    public static Vec3d getPos(Entity entity, float yOffset, float partialTicks, MatrixStack matrixStack) {
	        Vec3d bound = getEntityRenderPosition(entity, partialTicks).add(0, yOffset, 0);
	        Vector4f vector4f = new Vector4f((float)bound.x, (float)bound.y, (float)bound.z, 1.f);
	        vector4f.transform(matrixStack.peek().getPositionMatrix());
	        Vec3d twoD = to2D(vector4f.getX(), vector4f.getY(), vector4f.getZ());
	        return new Vec3d(twoD.x, twoD.y, twoD.z);
	    }

		public static Vec3d getRenderPosition(double x, double y, double z) {
        double minX = x - mc.getEntityRenderDispatcher().camera.getPos().x;
        double minY = y - mc.getEntityRenderDispatcher().camera.getPos().y;
        double minZ = z - mc.getEntityRenderDispatcher().camera.getPos().z;
        return new Vec3d(minX, minY, minZ);
    }
		
		public static void renderOutlineIntern(Vec3d start, Vec3d dimensions, MatrixStack stack, BufferBuilder buffer) {
	        Camera c = mc.gameRenderer.getCamera();
	        Vec3d camPos = c.getPos();
	        start = start.subtract(camPos);
	        Vec3d end = start.add(dimensions);
	        Matrix4f matrix = stack.peek().getPositionMatrix();
	        float x1 = (float) start.x;
	        float y1 = (float) start.y;
	        float z1 = (float) start.z;
	        float x2 = (float) end.x;
	        float y2 = (float) end.y;
	        float z2 = (float) end.z;

	        buffer.vertex(matrix, x1, y1, z1).next();
	        buffer.vertex(matrix, x1, y1, z2).next();
	        buffer.vertex(matrix, x1, y1, z2).next();
	        buffer.vertex(matrix, x2, y1, z2).next();
	        buffer.vertex(matrix, x2, y1, z2).next();
	        buffer.vertex(matrix, x2, y1, z1).next();
	        buffer.vertex(matrix, x2, y1, z1).next();
	        buffer.vertex(matrix, x1, y1, z1).next();

	        buffer.vertex(matrix, x1, y2, z1).next();
	        buffer.vertex(matrix, x1, y2, z2).next();
	        buffer.vertex(matrix, x1, y2, z2).next();
	        buffer.vertex(matrix, x2, y2, z2).next();
	        buffer.vertex(matrix, x2, y2, z2).next();
	        buffer.vertex(matrix, x2, y2, z1).next();
	        buffer.vertex(matrix, x2, y2, z1).next();
	        buffer.vertex(matrix, x1, y2, z1).next();

	        buffer.vertex(matrix, x1, y1, z1).next();
	        buffer.vertex(matrix, x1, y2, z1).next();

	        buffer.vertex(matrix, x2, y1, z1).next();
	        buffer.vertex(matrix, x2, y2, z1).next();

	        buffer.vertex(matrix, x2, y1, z2).next();
	        buffer.vertex(matrix, x2, y2, z2).next();

	        buffer.vertex(matrix, x1, y1, z2).next();
	        buffer.vertex(matrix, x1, y2, z2).next();
	    }
		
		public static BufferBuilder renderPrepare(Color color) {
	        float red = color.getRed() / 255f;
	        float green = color.getGreen() / 255f;
	        float blue = color.getBlue() / 255f;
	        float alpha = color.getAlpha() / 255f;
	        RenderSystem.setShader(GameRenderer::getPositionShader);
	        GL11.glDepthFunc(GL11.GL_ALWAYS);
	        RenderSystem.setShaderColor(red, green, blue, alpha);
	        RenderSystem.lineWidth(2f);
	        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
	        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES,
	                VertexFormats.POSITION);
	        return buffer;
	    }
		
		public static void drawFilledBox(MatrixStack matrixStack, Box bb, int color, boolean draw) {
	        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
	        Color color1 = ColorUtils.getColor(color);

	        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
	        if (draw)
	        	bufferBuilder.begin(VertexFormat.DrawMode.QUADS/*QUADS*/, VertexFormats.POSITION_COLOR);
	        float minX = (float)bb.minX;
	        float minY = (float)bb.minY;
	        float minZ = (float)bb.minZ;
	        float maxX = (float)bb.maxX;
	        float maxY = (float)bb.maxY;
	        float maxZ = (float)bb.maxZ;

	        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

	        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

	        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

	        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

	        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

	        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        if (draw) {
		        BufferRenderer.drawWithShader(bufferBuilder.end());
	        }
	    }
		
		public static void drawFilledBox(MatrixStack matrixStack, Box bb, int color) {
	    	drawFilledBox(matrixStack, bb, color, true);
	    }
		
		public static void drawOutlineBox(MatrixStack matrixStack, Box bb, int color) {
	    	drawOutlineBox(matrixStack, bb, color, true);
	    }

	    public static void drawOutlineBox(MatrixStack matrixStack, Box bb, int color, boolean draw) {
	        Color color1 = ColorUtils.getColor(color);
	        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

	        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
	        if (draw)
	        	bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES/*LINES*/, VertexFormats.POSITION_COLOR);

	        VoxelShape shape = VoxelShapes.cuboid(bb);
	        shape.forEachEdge((x1, y1, z1, x2, y2, z2) -> {
	            bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z1).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	            bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z2).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        });
	        if (draw) {
		        BufferRenderer.drawWithShader(bufferBuilder.end());
	        }
	    }

	    //you can call renderOutlineIntern multiple times to save performance
	    public static void renderOutline(Vec3d start, Vec3d dimensions, Color color, MatrixStack stack) {
	        RenderSystem.defaultBlendFunc();
	        RenderSystem.enableBlend();
	        BufferBuilder buffer = renderPrepare(color);

	        renderOutlineIntern(start, dimensions, stack, buffer);

	        BufferRenderer.drawWithShader(buffer.end());
	        GL11.glDepthFunc(GL11.GL_LEQUAL);
	        RenderSystem.disableBlend();
	    }

		public static void drawBox(MatrixStack matrixstack, Box bb, int color) {
        setup3DRender(true);

        drawFilledBox(matrixstack, bb, color & 0x70ffffff);
        RenderSystem.lineWidth(1);
        drawOutlineBox(matrixstack, bb, color);

        end3DRender();
    }
		
		public static void line(Vec3d start, Vec3d end, Color color, MatrixStack matrices) {
	        float red = color.getRed() / 255f;
	        float green = color.getGreen() / 255f;
	        float blue = color.getBlue() / 255f;
	        float alpha = color.getAlpha() / 255f;
	        Camera c = mc.gameRenderer.getCamera();
	        Vec3d camPos = c.getPos();
	        start = start.subtract(camPos);
	        end = end.subtract(camPos);
	        Matrix4f matrix = matrices.peek().getPositionMatrix();
	        float x1 = (float) start.x;
	        float y1 = (float) start.y;
	        float z1 = (float) start.z;
	        float x2 = (float) end.x;
	        float y2 = (float) end.y;
	        float z2 = (float) end.z;
	        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
	        RenderSystem.setShader(GameRenderer::getPositionColorShader);
	        GL11.glDepthFunc(GL11.GL_ALWAYS);
	        
	        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
	        RenderSystem.defaultBlendFunc();
	        RenderSystem.enableBlend();
	        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES,
	                VertexFormats.POSITION_COLOR);
	        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
	        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();

	        BufferRenderer.drawWithShader(buffer.end());
	        GL11.glDepthFunc(GL11.GL_LEQUAL);
	        RenderSystem.disableBlend();
	    }
		
		public static void drawCircle(MatrixStack matrices, Vec3d pos, float partialTicks, double rad, double height, int color) {
	        double lastX = 0;
			double lastZ = rad;
			for (int angle = 0; angle <= 360; angle += 6) {
				float cos = MathHelper.cos((float) Math.toRadians(angle));
				float sin = MathHelper.sin((float) Math.toRadians(angle));

				double x = rad * sin;
				double z = rad * cos;
				drawLine(
						pos.x + lastX, pos.y, pos.z + lastZ,
						pos.x + x, pos.y, pos.z + z,
						LineColor.single(color), 2);

				lastX = x;
				lastZ = z;
			}
	    }
		
		public static void drawAuraESP(MatrixStack matrices, Vec3d pos, float partialTicks, double rad, double height, int color) {
	        float lastX = 0;
			float lastZ = (float) rad;
			for (int angle = 0; angle <= 360; angle += 6) {
				float cos = MathHelper.cos((float) Math.toRadians(angle));
				float sin = MathHelper.sin((float) Math.toRadians(angle));

				float x = (float) (rad * sin);
				float z = (float) (rad * cos);
				
	            Tessellator tessellator = Tessellator.getInstance();
				BufferBuilder buffer = tessellator.getBuffer();

				RenderSystem.enableBlend();
				RenderSystem.disableDepthTest();
				RenderSystem.disableCull();
				RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
				RenderSystem.lineWidth(1);

				buffer.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);
				Vertexer.vertexTri(matrices, buffer, (float)pos.x + lastX, (float)pos.y, (float)pos.z + lastZ, (float)pos.x + x, (float)pos.y, (float)pos.z + z, (float)pos.x + lastX + 12, (float)pos.y + 12, (float)pos.z + lastZ + 12, Color.WHITE);
				tessellator.draw();

				RenderSystem.enableCull();
				RenderSystem.enableDepthTest();

				lastX = x;
				lastZ = z;
			}
	    }
		
		public static void drawBoxFill(Box box, QuadColor color, Direction... excludeDirs) {
			if (!getFrustum().isVisible(box)) {
				return;
			}

			setup3DRender(true);

			MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();

			// Fill
			RenderSystem.setShader(GameRenderer::getPositionColorShader);

			buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
			Vertexer.vertexBoxQuads(matrices, buffer, Boxes.moveToZero(box), color, excludeDirs);
			tessellator.draw();

			end3DRender();
		}
		
		public static void drawBoxOutline(Box box, QuadColor color, float lineWidth, Direction... excludeDirs) {
			if (!getFrustum().isVisible(box)) {
				return;
			}

			setup3DRender(true);

			MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();

			// Outline
			RenderSystem.disableCull();
			RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
			RenderSystem.lineWidth(lineWidth);

			buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
			Vertexer.vertexBoxLines(matrices, buffer, Boxes.moveToZero(box), color, excludeDirs);
			tessellator.draw();

			RenderSystem.enableCull();

			end3DRender();
		}
		
		public static void drawLine(double x1, double y1, double z1, double x2, double y2, double z2, LineColor color, float width) {
			if (!isPointVisible(x1, y1, z1) && !isPointVisible(x2, y2, z2)) {
				return;
			}

			setup3DRender(true);

			MatrixStack matrices = matrixFrom(x1, y1, z1);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();

			// Line
			RenderSystem.disableDepthTest();
			RenderSystem.disableCull();
			RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
			RenderSystem.lineWidth(width);

			buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
			Vertexer.vertexLine(matrices, buffer, 0f, 0f, 0f, (float) (x2 - x1), (float) (y2 - y1), (float) (z2 - z1), color);
			tessellator.draw();

			RenderSystem.enableCull();
			RenderSystem.enableDepthTest();
			end3DRender();
		}
		
		public static MatrixStack matrixFrom(double x, double y, double z) {
			MatrixStack matrices = new MatrixStack();

			Camera camera = mc.gameRenderer.getCamera();
			matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(camera.getPitch()));
			matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(camera.getYaw() + 180.0F));

			matrices.translate(x - camera.getPos().x, y - camera.getPos().y, z - camera.getPos().z);

			return matrices;
		}
		
		public static Frustum getFrustum() {
			return ((WorldRendererAccessor) mc.worldRenderer).getFrustum();
		}
		
		public static boolean isPointVisible(double x, double y, double z) {
			FrustramAccessor frustum = (FrustramAccessor) getFrustum();
			Vector4f[] frustumCoords = frustum.getHomogeneousCoordinates();
			Vector4f pos = new Vector4f((float) (x - frustum.getX()), (float) (y - frustum.getY()), (float) (z - frustum.getZ()), 1f);

			for (int i = 0; i < 6; ++i) {
				if (frustumCoords[i].dotProduct(pos) <= 0f) {
					return false;
				}
			}

			return true;
		}
		
		public static void fill(MatrixStack matrices, int x1, int y1, int x2, int y2, int colTop, int colBot, int colFill) {
			DrawableHelper.fill(matrices, x1, y1 + 1, x1 + 1, y2 - 1, colTop);
			DrawableHelper.fill(matrices, x1 + 1, y1, x2 - 1, y1 + 1, colTop);
			DrawableHelper.fill(matrices, x2 - 1, y1 + 1, x2, y2 - 1, colBot);
			DrawableHelper.fill(matrices, x1 + 1, y2 - 1, x2 - 1, y2, colBot);
			DrawableHelper.fill(matrices, x1 + 1, y1 + 1, x2 - 1, y2 - 1, colFill);
		}
		
		public static Vec3d center() {
			Vec3d pos = new Vec3d(0, 0, 1);

	        if (mc.options.getBobView().getValue()) {
	            MatrixStack bobViewMatrices = new MatrixStack();

	            bobView(bobViewMatrices);
	            bobViewMatrices.peek().getPositionMatrix().invert();

	            pos = ((IMatrix4f) (Object) bobViewMatrices.peek().getPositionMatrix()).mul(pos);
	        }
	        return new Vec3d(pos.x, -pos.y, pos.z)
	            .rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
	            .rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
	            .add(mc.gameRenderer.getCamera().getPos());
		}
		
		private static void bobView(MatrixStack matrices) {
	        Entity cameraEntity = MinecraftClient.getInstance().getCameraEntity();

	        if (cameraEntity instanceof PlayerEntity) {
	        	PlayerEntity playerEntity = (PlayerEntity)cameraEntity;
	            float f = MinecraftClient.getInstance().getTickDelta();
	            float g = playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed;
	            float h = -(playerEntity.horizontalSpeed + g * f);
	            float i = MathHelper.lerp(f, playerEntity.prevStrideDistance, playerEntity.strideDistance);

	            matrices.translate(-(MathHelper.sin(h * 3.1415927f) * i * 0.5), -(-Math.abs(MathHelper.cos(h * 3.1415927f) * i)), 0);
	            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.sin(h * 3.1415927f) * i * 3));
	            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(Math.abs(MathHelper.cos(h * 3.1415927f - 0.2f) * i) * 5));
	        }
	    }
		
		public static Vec3d getCrosshairVector() {

	        ClientPlayerEntity player = mc.player;

	        float f = 0.017453292F;
	        float pi = (float) Math.PI;

	        assert player != null;
	        float f1 = MathHelper.cos(-player.getYaw() * f - pi);
	        float f2 = MathHelper.sin(-player.getYaw() * f - pi);
	        float f3 = -MathHelper.cos(-player.getPitch() * f);
	        float f4 = MathHelper.sin(-player.getPitch() * f);

	        return new Vec3d(f2 * f3, f4, f1 * f3).add(mc.player.getX(), mc.player.getY() + 1.5, mc.player.getZ());
	    }
		
		public static Box getBoundingBox(BlockPos pos) {
	        try {
	            assert mc.world != null;
	            return mc.world.getBlockState(pos).getOutlineShape(mc.world, pos).getBoundingBox().offset(pos);
	        } catch(Exception e) {
	            return null;
	        }
	    }
		
		public static void fillAndBorder(MatrixStack matrixStack, float left, float top, float right, float bottom, int bcolor, int icolor, float f) {
	        fill(matrixStack, left + f, top + f, right - f, bottom - f, icolor);
	        fill(matrixStack, left, top, left + f, bottom, bcolor);
	        fill(matrixStack, left + f, top, right, top + f, bcolor);
	        fill(matrixStack, left + f, bottom - f, right, bottom, bcolor);
	        fill(matrixStack, right - f, top + f, right, bottom - f, bcolor);
	    }

		public static void applyRegionalRenderOffset(MatrixStack matrixStack)
	{
		Vec3d camPos = mc.getBlockEntityRenderDispatcher().camera.getPos();
		BlockPos blockPos = mc.getBlockEntityRenderDispatcher().camera.getBlockPos();
		
		int regionX = (blockPos.getX() >> 9) * 512;
		int regionZ = (blockPos.getZ() >> 9) * 512;
		
		matrixStack.translate(regionX - camPos.x, -camPos.y,
			regionZ - camPos.z);
	}

		public static void drawBoxBoth(BlockPos blockPos, QuadColor color, float lineWidth, Direction... excludeDirs) {
			drawBoxBoth(new Box(blockPos), color, lineWidth, excludeDirs);
		}

		public static void drawBoxBoth(Box box, QuadColor color, float lineWidth, Direction... excludeDirs) {
			QuadColor outlineColor = color.clone();
			outlineColor.overwriteAlpha(255);

			drawBoxBoth(box, color, outlineColor, lineWidth, excludeDirs);
		}

		public static void drawBoxBoth(BlockPos blockPos, QuadColor fillColor, QuadColor outlineColor, float lineWidth, Direction... excludeDirs) {
			drawBoxBoth(new Box(blockPos), fillColor, outlineColor, lineWidth, excludeDirs);
		}

		public static void drawBoxBoth(Box box, QuadColor fillColor, QuadColor outlineColor, float lineWidth, Direction... excludeDirs) {
			drawBoxFill(box, fillColor, excludeDirs);
			drawBoxOutline(box, outlineColor, lineWidth, excludeDirs);
		}

		public static void drawBoxFill(BlockPos blockPos, QuadColor color, Direction... excludeDirs) {
			drawBoxFill(new Box(blockPos), color, excludeDirs);
		}
		
		public static void drawFaceFill(BlockPos blockPos, QuadColor color, Direction face) {
			Box box = new Box(blockPos);
			if (!getFrustum().isVisible(box)) {
				return;
			}

			setup3DRender(true);

			MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();

			// Fill
			RenderSystem.setShader(GameRenderer::getPositionColorShader);

			buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
			Vertexer.vertexBoxQuadsFace(matrices, buffer, Boxes.moveToZero(box), color, face);
			tessellator.draw();

			end3DRender();
		}
		
		public static void drawFaceOutline(BlockPos blockPos, QuadColor color, int width, Direction face) {
			Box box = new Box(blockPos);
			if (!getFrustum().isVisible(box)) {
				return;
			}

			setup3DRender(true);

			MatrixStack matrices = matrixFrom(box.minX, box.minY, box.minZ);

			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();

			// Outline
			RenderSystem.disableCull();
			RenderSystem.setShader(GameRenderer::getRenderTypeLinesShader);
			RenderSystem.lineWidth(width);

			buffer.begin(VertexFormat.DrawMode.LINES, VertexFormats.LINES);
			Vertexer.vertexBoxLinesFace(matrices, buffer, Boxes.moveToZero(box), color, face);
			tessellator.draw();

			RenderSystem.enableCull();

			end3DRender();
		}

		public static void drawBoxOutline(BlockPos blockPos, QuadColor color, float lineWidth, Direction... excludeDirs) {
			drawBoxOutline(new Box(blockPos), color, lineWidth, excludeDirs);
		}

		public static void startScissor(int x, int y, int width, int height) {
			double factor = mc.getWindow().getScaleFactor();
	        RenderSystem.enableScissor((int) (x * factor), (int) ((mc.getWindow().getHeight() - (y * factor) - height * factor)), (int) (width * factor), (int) (height * factor));
		}
		
		public static void endScissor() {
			RenderSystem.disableScissor();
		}
		
		private HashMap<UUID, Identifier> playerSkins = Maps.newHashMap();
		private ArrayList<String> avatarsRequested = new ArrayList<>();
		
		private static final Identifier STEVE_SKIN = new Identifier("textures/entity/steve.png");
		
		public static void downloadPlayerSkin(UUID uuid) {
	        if (uuid == null || INSTANCE.avatarsRequested.contains(uuid.toString().replace("-", "")))
	            return;
	        
	        GameProfile gameProfile = new GameProfile(uuid, "skindl");//name doesn't matter because the url uses the uuid
	        
	        //using the handy dandy method Minecraft uses because it actually lets you do something with it rather than just automatically storing them
	        MinecraftClient.getInstance().getSkinProvider().loadSkin(gameProfile, (type, identifier, minecraftProfileTexture) -> {
	            if (type == MinecraftProfileTexture.Type.SKIN) {
	            	INSTANCE.playerSkins.put(uuid, identifier);
	            }

	        }, true);
	        INSTANCE.avatarsRequested.add(uuid.toString().replace("-", ""));
	    }
		
		public static Identifier getPlayerSkin(UUID uuid) {
	        if (INSTANCE.playerSkins.containsKey(uuid)) {
	        	
	            return INSTANCE.playerSkins.get(uuid);
	        } else {
	            downloadPlayerSkin(uuid);
	        }
	        return STEVE_SKIN;
	    }
		

		public static boolean isOnScreen2d(Vec3d pos) {
	        return pos != null && (pos.z > -1 && pos.z < 1);
	    }
		
		public static double distanceTo(double x1, double x2) {
			return x2 - x1;
		}
	
		public static double slowDownTo(double x1, double x2, float smooth) {
			return (x2 - x1) / smooth;
		}
	
	public static void vertex2f(float x, float y) {
		Tessellator.getInstance().getBuffer().vertex(x, y, 0);
	}
	
	// Taken from Coffee client (https://github.com/business-goose/Coffee/tree/master)
	// My rounded stuff looked retarded
	public static void renderRoundedQuadInternal(Matrix4f matrix, float cr, float cg, float cb, float ca, double fromX, double fromY, double toX, double toY, double rad, double samples) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.TRIANGLE_FAN, VertexFormats.POSITION_COLOR);

        double toX1 = toX - rad;
        double toY1 = toY - rad;
        double fromX1 = fromX + rad;
        double fromY1 = fromY + rad;
        double[][] map = new double[][]{new double[]{toX1, toY1}, new double[]{toX1, fromY1}, new double[]{fromX1, fromY1}, new double[]{fromX1, toY1}};
        for (int i = 0; i < 4; i++) {
            double[] current = map[i];
            for (double r = i * 90d; r < (360 / 4d + i * 90d); r += (90 / samples)) {
                float rad1 = (float) Math.toRadians(r);
                float sin = (float) (Math.sin(rad1) * rad);
                float cos = (float) (Math.cos(rad1) * rad);
                bufferBuilder.vertex(matrix, (float) current[0] + sin, (float) current[1] + cos, 0.0F).color(cr, cg, cb, ca).next();
            }
        }
        BufferRenderer.drawWithShader(bufferBuilder.end());
    }

	// Taken from Coffee client (https://github.com/business-goose/Coffee/tree/master)
	// My rounded stuff looked retarded
    public static void renderRoundedQuad(MatrixStack matrices, Color c, double fromX, double fromY, double toX, double toY, double rad, double samples) {
        int color = c.getRGB();
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        float f = (float) (color >> 24 & 255) / 255.0F;
        float g = (float) (color >> 16 & 255) / 255.0F;
        float h = (float) (color >> 8 & 255) / 255.0F;
        float k = (float) (color & 255) / 255.0F;
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        renderRoundedQuadInternal(matrix, g, h, k, f, fromX, fromY, toX, toY, rad, samples);

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
	
    public static ManagedShaderEffect blur = ShaderEffectManager.getInstance().manage(new Identifier("shaders/post/blur.json"));
    
	public static void blur(MatrixStack matrices, double x, double y, double y1, double x1) {
		preStencil();
		renderRoundedQuad(matrices, Color.white, x, y, x1, y1, 10 ,50);
		
		postStencil();
//		blur.setUniformValue("Progress", 1f);
//		blur.render(mc.getTickDelta());
		fill(matrices, 0, 0, 1000, 1000, -1);
		disableStencil();
	}
	
	private static int stencilBit = 0xff;
	
	public static void preStencil() {
		GL11.glEnable(GL11.GL_STENCIL_TEST);
		RenderSystem.colorMask(false, false, false, false);
		RenderSystem.depthMask(false);
		RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_REPLACE);
		RenderSystem.stencilFunc(GL11.GL_ALWAYS, stencilBit, stencilBit);
		RenderSystem.stencilMask(stencilBit);
		RenderSystem.clear(GL11.GL_STENCIL_BUFFER_BIT, false);
	}
	
	public static void postStencil() {
		RenderSystem.colorMask(true, true, true, true);
		RenderSystem.depthMask(true);
		RenderSystem.stencilMask(0x00);
		RenderSystem.stencilOp(GL11.GL_KEEP, GL11.GL_KEEP, GL11.GL_KEEP);
		RenderSystem.stencilFunc(GL11.GL_EQUAL, stencilBit, stencilBit);
	}
	
	public static void disableStencil() {
		GL11.glDisable(GL11.GL_STENCIL_TEST);
	}
	
	// Taken from Coffee client (https://github.com/business-goose/Coffee/tree/master)
	// I had no clue how to register a texture from a url
	static final HttpClient downloader = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(3)).build();
	public static void registerImageFromUrl(String url, Identifier id) {
		HttpRequest hr = HttpRequest.newBuilder().uri(URI.create("url")).header("User-Agent", "").timeout(Duration.ofSeconds(5)).build();
        downloader.sendAsync(hr, HttpResponse.BodyHandlers.ofByteArray()).thenAccept(httpResponse -> {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write(ImageIO.read(new ByteArrayInputStream(httpResponse.body())), "png", stream);
                byte[] bytes = stream.toByteArray();

                ByteBuffer data = BufferUtils.createByteBuffer(bytes.length).put(bytes);
                data.flip();
                NativeImage img = NativeImage.read(data);
                NativeImageBackedTexture texture = new NativeImageBackedTexture(img);

                mc.execute(() -> {
                    mc.getTextureManager().registerTexture(id, texture);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }); 
	}
	
	public static Framebuffer createFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.viewportWidth != mc.getWindow().getFramebufferWidth() || framebuffer.viewportHeight != mc.getWindow().getFramebufferHeight()) {
            if (framebuffer != null) {
                framebuffer.delete();
            }
            return new SimpleFramebuffer(mc.getWindow().getFramebufferWidth(), mc.getWindow().getFramebufferHeight(), true, MinecraftClient.IS_SYSTEM_MAC);
        }
        return framebuffer;
    }
	
	public static void drawCylinder(double x, double y, double z, float radius, float height, Color color) {
        double startAngle = Math.atan2(z, x) + Math.PI;

        BufferBuilder builder = Tessellator.getInstance().getBuffer();
        
        builder.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        
        // This draws all the segments back-to-front to avoid depth issues
        int segments = 64;
        double angleStep = Math.PI * 2.0 / (double) segments;
        for (int segment = 0; segment < segments / 2; segment++) {
            double previousAngleOffset = segment * angleStep;
            double currentAngleOffset = (segment + 1) * angleStep;

            // Draw the positive side of this offset
            double previousRotatedX = x + radius * Math.cos(startAngle + previousAngleOffset);
            double previousRotatedZ = z + radius * Math.sin(startAngle + previousAngleOffset);
            double rotatedX = x + radius * Math.cos(startAngle + currentAngleOffset);
            double rotatedZ = z + radius * Math.sin(startAngle + currentAngleOffset);
            
            builder.vertex(previousRotatedX, y + height, previousRotatedZ).color(color.getRGB()).next();
            builder.vertex(rotatedX, y + height, rotatedZ).color(color.getRGB()).next();
            builder.vertex(rotatedX, y, rotatedZ).color(color.getRGB()).next();
            builder.vertex(previousRotatedX, y, previousRotatedZ).color(color.getRGB()).next();

            // Draw the negative side of this offset
            previousRotatedX = x + radius * Math.cos(startAngle - previousAngleOffset);
            previousRotatedZ = z + radius * Math.sin(startAngle - previousAngleOffset);
            rotatedX = x + radius * Math.cos(startAngle - currentAngleOffset);
            rotatedZ = z + radius * Math.sin(startAngle - currentAngleOffset);

            builder.vertex(previousRotatedX, y + height, previousRotatedZ).color(color.getRGB()).next();
            builder.vertex(previousRotatedX, y, previousRotatedZ).color(color.getRGB()).next();
            builder.vertex(rotatedX, y, rotatedZ).color(color.getRGB()).next();
            builder.vertex(rotatedX, y + height, rotatedZ).color(color.getRGB()).next();
        }

        BufferRenderer.drawWithShader(builder.end());
    }
}
