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
		boolean didDie = response.body().replace("<!DOCTYPE html><html><body>", "").replace("</body></html>", "") != "-1";
		System.out.println(response.body().replace("<!DOCTYPE html><html><body>", "").replace("</body></html>", "") != "-1");
		return didDie;
	}
	
	public boolean setOnline(String username) throws IOException, InterruptedException
	{
		System.out.println(username);
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://68.0.143.100:8080/hypnotic/api/setonline.php?uuid=" + username))
                .build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		boolean didDie = response.body().replace("<!DOCTYPE html><html><body>", "").replace("</body></html>", "") != "-1";
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println(response.body().replace("<!DOCTYPE html><html><body>", "").replace("</body></html>", ""));
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
		boolean didDie = response.body().replace("<!DOCTYPE html><html><body>", "").replace("</body>", "</html>") != "-1";
		return didDie;
	}
}
