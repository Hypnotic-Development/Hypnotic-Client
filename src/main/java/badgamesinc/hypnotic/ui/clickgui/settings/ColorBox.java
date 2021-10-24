package badgamesinc.hypnotic.ui.clickgui.settings;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import badgamesinc.hypnotic.settings.Setting;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.utils.font.FontManager;
import badgamesinc.hypnotic.utils.render.RenderUtils;
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
	private SettingsWindow parent;
	private boolean lmDown = false, rmDown = false;
	public boolean open = false;
	public float h, s, v;
	private int width, height;
	
	public ColorBox(int x, int y, SettingsWindow parent, Setting setting) {
		super(x, y, parent, setting);
		this.parent = parent;
		this.setting = setting;
		this.colorSet = (ColorSetting)setting;
		colorSet.displayName = colorSet.name;
		h = colorSet.hue;
		s = colorSet.sat;
		v = colorSet.bri;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		colorSet.displayName = colorSet.name;
		int sx = this.x - 10,
				sy = this.y + 12,
				ex = this.x + 100,
				ey = this.y + getHeight(120);

		FontManager.robotoMed.drawWithShadow(matrices, colorSet.name, (int) sx, (int) sy - 12, -1);
		FontManager.robotoMed.drawWithShadow(matrices, "#" + colorSet.getHex().toUpperCase(), (int) sx + FontManager.robotoMed.getStringWidth(colorSet.name) + 12, (int) sy - 12, colorSet.getRGB());
		if (hovered((int)mouseX, (int)mouseY, sx + 160, sy - 12, sx + 190, sy - 4) && rmDown) {
			RenderUtils.setup2DRender(true);
			RenderUtils.end2DRender();
			open = !open;
			rmDown = false;
		}
		RenderUtils.fill(matrices, sx + 190, sy - 12, sx + 160, sy - 4, colorSet.getColor().getRGB());
		RenderUtils.fill(matrices, sx + 3 + FontManager.robotoMed.getStringWidth(colorSet.name), sy - 3, sx + 10 + FontManager.robotoMed.getStringWidth(colorSet.name), sy - 11, colorSet.getColor().getRGB());
		if (hovered(mouseX, mouseY, sx + 3 + (int)FontManager.robotoMed.getStringWidth(colorSet.name + colorSet.getHex().toUpperCase()) + 17, sy - 12, sx + 27 + (int)FontManager.robotoMed.getStringWidth(colorSet.name + colorSet.getHex().toUpperCase()), sy - 4)) {
			RenderUtils.setup2DRender(true);
			RenderUtils.end2DRender();
			if (lmDown && colorSet.getColor() != colorSet.hexToRgb(mc.keyboard.getClipboard())) {
				Color hexColor = colorSet.hexToRgb(mc.keyboard.getClipboard());
				float[] vals = colorSet.rgbToHsv(hexColor.getRed(), hexColor.getGreen(), hexColor.getBlue(), hexColor.getAlpha());
				colorSet.setHSV(vals[0], vals[1], vals[2]);
				h = vals[0];
				s = vals[1];
				v = vals[2];
			}
		}
		
		if (!open) {
			return;
		}
		
		RenderUtils.fill(matrices, sx + 3 + (int)FontManager.robotoMed.getStringWidth(colorSet.name + colorSet.getHex().toUpperCase()) + 17, sy - 4, sx + 27 + (int)FontManager.robotoMed.getStringWidth(colorSet.name + colorSet.getHex().toUpperCase()), sy - 12, new Color(0, 0, 0, 200).getRGB());
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


		//Set hex codes
		if (hovered(mouseX, mouseY, sx + 3 + (int)FontManager.robotoMed.getStringWidth(colorSet.name + colorSet.getHex().toUpperCase()) + 17, sy - 12, sx + 27 + (int)FontManager.robotoMed.getStringWidth(colorSet.name + colorSet.getHex().toUpperCase()), sy - 4)) {
			RenderUtils.setup2DRender(true);
			RenderUtils.fill(matrices, mouseX, mouseY, mouseX + FontManager.robotoMed.getStringWidth("Sets the hex color to your current clipboard") + 6, mouseY - 12, new Color(0, 0, 0, 200).getRGB());
			FontManager.robotoMed.drawWithShadow(matrices, "Sets the hex color to your current clipboard", mouseX + 2, mouseY - 10, -1);
			RenderUtils.end2DRender();
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
		RenderUtils.drawCircle(matrices, 1 + satX / 2, briY / 2 + 1, 1.5f, 2, 80, Color.WHITE);
		super.render(matrices, mouseX, mouseY);
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
		if (button == 1) rmDown = true;
		super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		if (button == 0) lmDown = false;
		if (button == 1) rmDown = false;
		super.mouseReleased(mouseX, mouseY, button);
	}
}

