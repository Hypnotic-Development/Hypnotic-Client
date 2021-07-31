package badgamesinc.hypnotic.utils;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class RenderUtils {

	// Made by lavaflowglow 11/19/2020 3:39 AM
	
		public static RenderUtils INSTANCE = new RenderUtils();
		static MinecraftClient mc = MinecraftClient.getInstance();
		public static boolean SetCustomYaw = false;
		public static float CustomYaw = 0;
		public int scaledWidth = 0;
		public int scaledHeight = 0;
		
		public void onTick() {
			scaledWidth = mc.getWindow().getScaledWidth();
			scaledHeight = mc.getWindow().getScaledHeight();
		}
		
		public static void setCustomYaw(float customYaw) {
			CustomYaw = customYaw;
			SetCustomYaw = true;
			mc.player.headYaw = customYaw;
		}
		
		public static void resetPlayerYaw() {
			SetCustomYaw = false;
		}
		
		public static float getCustomYaw() {
			
			return CustomYaw;
			
		}
		public static boolean SetCustomPitch = false;
		public static float CustomPitch = 0;
		
		public static void setCustomPitch(float customPitch) {
			CustomPitch = customPitch;
			SetCustomPitch = true;
		}
		
		public static void resetPlayerPitch() {
			SetCustomPitch = false;
		}
		
		public static float getCustomPitch() {
			
			return CustomPitch;
			
		}
		
		// Made by lavaflowglow 11/19/2020 3:39 AM
		
		public static WorldRenderer worldRenderer = mc.worldRenderer;
		public static Tessellator tessellator = Tessellator.getInstance();

		public static void fix(MatrixStack matrices) {
	        DrawableHelper.fill(matrices, 0, 0, 0, 0, -1);
	    }

		public static void drawTracerLine(double x, double y, double z, float red, float green, float blue, float alpha, float lineWdith) {
			GL11.glPushMatrix();
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glEnable(GL11.GL_LINE_SMOOTH);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        // GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glBlendFunc(770, 771);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glLineWidth(lineWdith);
	        GL11.glColor4f(red, green, blue, alpha);
	        GL11.glBegin(2);
	        GL11.glVertex3d(0.0D, 0.0D + mc.player.getStandingEyeHeight(), 0.0D);
	        GL11.glVertex3d(x, y, z);
	        GL11.glEnd();
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        GL11.glDisable(GL11.GL_LINE_SMOOTH);
	        GL11.glDisable(GL11.GL_BLEND);
	        // GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glPopMatrix();
		}
	    
	    public static int rainbow(final int delay) {
	        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	        rainbowState %= 360.0;
	        return Color.getHSBColor((float)(rainbowState / 360.0), 0.8f, 0.7f).getRGB();
	    }

	    public static void setColor(Color c) {
	        GL11.glColor4d(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
	    }
	    
	    public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
	        GL11.glPushMatrix();
	        cx *= 2.0f;
	        cy *= 2.0f;
	        float f = (float)(c >> 24 & 255) / 255.0f;
	        float f1 = (float)(c >> 16 & 255) / 255.0f;
	        float f2 = (float)(c >> 8 & 255) / 255.0f;
	        float f3 = (float)(c & 255) / 255.0f;
	        float theta = (float)(6.2831852 / (double)num_segments);
	        float p = (float)Math.cos(theta);
	        float s = (float)Math.sin(theta);
	        float x = r *= 2.0f;
	        float y = 0.0f;
	        RenderUtils.enableGL2D();
	        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
	        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
	        GL11.glBegin((int)2);
	        for (int ii = 0; ii < num_segments; ++ii) {
	            GL11.glVertex2f((float)(x + cx), (float)(y + cy));
	            float t = x;
	            x = p * x - s * y;
	            y = s * t + p * y;
	        }
	        GL11.glEnd();
	        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
	        RenderUtils.disableGL2D();
	        GlStateManager._clearColor(1.0f, 1.0f, 1.0f, 1.0f);
	        GL11.glPopMatrix();
	    }
	    
	    public static void enableGL2D() {
	        GL11.glDisable((int)2929);
	        GL11.glEnable((int)3042);
	        GL11.glDisable((int)3553);
	        GL11.glBlendFunc((int)770, (int)771);
	        GL11.glDepthMask((boolean)true);
	        GL11.glEnable((int)2848);
	        GL11.glHint((int)3154, (int)4354);
	        GL11.glHint((int)3155, (int)4354);
	    }

	    public static void disableGL2D() {
	        GL11.glEnable((int)3553);
	        GL11.glDisable((int)3042);
	        GL11.glEnable((int)2929);
	        GL11.glDisable((int)2848);
	        GL11.glHint((int)3154, (int)4352);
	        GL11.glHint((int)3155, (int)4352);
	    }
		
		
		public static void disableGL3D() {
	        GL11.glEnable(3553);
	        GL11.glEnable(2929);
	        GL11.glDisable(3042);
	        GL11.glEnable(3008);
	        GL11.glDepthMask(true);
	        GL11.glCullFace(1029);
	        GL11.glDisable(2848);
	        GL11.glHint(3154, 4352);
	        GL11.glHint(3155, 4352);
	    }
		
		public static void enableGL3D(final float lineWidth) {
	        GL11.glDisable(3008);
	        GL11.glEnable(3042);
	        GL11.glBlendFunc(770, 771);
	        GL11.glDisable(3553);
	        GL11.glDisable(2929);
	        GL11.glDepthMask(false);
	        GL11.glEnable(2884);
	      //  Shaders.disableLightmap();
	        //Shaders.disableFog();
	        GL11.glEnable(2848);
	        GL11.glHint(3154, 4354);
	        GL11.glHint(3155, 4354);
	        GL11.glLineWidth(lineWidth);
	    }
		
		public static void setColor(final int colorHex) {
	        final float alpha = (colorHex >> 24 & 0xFF) / 255.0f;
	        final float red = (colorHex >> 16 & 0xFF) / 255.0f;
	        final float green = (colorHex >> 8 & 0xFF) / 255.0f;
	        final float blue = (colorHex & 0xFF) / 255.0f;
	        GL11.glColor4f(red, green, blue, (alpha == 0.0f) ? 1.0f : alpha);
	    }
		
		/*
		 * 	NOTIFICATION
		 */
		
		/*public static void prepareScissorBox(float x, float y, float x2, float y2) {
		      ScaledResolution scale = new ScaledResolution(mc);
		      int factor = scale.getScaleFactor();
		      GL11.glScissor((int)(x * (float)factor), (int)(((float)scale.getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
		   }*/
		 
		 public static void drawFilledCircle(final int xx, final int yy, final float radius, final int color) {
		        int sections = 50;
		        double dAngle = 2 * Math.PI / sections;
		        float x, y;

		        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

		        GL11.glEnable(GL11.GL_BLEND);
		        GL11.glDisable(GL11.GL_TEXTURE_2D);
		        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		        GL11.glEnable(GL11.GL_LINE_SMOOTH);
		        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

		        for (int i = 0; i < sections; i++) {
		            x = (float) (radius * Math.sin((i * dAngle)));
		            y = (float) (radius * Math.cos((i * dAngle)));

		            GL11.glColor4f(new Color(color).getRed() / 255F, new Color(color).getGreen() / 255F, new Color(color).getBlue() / 255F, new Color(color).getAlpha() / 255F);
		            GL11.glVertex2f(xx + x, yy + y);
		        }

		        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		        GL11.glEnd();

		        GL11.glPopAttrib();
		    }
		 
		 
		 public static void drawRoundedRect(MatrixStack matrices, int left, int top, int right, int bottom, int smooth, Color color){
		        DrawableHelper.fill(matrices, left + smooth, top, right - smooth, bottom, color.getRGB());
		        DrawableHelper.fill(matrices, left, top + smooth, right, bottom - smooth, color.getRGB());
		        drawFilledCircle((int)left + smooth, (int)top + smooth, smooth, color.getRGB());
		        drawFilledCircle((int)right - smooth, (int)top + smooth, smooth, color.getRGB());
		        drawFilledCircle((int)right - smooth, (int)bottom - smooth, smooth, color.getRGB());
		        drawFilledCircle((int)left + smooth, (int)bottom - smooth, smooth, color.getRGB());

		    }
		 
		 public static void drawHLine(double x, double y, double x1, double y1, float width, int color) {
		        float var11 = (color >> 24 & 0xFF) / 255.0F;
		        float var6 = (color >> 16 & 0xFF) / 255.0F;
		        float var7 = (color >> 8 & 0xFF) / 255.0F;
		        float var8 = (color & 0xFF) / 255.0F;
		        GlStateManager._enableBlend();
		        GlStateManager._enableTexture();
		        GlStateManager._blendFuncSeparate(770, 771, 1, 0);
		        GlStateManager._clearColor(var6, var7, var8, var11);
		        GL11.glPushMatrix();
		        GL11.glLineWidth(width);
		        GL11.glBegin(GL11.GL_LINE_STRIP);
		        GL11.glVertex2d(x, y);
		        GL11.glVertex2d(x1, y1);
		        GL11.glEnd();

		        GL11.glLineWidth(1);


		        GL11.glPopMatrix();
		        GlStateManager._enableTexture();
		        GlStateManager._disableBlend();
		        GlStateManager._clearColor(1, 1, 1, 1);

		    }
		 
		 public static void drawBorderRect(double x, double y, double x1, double y1, int color, double lwidth) {
		        drawHLine(x, y, x1, y, (float) lwidth, color);
		        drawHLine(x1, y, x1, y1, (float) lwidth, color);
		        drawHLine(x, y1, x1, y1, (float) lwidth, color);
		        drawHLine(x, y1, x, y, (float) lwidth, color);
		    }
		 
		 /*public static void scissor(double x, double y, double width, double height) {
		        ScaledResolution sr = new ScaledResolution(mc);
		        final double scale = sr.getScaleFactor();

		        y = sr.getScaledHeight() - y;

		        x *= scale;
		        y *= scale;
		        width *= scale;
		        height *= scale;

		        GL11.glScissor((int) x, (int) (y - height), (int) width, (int) height);
		    }*/
		 

		 /*
		    GL11.glPushMatrix();
			GL11.glEnable(3089);
			RenderUtils.scissor(mouseX, mouseY, mouseY, partialTicks);
			GL11.glDisable(3089);
			GL11.glPopMatrix();
		  
		  */

		 public static void drawCenteredStringWithShadow(MatrixStack matrices, TextRenderer textRenderer, String text, int x, int y, int color) {
			 Screen.drawStringWithShadow(matrices, textRenderer, text, x - textRenderer.getWidth(text) / 2, y, color);
		 }
    
}
