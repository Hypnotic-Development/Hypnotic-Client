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
package dev.hypnotic.utils;

import java.awt.Color;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;

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
	
	public static Color defaultClientColor() {
		return new Color(255, 20, 100);
	}
	
	public static int rainbow(float seconds, float saturation, float brightness) {
		float hue = (System.currentTimeMillis() % (int) (seconds * 1000)) / (float) (seconds * 1000);
		int color = Color.HSBtoRGB(hue, saturation, brightness);
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
	
	public static Color fade(Color color, Color color2, int index, int count) {
	      float[] hsb = new float[3];
	      Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
	      float brightness = Math.abs(((float)(System.currentTimeMillis() + index % 2000L) / 1000.0F + (float)index / (float)count * 2.0F) % 2.0F - 1.0F);
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
	
	public static int transparent(int opacity) {
		Color color = Color.BLACK;
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), opacity).getRGB();
	}
	
	public static int blendColours(final int[] colours, final double progress) {
        final int size = colours.length;
        if (progress == 1.f) return colours[0];
        else if (progress == 0.f) return colours[size - 1];
        final double mulProgress = Math.max(0, (1 - progress) * (size - 1));
        final int index = (int) mulProgress;
        return fadeBetween(colours[index], colours[index + 1], mulProgress - index);
    }
    public static int fadeBetween(int startColour, int endColour, double progress) {
        if (progress > 1) progress = 1 - progress % 1;
        return fadeTo(startColour, endColour, progress);
    }

    public static int fadeBetween(int startColour, int endColour, long offset) {
        return fadeBetween(startColour, endColour, ((System.currentTimeMillis() + offset) % 2000L) / 1000.0);
    }

    public static int fadeBetween(int startColour, int endColour) {
        return fadeBetween(startColour, endColour, 0L);
    }

    public static int fadeTo(int startColour, int endColour, double progress) {
        double invert = 1.0 - progress;
        int r = (int) ((startColour >> 16 & 0xFF) * invert +
                (endColour >> 16 & 0xFF) * progress);
        int g = (int) ((startColour >> 8 & 0xFF) * invert +
                (endColour >> 8 & 0xFF) * progress);
        int b = (int) ((startColour & 0xFF) * invert +
                (endColour & 0xFF) * progress);
        int a = (int) ((startColour >> 24 & 0xFF) * invert +
                (endColour >> 24 & 0xFF) * progress);
        return ((a & 0xFF) << 24) |
                ((r & 0xFF) << 16) |
                ((g & 0xFF) << 8) |
                (b & 0xFF);
    }
	
	// Here for scripts
	
	public Color hex(String hex) {
		return Color.decode(hex);
	}
	
	public Color rgba(int red, int green, int blue, int alpha) {
		return new Color(red, green, blue, alpha);
	}
	
	public Color rgb(int red, int green, int blue) {
		return rgba(red, green, blue, 255);
	}

	/**
	 * @param hex - The hex string to decode
	 * @return A color object from the hex string,
	 * if the hex string is invalid it will return Color.WHITE
	 */
	public static Color hexToRgb(String hex) {
		try {
			return Color.decode("#" + hex.replace("#", ""));
		} catch(NumberFormatException e) {
			System.err.println("Invalid hex string!");
			return Color.WHITE;
		}
	}
}
