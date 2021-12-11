package dev.hypnotic.hypnotic_client.ui.clickgui2.frame.button.settings;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.hypnotic.hypnotic_client.settings.Setting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ColorSetting;
import dev.hypnotic.hypnotic_client.ui.HypnoticScreen;
import dev.hypnotic.hypnotic_client.ui.clickgui2.frame.button.Button;
import dev.hypnotic.hypnotic_client.utils.render.RenderUtils;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class ColorBox extends Component {

	private ColorSetting colorSet = (ColorSetting)setting;
	private Button parent;
	private boolean lmDown = false, rmDown = false;
	public boolean open = false;
	public float h, s, v;
	int sx, sy, ex, ey;
	
	public ColorBox(int x, int y, Setting setting, Button parent) {
		super(x, y, setting, parent);
		this.parent = parent;
		this.setting = setting;
		this.colorSet = (ColorSetting)setting;
		colorSet.displayName = colorSet.name;
		h = colorSet.hue;
		s = colorSet.sat;
		v = colorSet.bri;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, int offset) {
		colorSet.displayName = colorSet.name;
		sx = parent.getX() + 5;
		sy = parent.getY() + 4 + offset + parent.getHeight() + 12;
		ex = parent.getX() + parent.getWidth() - 17;
		ey = parent.getY() + 4 + offset + parent.getHeight() + getHeight(parent.getWidth()) + 8;

		RenderUtils.fill(matrices, parent.getX() + 1, parent.getY() + offset + parent.getHeight(), parent.getX() + parent.getWidth() - 1, parent.getY() + offset + parent.getHeight() * (open ? 8.5 : 2), new Color(40, 40, 40, 255).getRGB());
		HypnoticScreen.fontSmall.drawWithShadow(matrices, colorSet.name, (int) sx, (int) sy - 12, -1);
		HypnoticScreen.fontSmall.drawWithShadow(matrices, "#" + colorSet.getHex().toUpperCase(), (int) sx + HypnoticScreen.fontSmall.getStringWidth(colorSet.name) + (open ? 12 : 2), (int) sy - 12, colorSet.getRGB());
		
		if (hovered((int)mouseX, (int)mouseY, sx + (int) HypnoticScreen.fontSmall.getStringWidth(colorSet.name + "#" + colorSet.getHex().toUpperCase()) + 4, sy - 12, (int) (sx + HypnoticScreen.fontSmall.getStringWidth(colorSet.name + "#" + colorSet.getHex().toUpperCase()) + 30), sy - 4) && !open) {
			RenderUtils.setup2DRender(true);
			RenderUtils.fill(matrices, mouseX, mouseY, mouseX + HypnoticScreen.fontSmall.getStringWidth("Right click me!") + 6, mouseY - 12, new Color(0, 0, 0, 200).getRGB());
			HypnoticScreen.fontSmall.drawWithShadow(matrices, "Right click me!", mouseX + 2, mouseY - 10, -1);
			RenderUtils.end2DRender();
			if (rmDown) open = true;
		}
		if (!open) {
			RenderUtils.fill(matrices, sx + HypnoticScreen.fontSmall.getStringWidth(colorSet.name + "#" + colorSet.getHex().toUpperCase()) + 4, sy - 12, sx + HypnoticScreen.fontSmall.getStringWidth(colorSet.name + "#" + colorSet.getHex().toUpperCase()) + 30, sy - 4, colorSet.getColor().getRGB());
			
			return;
		}
		RenderUtils.fill(matrices, sx + 3 + (int)HypnoticScreen.fontSmall.getStringWidth(colorSet.name + colorSet.getHex().toUpperCase()) + 17, sy - 4, sx + 27 + (int)HypnoticScreen.fontSmall.getStringWidth(colorSet.name + colorSet.getHex().toUpperCase()), sy - 12, new Color(0, 0, 0, 200).getRGB());
		RenderUtils.fill(matrices, sx, sy, ex, ey, -1);
		int satColor = MathHelper.hsvToRgb(colorSet.hue, 1f, 1f);
		int red = satColor >> 16 & 255;
		int green = satColor >> 8 & 255;
		int blue = satColor & 255;

		
		RenderSystem.disableBlend();
		RenderSystem.disableTexture();
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShader(GameRenderer::getPositionColorShader);

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		//Draw the color
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		bufferBuilder.vertex(ex, sy, 0).color(red, green, blue, 255).next();
		bufferBuilder.vertex(sx, sy, 0).color(red, green, blue, 0).next();
		bufferBuilder.vertex(sx, ey, 0).color(red, green, blue, 0).next();
		bufferBuilder.vertex(ex, ey, 0).color(red, green, blue, 255).next();
		tessellator.draw();

		//Draw the black stuff
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		bufferBuilder.vertex(ex, sy, 0).color(0, 0, 0, 0).next();
		bufferBuilder.vertex(sx, sy, 0).color(0, 0, 0, 0).next();
		bufferBuilder.vertex(sx, ey, 0).color(0, 0, 0, 255).next();
		bufferBuilder.vertex(ex, ey, 0).color(0, 0, 0, 255).next();
		tessellator.draw();

		RenderSystem.disableBlend();
		RenderSystem.enableTexture();
		
		//Set the color
		if (hovered(mouseX, mouseY, sx, sy, ex, ey) && lmDown) {
			colorSet.bri = 1f - 1f / ((float) (ey - sy) / (mouseY - sy));
			colorSet.sat = 1f / ((float) (ex - sx) / (mouseX - sx));
		}

		int briY = (int) (ey - (ey - sy) * colorSet.bri);
		int satX = (int) (sx + (ex - sx) * colorSet.sat);

		RenderUtils.fill(matrices, satX - 2, briY - 2, satX + 2, briY + 2, Color.GRAY.brighter().getRGB(), Color.WHITE.darker().getRGB(), Color.WHITE.getRGB());
		RenderUtils.fill(matrices, sx + 3 + HypnoticScreen.fontSmall.getStringWidth(colorSet.name), sy - 4, sx + 10 + HypnoticScreen.fontSmall.getStringWidth(colorSet.name), sy - 12, colorSet.getColor().getRGB());

		if (hovered((int)mouseX, (int)mouseY, sx + 3 + (int)HypnoticScreen.fontSmall.getStringWidth(colorSet.name), sy - 12, sx + 10 + (int)HypnoticScreen.fontSmall.getStringWidth(colorSet.name), sy - 4) && open) {
			RenderUtils.setup2DRender(true);
			RenderUtils.fill(matrices, mouseX, mouseY, mouseX + HypnoticScreen.fontSmall.getStringWidth("Right click me!") + 6, mouseY - 12, new Color(0, 0, 0, 200).getRGB());
			HypnoticScreen.fontSmall.drawWithShadow(matrices, "Right click me!", mouseX + 2, mouseY - 10, -1);
			RenderUtils.end2DRender();
			if (rmDown) open = false;
		}

		//Set hex codes
		if (hovered(mouseX, mouseY, sx + 3 + (int)HypnoticScreen.fontSmall.getStringWidth(colorSet.name + colorSet.getHex().toUpperCase()) + 17, sy - 12, sx + 27 + (int)HypnoticScreen.fontSmall.getStringWidth(colorSet.name + colorSet.getHex().toUpperCase()), sy - 4)) {
			RenderSystem.disableDepthTest();
			RenderSystem.depthFunc(GL11.GL_ALWAYS);
			RenderUtils.fill(matrices, mouseX, mouseY, mouseX + HypnoticScreen.fontSmall.getStringWidth("Sets the hex color to your current clipboard") + 6, mouseY - 12, new Color(0, 0, 0, 200).getRGB());
			HypnoticScreen.fontSmall.drawWithShadow(matrices, "Sets the hex color to your current clipboard", mouseX + 2, mouseY - 10, -1);
			RenderSystem.depthFunc(GL11.GL_LEQUAL);
			RenderSystem.enableDepthTest();
			if (lmDown && colorSet.getColor() != colorSet.hexToRgb(mc.keyboard.getClipboard())) {
				Color hexColor = colorSet.hexToRgb(mc.keyboard.getClipboard());
				float[] vals = colorSet.rgbToHsv(hexColor.getRed(), hexColor.getGreen(), hexColor.getBlue(), hexColor.getAlpha());
				colorSet.setHSV(vals[0], vals[1], vals[2]);
				h = vals[0];
				s = vals[1];
				v = vals[2];
			}
		}
		
		sx = ex + 5;
		ex = ex + 12;

		for (int i = sy; i < ey; i++) {
			float curHue = 1f / ((float) (ey - sy) / (i - sy));
			DrawableHelper.fill(matrices, sx, i, ex, i + 1, 0xff000000 | MathHelper.hsvToRgb(curHue, 1f, 1f));
		}

		if (hovered(mouseX, mouseY, sx, sy, ex, ey) && lmDown) {
			colorSet.hue = 1f / ((float) (ey - sy) / (mouseY - sy));
		}

		int hueY = (int) (sy + (ey - sy) * colorSet.hue);
		RenderUtils.fill(matrices, sx, hueY - 2, ex, hueY + 2, Color.GRAY.brighter().getRGB(), Color.WHITE.darker().getRGB(), Color.WHITE.getRGB());
	
		super.render(matrices, mouseX, mouseY, offset);
	}
	
	public int getHeight(int len) {
		return len - len / 4 - 1;
	}

	public boolean hovered(int mouseX, int mouseY, int x1, int y1, int x2, int y2) {
		return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) lmDown = true;
		if (button == 1) {
			rmDown = true;
		}
		super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void mouseReleased(int button) {
		if (button == 0) lmDown = false;
		if (button == 1) rmDown = false;
		super.mouseReleased(button);
	}
}
