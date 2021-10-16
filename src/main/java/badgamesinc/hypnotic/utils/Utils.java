package badgamesinc.hypnotic.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Utils {

	public static String OS = System.getProperty("os.name").toLowerCase();
	private HashMap<String, UUID> nameMap = Maps.newHashMap();
	private static Utils get = new Utils();
	public static Gson gson = new Gson();
	
	public static void openURL(String domain) {
	    String url = "https://" + domain;
	    Runtime rt = Runtime.getRuntime();
	    try {
	        if (isWindows()) {
	            rt.exec("rundll32 url.dll,FileProtocolHandler " + url).waitFor();
	        } else if (isMac()) {
	            String[] cmd = {"open", url};
	            rt.exec(cmd).waitFor();
	        } else if (isUnix()) {
	            String[] cmd = {"xdg-open", url};
	            rt.exec(cmd).waitFor();
	        } else {
	            try {
	                throw new IllegalStateException();
	            } catch (IllegalStateException e1) {
	                e1.printStackTrace();
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	public static boolean isWindows() {
	    return OS.contains("win");
	}

	public static boolean isMac() {
	    return OS.contains("mac");
	}

	public static boolean isUnix() {
	    return OS.contains("nix") || OS.contains("nux") || OS.indexOf("aix") > 0;
	}
	
	public static String readURL(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10 * 1000);
        BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder buffer = new StringBuilder();
        for (String line; (line = input.readLine()) != null; ) {
            buffer.append(line);
            buffer.append("\n");
        }
        input.close();
        return buffer.toString();
    }
	

	public static UUID getUUIDFromName(String name) {
        try {
            if (get.nameMap.containsKey(name))
                return get.nameMap.get(name);
            String result = null;
            try {
                result = readURL(new URL(String.format("https://api.mojang.com/users/profiles/minecraft/%s", name)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (result == null)
                return null;
            JsonObject object = gson.fromJson(result, JsonObject.class);
            UUID uuid = UUID.fromString(object.get("id").getAsString().replaceFirst(
                    "(\\p{XDigit}{8})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}{4})(\\p{XDigit}+)", "$1-$2-$3-$4-$5"
            ));
            get.nameMap.putIfAbsent(name, uuid);
            return uuid;
        } catch (Exception e) {
            return null;
        }
    }
	
	public static InputStream getFileFromJar(ClassLoader classLoader, String path) {
		return classLoader.getResourceAsStream(path);
	}
}
