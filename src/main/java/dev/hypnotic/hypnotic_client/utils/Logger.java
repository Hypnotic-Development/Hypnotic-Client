package dev.hypnotic.hypnotic_client.utils;

public class Logger {

	private static String infoPrefix = "[Hypnotic INFO] ";
	private static String errPrefix = "[Hypnotic ERROR] ";
	
	public static void logInfo(String message) {
		infoPrefix = "[Hypnotic INFO] ";
		System.out.println(infoPrefix + message);
	}
	
	public static void logError(String message) {
		System.err.println(errPrefix + message);
	}
}
