package badgamesinc.hypnotic.utils.font;

public class FontManager {

	private static boolean mcFont = false;
	public static NahrFont robotoSmall = new NahrFont("RobotoRegular.ttf", 16, 1, mcFont);
	public static NahrFont roboto = new NahrFont("RobotoRegular.ttf", 18, 1, mcFont);
	public static NahrFont robotoMed = new NahrFont("RobotoRegular.ttf", 20, 1, mcFont);
	public static NahrFont robotoMed2 = new NahrFont("RobotoRegular.ttf", 22, 1, mcFont);
	public static NahrFont robotoBig = new NahrFont("RobotoRegular.ttf", 25, 1, mcFont);
	
	public static void setMcFont(boolean mcFont) {
		FontManager.mcFont = mcFont;
		robotoSmall = new NahrFont("RobotoRegular.ttf", 16, 1, mcFont);
		roboto = new NahrFont("RobotoRegular.ttf", 18, 1, mcFont);
		robotoMed = new NahrFont("RobotoRegular.ttf", 20, 1, mcFont);
		robotoMed2 = new NahrFont("RobotoRegular.ttf", 22, 1, mcFont);
		robotoBig = new NahrFont("RobotoRegular.ttf", 25, 1, mcFont);
	}
}
