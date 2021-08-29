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
	
	public boolean checkOnline(String uuid) throws IOException, InterruptedException
	{
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://kzkawaiizenbo.ml:8080/hypnotic/api/chkonline.php?uuid=" + uuid))
                .build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		boolean didDie = response.body() != "-1";
		return didDie;
	}
	
	public boolean setOnline(String uuid) throws IOException, InterruptedException
	{
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://kzkawaiizenbo.ml:8080/hypnotic/api/setonline.php?uuid=" + uuid))
                .build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		boolean didDie = response.body() != "-1";
		return didDie;
	}
	
	public boolean remOnline(String uuid) throws IOException, InterruptedException
	{
		HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://kzkawaiizenbo.ml:8080/hypnotic/api/remonline.php?uuid=" + uuid))
                .build();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		boolean didDie = response.body() != "-1";
		return didDie;
	}
}
