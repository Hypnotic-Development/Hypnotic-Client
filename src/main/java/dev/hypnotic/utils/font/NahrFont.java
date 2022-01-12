package dev.hypnotic.utils.font;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

/**
 * @author Nahr.
 * @author nuf
 */
//NahrFont in 2021????
public class NahrFont {

    private Font theFont;
    private Graphics2D theGraphics;
    private FontMetrics theMetrics;
    private float fontSize;
    private int startChar, endChar;
    private float[] xPos, yPos;
    public BufferedImage bufferedImage;
    public Identifier resourceLocation;
    private final Pattern patternControlCode = Pattern.compile("(?i)\\u00A7[0-9A-FK-OG]"), patternUnsupported = Pattern.compile("(?i)\\u00A7[L-O]");
    public boolean mcFont = false;
    
    public NahrFont(Object font, float size) {
        this(font, size, 0F);
    }

    public NahrFont(Object font) {
        this(font, 18F, 0F);
    }

    public NahrFont(Object font, float size, float spacing) {
        this.fontSize = size;
        this.startChar = 32;
        this.endChar = 255;
        this.xPos = new float[this.endChar - this.startChar];
        this.yPos = new float[this.endChar - this.startChar];
        setupGraphics2D();
        createFont(font, size);
    }
    
    public NahrFont(Object font, float size, float spacing, boolean mcFont) {
        this.fontSize = size;
        this.startChar = 32;
        this.endChar = 255;
        this.xPos = new float[this.endChar - this.startChar];
        this.yPos = new float[this.endChar - this.startChar];
        setupGraphics2D();
        createFont(font, size);
        this.mcFont = mcFont;
    }

    private final void setupGraphics2D() {
        this.bufferedImage = new BufferedImage(256, 256, 2);
        this.theGraphics = ((Graphics2D) this.bufferedImage.getGraphics());
        this.theGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    private final void createFont(Object font, float size) {
        try {
            if ((font instanceof Font))
                this.theFont = ((Font) font);
            else if ((font instanceof File))
                this.theFont = Font.createFont(0, (File) font).deriveFont(size);
            else if ((font instanceof InputStream))
                this.theFont = Font.createFont(0, (InputStream) font).deriveFont(size);
            else if ((font instanceof String)) {
                if (((String)font).toLowerCase().endsWith("ttf") || ((String)font).toLowerCase().endsWith("otf"))
                    this.theFont = Font.createFont(0, new File(Hypnotic.hypnoticDir + File.separator + "fonts", (String)font)).deriveFont(size);
                else
                    this.theFont = new Font((String) font, 0, Math.round(size));
            } else if ((font instanceof Identifier)) {
            	String name = ((Identifier)font).getPath();
            	if (name.toLowerCase().endsWith("ttf") || name.endsWith("otf"))
                    this.theFont = Font.createFont(0, new File(Hypnotic.hypnoticDir + File.separator + "fonts", name)).deriveFont(size);
                else
                    this.theFont = new Font(name, 0, Math.round(size));
            } else {
                this.theFont = new Font("Verdana", 0, Math.round(size));
            }
            this.theGraphics.setFont(this.theFont);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            this.theFont = new Font("Verdana", 0, Math.round(size));
            this.theGraphics.setFont(this.theFont);
        }
        this.theGraphics.setColor(new Color(255, 255, 255, 0));
        this.theGraphics.fillRect(0, 0, 256, 256);
        this.theGraphics.setColor(Color.white);
        this.theMetrics = this.theGraphics.getFontMetrics();

        float x = 5.0F;
        float y = 5.0F;
        for (int i = this.startChar; i < this.endChar; i++) {
            this.theGraphics.drawString(Character.toString((char) i), x, y + this.theMetrics.getAscent());
            this.xPos[(i - this.startChar)] = x;
            this.yPos[(i - this.startChar)] = (y - this.theMetrics.getMaxDescent());
            x += this.theMetrics.stringWidth(Character.toString((char) i)) + 2.0F;
            if (x >= 250 - this.theMetrics.getMaxAdvance()) {
                x = 5.0F;
                y += this.theMetrics.getMaxAscent() + this.theMetrics.getMaxDescent() + this.fontSize / 2.0F;
            }
        }
        String base64 = imageToBase64String(bufferedImage, "png");
        this.setResourceLocation(base64, theFont, size);
    }

    private String imageToBase64String(BufferedImage image, String type) {
        String ret = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, type, bos);
            byte[] bytes = bos.toByteArray();
            Base64 encoder = new Base64();
            ret = encoder.encodeAsString(bytes);
            ret = ret.replace(System.lineSeparator(), "");
        } catch (IOException e) {
            throw new RuntimeException();
        }
        return ret;
    }

    public void setResourceLocation(String base64, Object font, float size) {
        NativeImage image = readTexture(base64);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
            	/*
            	 * !!!!
            	 * 
            	 * 	if this gives you an error in eclipse, run "gradlew clean", then "gradlew eclipse", then "gradlew gensources" in the hypnotic root folder
            	 * 
            	 * !!!!
            	 */
                imgNew.setColor(x, y, image.getColor(x, y));
            }
        }

        image.close();
        this.resourceLocation = new Identifier("hypnotic", "fonts" + getFont().getFontName().toLowerCase().replace(" ", "-") + size);
        applyTexture(resourceLocation, imgNew);
    }

    private static NativeImage readTexture(String textureBase64) {
        try {
            byte[] imgBytes = Base64.decodeBase64(textureBase64);
            ByteArrayInputStream bais = new ByteArrayInputStream(imgBytes);
            return NativeImage.read(bais);
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    private void applyTexture(Identifier identifier, NativeImage nativeImage) {
        MinecraftClient.getInstance().execute(() -> MinecraftClient.getInstance().getTextureManager().registerTexture(identifier, new NativeImageBackedTexture(nativeImage)));
    }

    public final void drawString(MatrixStack matrixStack, String text, float x, float y, FontType fontType, int color, int color2) {
        text = stripUnsupported(text);

        RenderUtils.setup2DRender(false);
        String text2 = stripControlCodes(text);
        switch (fontType.ordinal()) {
            case 1:
                drawer(matrixStack, text2, x + 0.5F, y, color2);
                drawer(matrixStack, text2, x - 0.5F, y, color2);
                drawer(matrixStack, text2, x, y + 0.5F, color2);
                drawer(matrixStack, text2, x, y - 0.5F, color2);
                break;
            case 2:
                drawer(matrixStack, text2, x + 0.5F, y + 0.5F, color2);
                break;
            case 3:
                drawer(matrixStack, text2, x + 0.5F, y + 1.0F, color2);
                break;
            case 4:
                drawer(matrixStack, text2, x, y + 0.5F, color2);
                break;
            case 5:
                drawer(matrixStack, text2, x, y - 0.5F, color2);
                break;
            case 6:
                break;
        }

        drawer(matrixStack, text, x, y, color);
        RenderUtils.end2DRender();
    }

    public void drawCenteredString(MatrixStack matrixStack, String text, float x, float y, int color) {
    	if (!mcFont) drawString(matrixStack, text, (x - getStringWidth(text) / 2), y, FontType.SHADOW_THIN, color);
    	else DrawableHelper.drawCenteredText(matrixStack, mc.textRenderer, text, (int)x, (int)y + 5, color);
    }

    public final void drawString(MatrixStack matrixStack, String text, float x, float y, FontType fontType, int color) {
        matrixStack.scale(0.5f, 0.5f, 1);
        drawString(matrixStack, text, x, y, fontType, color, 0xBB000000);
        matrixStack.scale(2f, 2f, 1);
    }

    private final void drawer(MatrixStack matrixStack, String text, float x, float y, int color) {
        x *= 2.0F;
        y *= 2.0F;
        RenderUtils.setup2DRender(false);
        RenderUtils.bindTexture(this.resourceLocation);

        if ((color & -67108864) == 0)
        {
            color |= -16777216;
        }

        int newColor = color;
        float startX = x;
        boolean scramble = false;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        for (int i = 0; i < text.length(); i++)
            if ((text.charAt(i) == '\247') && (i + 1 < text.length())) {
                char oneMore = Character.toLowerCase(text.charAt(i + 1));
                if (oneMore == 'n') {
                    y += this.theMetrics.getAscent() + 2;
                    x = startX;
                }else if (oneMore == 'k') {
                    scramble = true;
                } else if (oneMore == 'r')
                    newColor = color;
                else {
                    newColor = getColorFromCode(oneMore);
                }
                i++;
            } else {
                try {
                    String obfText = "\\:><&%$@!/?";
                    char c = scramble ? obfText.charAt((int)(new Random().nextFloat() * (obfText.length() - 1))) : text.charAt(i);
                    drawChar(matrixStack, c, x, y, newColor);
                    x += getStringWidth(Character.toString(c)) * 2.0F;
                } catch (ArrayIndexOutOfBoundsException indexException) {
                    char c = text.charAt(i);
                    System.err.println("Can't draw character: " + c + " (" + Character.getNumericValue(c) + ")");

                }
            }
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
        RenderUtils.shaderColor(0xffffffff);
    }

    private final Rectangle2D getBounds(String text) {
        return this.theMetrics.getStringBounds(text, this.theGraphics);
    }

    private final void drawChar(MatrixStack matrixStack, char character, float x, float y, int color) throws ArrayIndexOutOfBoundsException {
        Rectangle2D bounds = this.theMetrics.getStringBounds(Character.toString(character), this.theGraphics);
        drawTexturedModalRect(matrixStack, x, y, this.xPos[(character - this.startChar)], this.yPos[(character - this.startChar)], (float) bounds.getWidth(), (float) bounds.getHeight() + this.theMetrics.getMaxDescent() + 1.0F, color);
    }

    @SuppressWarnings("unused")
	private final List<String> listFormattedStringToWidth(String s, int width) {
        return Arrays.asList(wrapFormattedStringToWidth(s, width).split("\n"));
    }

    private final String wrapFormattedStringToWidth(String s, float width) {
        int wrapWidth = sizeStringToWidth(s, width);

        if (s.length() <= wrapWidth) {
            return s;
        }
        String split = s.substring(0, wrapWidth);
        String split2 = getFormatFromString(split)
                + s.substring(wrapWidth + ((s.charAt(wrapWidth) == ' ') || (s.charAt(wrapWidth) == '\n') ? 1 : 0));
        try {
            return split + "\n" + wrapFormattedStringToWidth(split2, width);
        } catch (Exception e) {
        	System.err.println("Cannot wrap string to width.");
        }
        return "";
    }

    private final int sizeStringToWidth(String par1Str, float par2) {
        int var3 = par1Str.length();
        float var4 = 0.0F;
        int var5 = 0;
        int var6 = -1;

        for (boolean var7 = false; var5 < var3; var5++) {
            char var8 = par1Str.charAt(var5);

            switch (var8) {
                case '\n':
                    var5--;
                    break;
                case '\247':
                    if (var5 < var3 - 1) {
                        var5++;
                        char var9 = par1Str.charAt(var5);

                        if ((var9 != 'l') && (var9 != 'L')) {
                            if ((var9 == 'r') || (var9 == 'R') || (isFormatColor(var9)))
                                var7 = false;
                        } else
                            var7 = true;
                    }
                    break;
                case ' ':
                    var6 = var5;
                case '-':
                    var6 = var5;
                case '_':
                    var6 = var5;
                case ':':
                    var6 = var5;
                default:
                    String text = String.valueOf(var8);
                    var4 += getStringWidth(text);

                    if (var7) {
                        var4 += 1.0F;
                    }
                    break;
            }
            if (var8 == '\n') {
                var5++;
                var6 = var5;
            } else {
                if (var4 > par2) {
                    break;
                }
            }
        }
        return (var5 != var3) && (var6 != -1) && (var6 < var5) ? var6 : var5;
    }

    private final String getFormatFromString(String par0Str) {
        String var1 = "";
        int var2 = -1;
        int var3 = par0Str.length();

        while ((var2 = par0Str.indexOf('\247', var2 + 1)) != -1) {
            if (var2 < var3 - 1) {
                char var4 = par0Str.charAt(var2 + 1);

                if (isFormatColor(var4))
                    var1 = "\247" + var4;
                else if (isFormatSpecial(var4)) {
                    var1 = var1 + "\247" + var4;
                }
            }
        }

        return var1;
    }

    private final boolean isFormatColor(char par0) {
        return ((par0 >= '0') && (par0 <= '9')) || ((par0 >= 'a') && (par0 <= 'f')) || ((par0 >= 'A') && (par0 <= 'F'));
    }

    private final boolean isFormatSpecial(char par0) {
        return ((par0 >= 'k') && (par0 <= 'o')) || ((par0 >= 'K') && (par0 <= 'O')) || (par0 == 'r') || (par0 == 'R');
    }

    private final void drawTexturedModalRect(MatrixStack matrixStack, float x, float y, float u, float v, float width, float height, int color) {
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        float scale = 0.0039063F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        float f = (color >> 24 & 0xFF) / 255.0F;
        float f1 = (color >> 16 & 0xFF) / 255.0F;
        float f2 = (color >> 8 & 0xFF) / 255.0F;
        float f3 = (color & 0xFF) / 255.0F;
        bufferBuilder.vertex(matrix4f, x + 0.0F, y + height, 0.0f).texture((u + 0.0F) * scale, (v + height) * scale).color(f1, f2, f3, f).next();
        bufferBuilder.vertex(matrix4f, x + width, y + height, 0.0f).texture((u + width) * scale, (v + height) * scale).color(f1, f2, f3, f).next();
        bufferBuilder.vertex(matrix4f, x + width, y + 0.0F, 0.0f).texture((u + width) * scale, (v + 0.0F) * scale).color(f1, f2, f3, f).next();
        bufferBuilder.vertex(matrix4f, x + 0.0F, y + 0.0F, 0.0f).texture((u + 0.0F) * scale, (v + 0.0F) * scale).color(f1, f2, f3, f).next();
    }

    public final String stripControlCodes(String s) {
        return this.patternControlCode.matcher(s).replaceAll("");
    }

    public final String stripUnsupported(String s) {
        return this.patternUnsupported.matcher(s).replaceAll("");
    }

    public final Graphics2D getGraphics() {
        return this.theGraphics;
    }

    public final Font getFont() {
        return theFont;
    }

    private int getColorFromCode(char code) {
        switch (code) {
            case '0': return Color.BLACK.getRGB();
            case '1': return 0xff0000AA;
            case '2': return 0xff00AA00;
            case '3': return 0xff00AAAA;
            case '4': return 0xffAA0000;
            case '5': return 0xffAA00AA;
            case '6': return 0xffFFAA00;
            case '7': return 0xffAAAAAA;
            case '8': return 0xff555555;
            case '9': return 0xff5555FF;
            case 'a': return 0xff55FF55;
            case 'b': return 0xff55FFFF;
            case 'c': return 0xffFF5555;
            case 'd': return 0xffFF55FF;
            case 'e': return 0xffFFFF55;
            case 'f': return 0xffffffff;
            case 'g': return 0xffDDD605;
        }
        return -1;
    }

    public enum FontType {
        NORMAL, SHADOW_THICK, SHADOW_THIN, OUTLINE_THIN, EMBOSS_TOP, EMBOSS_BOTTOM;
    }

    private static MinecraftClient mc = MinecraftClient.getInstance();

    public float getStringWidth(String string) {
        if (!mcFont)
            return (float) (getBounds(this.stripControlCodes(string)).getWidth()) / 2F;
        else
            return mc.textRenderer.getWidth(string);
    }

    public float getStringHeight(String string) {
        if (!mcFont)
            return (float) (getBounds((this.stripControlCodes(string))).getHeight() / 2.0F);
        else
            return mc.textRenderer.fontHeight;
    }

    public float getStringWidth(Text string) {
        return mc.textRenderer.getWidth(string);
    }

    public void drawWithShadow(MatrixStack matrixStack, String text, float x, float y, int color) {
        if (!mcFont) {
            this.drawString(matrixStack, text, x, y - 3, NahrFont.FontType.SHADOW_THIN, color);
        } else {
            mc.textRenderer.drawWithShadow(matrixStack, text, x, y + 2, color);
        }
    }
    
    public float getWidth(String string) {
        if (!mcFont)
            return (float) ((getBounds(this.stripControlCodes(string)).getWidth()) / 2F) + 10;
        else
            return mc.textRenderer.getWidth(string) + 10;
    }

    public void draw(MatrixStack matrixStack, String text, float x, float y, int color) {
        if (!mcFont) {
            this.drawString(matrixStack, text, x, y + 2, NahrFont.FontType.NORMAL, color);
        } else {
            mc.textRenderer.draw(matrixStack, text, x, y + 4, color);
        }
    }

    public void drawCenteredString(MatrixStack matrixStack, String string, float x, float y, int color, boolean shadow) {
        float newX = x - ((getStringWidth(string)) / 2);
        if (!mcFont) {
            this.drawString(matrixStack, string, newX, y, shadow ? NahrFont.FontType.SHADOW_THIN : NahrFont.FontType.NORMAL, color);
        } else {
            mc.textRenderer.drawWithShadow(matrixStack, string, newX, y + 2, color);
        }
    }

    public void drawWithShadow(MatrixStack matrixStack, Text text, float x, float y, int color) {
        String s = text.getString();
        draw(matrixStack, s, x + 0.5f, y + 0.5f + 4, 0xff000000);
        draw(matrixStack, s, x, y, color);
    }

    public void draw(MatrixStack matrixStack, Text text, float x, float y, int color) {
        mc.textRenderer.draw(matrixStack, text, x, y + 2, color);
    }

    public void drawCenteredString(MatrixStack matrixStack, Text string, float x, float y, int color) {
        float newX = x - (getStringWidth(string) / 2);

        drawWithShadow(matrixStack, string, newX, y, color);
    }

    public String fix(String s) {
        if (s == null || s.isEmpty())
            return s;
        for (int i = 0; i < 9; i++) {
            if (s.contains("\247" + i))
                s = s.replace("\247" + i, "");
        }
        return s.replace("\247a", "").replace("\247b", "").replace("\247c", "").replace("\247d", "").replace("\247e", "").replace("\247f", "").replace("\247g", "");
    }
    
    public String trimToWidth(String string, int width) {
    	try {
    		return string.substring(0, width);
    	} catch(Exception e) {
    		return string;
    	}
    }
    
    public String trimToWidth(String string, int width, boolean backwards) {
    	try {
    		return backwards ? string.substring(width) : string.substring(0, width);
    	} catch(Exception e) {
    		return string;
    	}
    }
}
