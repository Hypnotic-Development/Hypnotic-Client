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
package dev.hypnotic.settings.settingtypes;

import java.awt.Color;

import dev.hypnotic.settings.Setting;
import dev.hypnotic.utils.ColorUtils;
import net.minecraft.util.math.MathHelper;

public class ColorSetting extends Setting {

	public float hue;
	public float sat;
	public float bri;
	public float alpha;
	
	public boolean rainbow = false;
	public float rainbowSpeed = 6;
	public float rainbowSat = 0.6f;

	protected float defaultHue;
	protected float defaultSat;
	protected float defaultBri;
	
	public ColorSetting(String name, float r, float g, float b, float a, boolean hsv) {
		this.name = name;
		
		if (hsv) {
			this.hue = r;
			this.sat = g;
			this.bri = b;
			this.alpha = a;
		} else {
			float[] vals = rgbToHsv(r, g, b, a);
			this.hue = vals[0];
			this.sat = vals[1];
			this.bri = vals[2];
		}
		
		this.defaultHue = hue;
		this.defaultSat = sat;
		this.defaultBri = bri;
	}
	
	public ColorSetting(String name, float r, float g, float b, boolean hsv) {
		this.name = name;
		
		if (hsv) {
			this.hue = r;
			this.sat = g;
			this.bri = b;
			this.alpha = 1;
		} else {
			float[] vals = rgbToHsv(r, g, b, 1f);
			this.hue = vals[0];
			this.sat = vals[1];
			this.bri = vals[2];
			this.alpha = 1;
		}
		
		this.defaultHue = hue;
		this.defaultSat = sat;
		this.defaultBri = bri;
	}
	
	public ColorSetting(String name, Color color) {
		this.name = name;
		float[] vals = rgbToHsv(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		this.setHSV(vals[0], vals[1], vals[2]);
		this.hue = vals[0];
		this.sat = vals[1];
		this.bri = vals[2];
		this.alpha = vals[3];
		
		this.defaultHue = hue;
		this.defaultSat = sat;
		this.defaultBri = bri;
	}
	
	public ColorSetting(String name, String hex) {
		this.name = name;
		Color hexColor = ColorUtils.hexToRgb(hex);
		float[] vals = rgbToHsv(hexColor.getRed(), hexColor.getGreen(), hexColor.getBlue(), hexColor.getAlpha());
		this.setHSV(vals[0], vals[1], vals[2]);
		this.hue = vals[0];
		this.sat = vals[1];
		this.bri = vals[2];
		this.alpha = vals[3];
		
		this.defaultHue = hue;
		this.defaultSat = sat;
		this.defaultBri = bri;
	}
	
	public ColorSetting(String name, float rainbowSpeed, float rainbowSat) {
		this.name = name;
		this.rainbow = true;
		Color color = new Color(ColorUtils.rainbow(rainbowSpeed, rainbowSat, 1));
		float[] vals = rgbToHsv(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		this.setHSV(vals[0], vals[1], vals[2]);
		this.hue = vals[0];
		this.sat = vals[1];
		this.bri = vals[2];
		this.alpha = vals[3];
		
		this.defaultHue = hue;
		this.defaultSat = sat;
		this.defaultBri = bri;
	}
	
	public int getRGB() {
		Color c = new Color(MathHelper.hsvToRgb(hue, sat, bri));
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), (int)alpha).getRGB();
	}

	public float[] getRGBFloat() {
		int col = MathHelper.hsvToRgb(hue, sat, bri);
		return new float[] { (col >> 16 & 255) / 255f, (col >> 8 & 255) / 255f, (col & 255) / 255f, alpha};
	}
	
	public void setRGB(float r, float g, float b, float a) {
		float[] vals = rgbToHsv(r, g, b, a);
		this.hue = vals[0];
		this.sat = vals[1];
		this.bri = vals[2];
		this.alpha = vals[3];
	}
	
	public String getHSVString() {
		StringBuilder string = new StringBuilder();
		string.append(getRGBFloat()[0] + "-");
		string.append(getRGBFloat()[1] + "-");
		string.append(getRGBFloat()[2]);
		return string.toString();
	}
	
	public void setHSV(float h, float s, float v) {
		this.hue = h;
		this.sat = s;
		this.bri = v;
	}
	
	public Color getColor() {
		return new Color(getRGB());
	}
	
	public float[] getHSB() {
		return new float[] {hue, sat, bri};
	}
	
	public String getHex() {
		return Integer.toHexString(getRGB());
	}

	public boolean isRainbow() {
		return rainbow;
	}
	
	public float[] rgbToHsv(float r, float g, float b, float a) {
		float[] hsv = Color.RGBtoHSB((int)r, (int)g, (int)b, null);
		return new float[] {hsv[0], hsv[1], hsv[2], a / 255f};
	}
	
	public String rgbToHex(int rgb) {
		return Integer.toHexString(rgb);
	}
	
	public int[] hexToRgbInt(String hex) {
		try {
			Color color = Color.decode("#" + hex.replace("#", ""));
			return new int[] {color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()};
		} catch(NumberFormatException e) {
			System.err.println("Invalid hex string!");
			return new int[] {Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), Color.WHITE.getAlpha()};
		}
	}
}
