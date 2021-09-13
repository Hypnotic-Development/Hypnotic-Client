package badgamesinc.hypnotic.utils;

import java.awt.Color;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;

public class ColorUtils {

	public static String colorChar = "\247";
	public static String purple = "\2475";
	public static String red = "\247c";
	public static String aqua = "\247b";
	public static String green = "\247a";
	public static String blue = "\2479";
	public static String darkGreen = "\2472";
	public static String darkBlue = "\2471";
	public static String black = "\2470";
	public static String darkRed = "\2474";
	public static String darkAqua = "\2473";
	public static String lightPurple = "\247d";
	public static String yellow = "\247e";
	public static String white = "\247f";
	public static String gray = "\2477";
	public static String darkGray = "\2478";
	public static String reset = "\247r";
	public static String pingle = "#FF1464";
	public static int defaultClientColor = new Color(255, 20, 100).getRGB();
	
	public static Color getCategoryColor(Category category) {
		return category.color;
	}
	
	public static Color getCategoryColor(Mod mod) {
		return mod.getCategory().color;
	}
	
	public static int rainbow(float seconds, float saturation, float brigtness) {
		float hue = (System.currentTimeMillis() % (int) (seconds * 1000)) / (float) (seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, 1);
		return color;
	}
	
	public static int rainbow(float seconds, float saturation, float brigtness, long index) {
		float hue = ((System.currentTimeMillis() + index) % (int) (seconds * 1000)) / (float) (seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, 1);
		return color;
	}
	
	public static Color fade(Color color, int index, int count) {
	      float[] hsb = new float[3];
	      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
	      float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + (float)index / (float)count * 2.0F) % 2.0F - 1.0F);
	      brightness = 0.5F + 0.5F * brightness;
	      hsb[2] = brightness % 2.0F;
	      return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
	 }

	public static Color getColor(int color) {
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        return new Color(red, green, blue, alpha);
    }
		
	public static int transparent(int rgb, int opacity) {
		Color color = new Color(rgb);
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity).getRGB();
	}
}
