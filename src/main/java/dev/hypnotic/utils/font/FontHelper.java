package dev.hypnotic.utils.font;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public enum FontHelper {
    INSTANCE;
	private static MinecraftClient mc = MinecraftClient.getInstance();
    private NahrFont clientFont = new NahrFont("Verdana", 20, 1);

    public float getStringWidth(String string, boolean customFont) {
        if (customFont)
            return clientFont.getStringWidth(clientFont.stripControlCodes(string));
        else
            return mc.textRenderer.getWidth(string);
    }

    public float getStringHeight(String string, boolean customFont) {
        if (customFont)
            return clientFont.getStringHeight(clientFont.stripControlCodes(string));
        else
            return mc.textRenderer.fontHeight;
    }

    public float getStringWidth(Text string) {
        return mc.textRenderer.getWidth(string);
    }

    public void drawWithShadow(MatrixStack matrixStack, String text, float x, float y, int color, boolean customFont) {
        if (customFont) {
            clientFont.drawString(matrixStack, text, x, y, NahrFont.FontType.SHADOW_THIN, color);
        } else {
            mc.textRenderer.draw(matrixStack, fix(text), x + 0.5f, y + 0.5f, 0xff000000);
            mc.textRenderer.draw(matrixStack, text, x, y, color);
        }
    }

    public void draw(MatrixStack matrixStack, String text, float x, float y, int color, boolean customFont) {
        if (customFont) {
            clientFont.drawString(matrixStack, text, x, y, NahrFont.FontType.NORMAL, color);
        } else {
            mc.textRenderer.draw(matrixStack, text, x, y, color);
        }
    }

    public void drawCenteredString(MatrixStack matrixStack, String string, float x, float y, int color, boolean customFont, boolean shadow) {
        float newX = x - ((getStringWidth(string, customFont)) / 2);
        if (customFont) {
            clientFont.drawString(matrixStack, string, newX, y, shadow ? NahrFont.FontType.SHADOW_THIN : NahrFont.FontType.NORMAL, color);
        } else {
            mc.textRenderer.draw(matrixStack, fix(string), newX + 0.5f, y + 0.5f, 0xff000000);
            mc.textRenderer.draw(matrixStack, string, newX, y, color);
        }
    }

    public void drawWithShadow(MatrixStack matrixStack, Text text, float x, float y, int color) {
        String s = text.getString();
        draw(matrixStack, s, x + 0.5f, y + 0.5f, 0xff000000, true);
        draw(matrixStack, s, x, y, color, true);
    }

    public void draw(MatrixStack matrixStack, Text text, float x, float y, int color) {
        mc.textRenderer.draw(matrixStack, text, x, y, color);
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

    public boolean setClientFont(NahrFont font) {
        try {
            this.clientFont = font;
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public NahrFont getClientFont() {
        return clientFont;
    }
}
