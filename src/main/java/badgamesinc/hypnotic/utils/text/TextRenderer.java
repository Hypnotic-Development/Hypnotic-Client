package badgamesinc.hypnotic.utils.text;

import badgamesinc.hypnotic.utils.Color;
import net.minecraft.client.util.math.MatrixStack;

public interface TextRenderer {
    static TextRenderer get(boolean customFont) {
        return customFont ? Fonts.CUSTOM_FONT : VanillaTextRenderer.INSTANCE;
    }

    void setAlpha(double a);

    void begin(double scale, boolean scaleOnly, boolean big);
    default void begin(double scale) { begin(scale, false, false); }
    default void begin() { begin(1, false, false); }

    default void beginBig() { begin(1, false, true); }

    double getWidth(String text, int length, boolean shadow);
    default double getWidth(String text, boolean shadow) { return getWidth(text, text.length(), shadow); }
    default double getWidth(String text) { return getWidth(text, text.length(), false); }

    double getHeight(boolean shadow);
    default double getHeight() { return getHeight(false); }

    double render(String text, double x, double y, Color color, boolean shadow);
    default double render(String text, double x, double y, Color color) { return render(text, x, y, color, false); }

    boolean isBuilding();

    default void end() { end(null); }
    void end(MatrixStack matrices);
}
