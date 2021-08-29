package badgamesinc.hypnotic.settings.settingtypes;

import java.awt.Color;

import badgamesinc.hypnotic.settings.Setting;
import net.minecraft.util.math.MathHelper;

public class ColorSetting extends Setting {

	public float hue;
	public float sat;
	public float bri;

	protected float defaultHue;
	protected float defaultSat;
	protected float defaultBri;
	
	public ColorSetting(String name, float r, float g, float b, boolean hsv) {
		this.name = name;
		
		if (hsv) {
			this.hue = r;
			this.sat = g;
			this.bri = b;
		} else {
			float[] vals = rgbToHsv(r, g, b);
			this.hue = vals[0];
			this.sat = vals[1];
			this.bri = vals[2];
		}
		
		this.defaultHue = hue;
		this.defaultSat = sat;
		this.defaultBri = bri;
	}
	
	public int getRGB() {
		return MathHelper.hsvToRgb(hue, sat, bri);
	}

	public float[] getRGBFloat() {
		int col = MathHelper.hsvToRgb(hue, sat, bri);
		return new float[] { (col >> 16 & 255) / 255f, (col >> 8 & 255) / 255f, (col & 255) / 255f };
	}
	
	public String getRGBString() {
		StringBuilder string = new StringBuilder();
		string.append(getRGBFloat()[0] + "-");
		string.append(getRGBFloat()[1] + "-");
		string.append(getRGBFloat()[2]);
		return string.toString();
	}
	
	public void setRGB(int r, int b, int g) {
//		MathHelper.rgb
	}
	
	public Color getColor() {
		return new Color(getRGB());
	}
	
	public float[] getHSB() {
		return new float[] {hue, sat, bri};
	}
	

	private float[] rgbToHsv(float r, float g, float b) {
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
	}
}
