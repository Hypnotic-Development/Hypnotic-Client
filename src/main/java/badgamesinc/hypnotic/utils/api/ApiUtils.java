package badgamesinc.hypnotic.utils.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiUtils 
{
	HttpClient client;
	
	
	public ApiUtils()
	{
		client = HttpClient.newHttpClient();
	}
	
	public boolean checkOnline(String username)
	{
		System.out.println(username);
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://68.0.143.100:8080/hypnotic/api/chkonline.php?uuid=" + username))
                .build();
		HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(removeHtmlTags(response.body()) + " sdlkfsjdlkfjslkjflsdjflksfsldkfjsldkfjlsk");
			return Boolean.parseBoolean(removeHtmlTags(response.body()));
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean setOnline(String username)
	{
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://68.0.143.100:8080/hypnotic/api/setonline.php?uuid=" + username))
                .build();
		HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			boolean didDie = !removeHtmlTags(response.body()).equalsIgnoreCase("-1");
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println(removeHtmlTags(response.body()));
			System.out.println();
			System.out.println();
			System.out.println();
			return didDie;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean remOnline(String username)
	{
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://68.0.143.100:8080/hypnotic/api/remonline.php?uuid=" + username))
                .build();
		HttpResponse<String> response;
		try {
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			boolean didDie = !removeHtmlTags(response.body()).equalsIgnoreCase("-1");
			return didDie;
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	private String removeHtmlTags(String string) {
		return string.replaceAll("\\<.*?\\>", "");
	}
}
