package badgamesinc.hypnotic.settings.settingtypes;

import java.awt.Color;

import badgamesinc.hypnotic.settings.Setting;
import net.minecraft.util.math.MathHelper;

public class ColorSetting extends Setting {

	public float hue;
	public float sat;
	public float bri;
	public float alpha;

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
	
	public ColorSetting(String name, String hex) {
		this.name = name;
		Color hexColor = hexToRgb(hex);
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

	/*public float[] rgbToHsv(float r, float g, float b) {
		float minRGB = Math.min(r, Math.min(g, b));
		float maxRGB = Math.max(r, Math.max(g, b));

		// Black-gray-white
		if (minRGB == maxRGB) {
			return new float[] { 0f, 0f, minRGB };
		}

		// Colors other than black-gray-white:
		float d = (r == minRGB) ? g - b : (b == minRGB) ? r - g : b - r;
		float h = (r == minRGB) ? 3 : (b == minRGB) ? 1 : 5;
		float computedH = 60 * (h - d / (maxRGB - minRGB)) / 360f;
		float computedS = (maxRGB - minRGB) / maxRGB;
		float computedV = maxRGB;

		return new float[] { computedH, computedS, computedV };
	}*/
	
	public float[] rgbToHsv(float r, float g, float b, float a) {
		float[] hsv = Color.RGBtoHSB((int)r, (int)g, (int)b, null);
		return new float[] {hsv[0], hsv[1], hsv[2], a / 255f};
	}
	
	public String rgbToHex(int rgb) {
		return Integer.toHexString(rgb);
	}
	
	public Color hexToRgb(String hex) {
		try {
			return Color.decode("#" + hex.replace("#", ""));
		} catch(NumberFormatException e) {
			System.err.println("Invalid hex string!");
			return Color.WHITE;
		}
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
