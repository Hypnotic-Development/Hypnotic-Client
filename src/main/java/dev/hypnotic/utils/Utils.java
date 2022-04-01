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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.UUID;
import java.util.stream.Stream;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class Utils {

	public static String OS = System.getProperty("os.name").toLowerCase();
	private HashMap<String, UUID> nameMap = Maps.newHashMap();
	private static Utils get = new Utils();
	public static Gson gson = new Gson();
	private static final HttpClient CLIENT = HttpClient.newHttpClient();
	
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
	
	private enum Method {
        GET,
        POST
    }

    public static class Request {
        private HttpRequest.Builder builder;
        private Method method;

        public Request(Method method, String url) {
            try {
                this.builder = HttpRequest.newBuilder().uri(new URI(url)).header("User-Agent", "Hypnotic Client");
                this.method = method;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        public Request bearer(String token) {
            builder.header("Authorization", "Bearer " + token);

            return this;
        }

        public Request bodyString(String string) {
            builder.header("Content-Type", "text/plain");
            builder.method(method.name(), HttpRequest.BodyPublishers.ofString(string));
            method = null;

            return this;
        }

        public Request bodyForm(String string) {
            builder.header("Content-Type", "application/x-www-form-urlencoded");
            builder.method(method.name(), HttpRequest.BodyPublishers.ofString(string));
            method = null;

            return this;
        }

        public Request bodyJson(String string) {
            builder.header("Content-Type", "application/json");
            builder.method(method.name(), HttpRequest.BodyPublishers.ofString(string));
            method = null;

            return this;
        }

        public Request bodyJson(Object object) {
            builder.header("Content-Type", "application/json");
            builder.method(method.name(), HttpRequest.BodyPublishers.ofString(gson.toJson(object)));
            method = null;

            return this;
        }

        private <T> T _send(String accept, HttpResponse.BodyHandler<T> responseBodyHandler) {
            builder.header("Accept", accept);
            if (method != null) builder.method(method.name(), HttpRequest.BodyPublishers.noBody());

            try {
                var res = CLIENT.send(builder.build(), responseBodyHandler);
                return res.statusCode() == 200 ? res.body() : null;
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void send() {
            _send("*/*", HttpResponse.BodyHandlers.discarding());
        }

        public InputStream sendInputStream() {
            return _send("*/*", HttpResponse.BodyHandlers.ofInputStream());
        }

        public String sendString() {
            return _send("*/*", HttpResponse.BodyHandlers.ofString());
        }

        public Stream<String> sendLines() {
            return _send("*/*", HttpResponse.BodyHandlers.ofLines());
        }

        public <T> T sendJson(Type type) {
            InputStream in = _send("application/json", HttpResponse.BodyHandlers.ofInputStream());
            return in == null ? null : gson.fromJson(new InputStreamReader(in), type);
        }
    }

    public static Request get(String url) {
        return new Request(Method.GET, url);
    }

    public static Request post(String url) {
        return new Request(Method.POST, url);
    }
}
