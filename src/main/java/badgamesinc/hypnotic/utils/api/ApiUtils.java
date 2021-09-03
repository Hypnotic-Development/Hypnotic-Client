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
	
	public boolean checkOnline(String username) throws IOException, InterruptedException
	{
		System.out.println(username);
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://68.0.143.100:8080/hypnotic/api/chkonline.php?uuid=" + username))
                .build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(removeHtmlTags(response.body()) + " sdlkfsjdlkfjslkjflsdjflksfsldkfjsldkfjlsk");
		return Boolean.parseBoolean(removeHtmlTags(response.body()));
	}
	
	public boolean setOnline(String username) throws IOException, InterruptedException
	{
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://68.0.143.100:8080/hypnotic/api/setonline.php?uuid=" + username))
                .build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		boolean didDie = !removeHtmlTags(response.body()).equalsIgnoreCase("-1");
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(removeHtmlTags(response.body()));
		System.out.println();
		System.out.println();
		System.out.println();
		return didDie;
	}
	
	public boolean remOnline(String username) throws IOException, InterruptedException
	{
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://68.0.143.100:8080/hypnotic/api/remonline.php?uuid=" + username))
                .build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		boolean didDie = !removeHtmlTags(response.body()).equalsIgnoreCase("-1");
		return didDie;
	}
	
	private String removeHtmlTags(String string) {
		return string.replaceAll("\\<.*?\\>", "");
	}
}
