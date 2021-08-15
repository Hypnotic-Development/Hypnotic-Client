package badgamesinc.hypnotic.utils;

public class Logger {

	private static String infoPrefix = "[Hypnotic ERROR] ";
	private static String errPrefix = "[Hypnotic ERROR] ";
	
	public static void logInfo(String message) {
		System.err.println(infoPrefix + message);
	}
	
	public static void logError(String message) {
		System.err.println(errPrefix + message);
	}
}
