package badgamesinc.hypnotic.utils.font;

public class FontManager {

	private static String font = "RobotoRegular.ttf";
	public static int size = 20;
	private static boolean mcFont = false;
	public static NahrFont robotoSmall = new NahrFont(font, 16, 1, mcFont);
	public static NahrFont roboto = new NahrFont(font, 18, 1, mcFont);
	public static NahrFont robotoMed = new NahrFont(font, 20, 1, mcFont);
	public static NahrFont robotoMed2 = new NahrFont(font, 22, 1, mcFont);
	public static NahrFont robotoBig = new NahrFont(font, 25, 1, mcFont);
	public static NahrFont robotoCustomSize = new NahrFont(font, size, 1, mcFont);
	
	public static void setMcFont(boolean mcFont) {
		FontManager.mcFont = mcFont;
		font = "RobotoRegular.ttf";
		robotoSmall = new NahrFont(font, 16, 1, mcFont);
		roboto = new NahrFont(font, 18, 1, mcFont);
		robotoMed = new NahrFont(font, 20, 1, mcFont);
		robotoMed2 = new NahrFont(font, 22, 1, mcFont);
		robotoBig = new NahrFont(font, 25, 1, mcFont);
		robotoCustomSize = new NahrFont(font, size, 1, mcFont);
	}
	
	public static void setSize(int size) {
		FontManager.size = size;
		robotoCustomSize = new NahrFont(font, size, 1, mcFont);
	}
}
