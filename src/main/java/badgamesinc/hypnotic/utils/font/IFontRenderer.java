package badgamesinc.hypnotic.utils.font;

import java.awt.Color;

import net.minecraft.client.util.math.MatrixStack;

public interface IFontRenderer {
    double drawChar(MatrixStack matrices, char c, double x, double y);

    void drawString(MatrixStack matrices, String text, double x, double y);

    void drawString(MatrixStack matrices, String text, double x, double y, Color color, boolean shadow);

    void drawString(MatrixStack matrices, String text, double x, double y, Color color);

    void drawStringWithShadow(MatrixStack matrices, String text, double x, double y, Color color);

    int drawStringWithCustomWidthWithShadow(MatrixStack matrices, String text, double x, double y, Color color, double width);

    int drawStringWithCustomWidth(MatrixStack matrices, String text, double x, double y, Color color, double width);

    int drawStringWithCustomWidth(MatrixStack matrices, String text, double x, double y, Color color, double width, boolean shadow);

    int drawStringWithCustomHeightWithShadow(MatrixStack matrices, String text, double x, double y, Color color, double height);

    int drawStringWithCustomHeight(MatrixStack matrices, String text, double x, double y, Color color, double height);

    int drawStringWithCustomHeight(MatrixStack matrices, String text, double x, double y, Color color, double height, boolean shadow);

    int drawSplitString(MatrixStack matrices, String text, double x, double y, Color color, double width);

    double drawSplitString(MatrixStack matrices, String text, double x, double y, Color color, double width, double height);

    double getCharWidth(char c);

    double getStringWidth(String text);

    double getStringWidth(String text, double height);

    int getFontHeight();

    double getFontHeightWithCustomWidth(String text, double width);
}
