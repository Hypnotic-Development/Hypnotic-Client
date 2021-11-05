package badgamesinc.hypnotic.utils.font;

import badgamesinc.hypnotic.utils.Utils;

public class FontManager {

	public static FontManager INSTANCE = new FontManager();
	public static String font = "Roboto-Regular.ttf";
	private static final String assets = "assets/hypnotic/fonts/";
	public static int size = 20;
	private static boolean mcFont = false;
	public static NahrFont robotoSmaller = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 14, 1, mcFont);
	public static NahrFont robotoSmall = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 16, 1, mcFont);
	public static NahrFont roboto = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 18, 1, mcFont);
	public static NahrFont robotoMed = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 20, 1, mcFont);
	public static NahrFont robotoMed2 = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 22, 1, mcFont);
	public static NahrFont robotoBig = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 25, 1, mcFont);
	public static NahrFont robotoCustomSize = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), size, 1, mcFont);
	public static NahrFont icons = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + "icons.ttf"), 21, 1, false);
	public static NahrFont iconsSmall = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + "icons.ttf"), 18, 1, false);
	public static NahrFont magneto = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + "magneto.ttf"), 16, 1, false);
	
	
	public static void setMcFont(boolean mcFont) {
		FontManager.mcFont = mcFont;
		robotoSmaller = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 14, 1, mcFont);
		robotoSmall = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 16, 1, mcFont);
		roboto = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 18, 1, mcFont);
		robotoMed = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 20, 1, mcFont);
		robotoMed2 = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 22, 1, mcFont);
		robotoBig = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), 25, 1, mcFont);
		robotoCustomSize = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), size, 1, mcFont);
		icons = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + "icons.ttf"), 21, 1, false);
		iconsSmall = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + "icons.ttf"), 18, 1, false);
		magneto = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + "magneto.ttf"), 16, 1, false);
	}
	
	public static void setSize(int size) {
		FontManager.size = size;
		robotoCustomSize = new NahrFont(Utils.getFileFromJar(FontManager.INSTANCE.getClass().getClassLoader(), assets + font), size, 1, mcFont);
	}
}
