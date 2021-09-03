package badgamesinc.hypnotic.ui.clickgui2.frame.button.settings;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.systems.RenderSystem;

import badgamesinc.hypnotic.config.SaveLoad;
import badgamesinc.hypnotic.settings.Setting;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.ui.clickgui2.frame.button.Button;
import badgamesinc.hypnotic.utils.ColorUtils;
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
	private Button parent;
	private boolean lmDown = false;
	
	public ColorBox(int x, int y, Setting setting, Button parent) {
		super(x, y, setting, parent);
		this.parent = parent;
		this.setting = setting;
		this.colorSet = (ColorSetting)setting;
		colorSet.displayName = colorSet.name;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, int offset) {
		colorSet.displayName = colorSet.name;
		int sx = parent.getX() + 5,
				sy = parent.getY() + offset + parent.getHeight() + 12,
				ex = parent.getX() + parent.getWidth() - 17,
				ey = parent.getY() + offset + parent.getHeight() + getHeight(parent.getWidth()) + 8;

		DrawableHelper.fill(matrices, parent.getX(), parent.getY() + offset + parent.getHeight(), parent.getX() + parent.getWidth(), parent.getY() + offset + parent.getHeight() * 7, ColorUtils.getClientColorInt());
		DrawableHelper.fill(matrices, parent.getX() + 1, parent.getY() + offset + parent.getHeight(), parent.getX() + parent.getWidth() - 1, parent.getY() + offset + parent.getHeight() * 7, new Color(40, 40, 40, 255).getRGB());
		
		//Render name
		RenderUtils.fill(matrices, sx + 3 + (int)FontManager.robotoSmall.getStringWidth(colorSet.name + colorSet.getHex().toUpperCase()) + 17, sy - 2, sx + 27 + (int)FontManager.robotoSmall.getStringWidth(colorSet.name + colorSet.getHex().toUpperCase()), sy - 9, new Color(0, 0, 0, 200).getRGB());
		DrawableHelper.fill(matrices, sx, sy, ex, ey, -1);
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
		FontManager.robotoSmall.drawWithShadow(matrices, colorSet.name, (int) sx, (int) sy - 9, -1);
		FontManager.robotoSmall.drawWithShadow(matrices, "#" + colorSet.getHex().toUpperCase(), (int) sx + FontManager.robotoSmall.getStringWidth(colorSet.name) + 12, (int) sy - 9, colorSet.getRGB());
		RenderUtils.fill(matrices, sx + 3 + FontManager.robotoSmall.getStringWidth(colorSet.name), sy - 2, sx + 10 + FontManager.robotoSmall.getStringWidth(colorSet.name), sy - 9, colorSet.getColor().getRGB());


		//Set hex codes
		if (hovered(mouseX, mouseY, sx + 3 + (int)FontManager.robotoSmall.getStringWidth(colorSet.name + colorSet.getHex().toUpperCase()) + 17, sy - 7, sx + 27 + (int)FontManager.robotoSmall.getStringWidth(colorSet.name + colorSet.getHex().toUpperCase()), sy - 2)) {
			RenderSystem.disableDepthTest();
			RenderSystem.depthFunc(GL11.GL_ALWAYS);
			RenderUtils.fill(matrices, mouseX, mouseY, mouseX + FontManager.robotoSmall.getStringWidth("Sets the hex color to your current clipboard") + 6, mouseY - 12, new Color(0, 0, 0, 200).getRGB());
			FontManager.robotoSmall.drawWithShadow(matrices, "Sets the hex color to your current clipboard", mouseX + 2, mouseY - 10, -1);
			RenderSystem.depthFunc(GL11.GL_LEQUAL);
			RenderSystem.enableDepthTest();
			if (lmDown && colorSet.getColor() != colorSet.hexToRgb(mc.keyboard.getClipboard())) {
				Color hexColor = colorSet.hexToRgb(mc.keyboard.getClipboard());
				float[] vals = colorSet.rgbToHsv(hexColor.getRed(), hexColor.getGreen(), hexColor.getBlue());
				colorSet.setHSV(vals[0], vals[1], vals[2]);
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
		super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void mouseReleased(int button) {
		if (button == 0) lmDown = false;
		super.mouseReleased(button);
	}
}
