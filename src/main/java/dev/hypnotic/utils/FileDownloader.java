package dev.hypnotic.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileDownloader implements Runnable {

	public String link;
	public File out;
	
	public FileDownloader(String link, File out) {
		this.link = link;
		this.out = out;
	}

	@Override
	public void run() {
		try {
			URL url = new URL(link);
			HttpURLConnection http = (HttpURLConnection)url.openConnection();
			double fileSize = (double)http.getContentLengthLong();
			BufferedInputStream in = new BufferedInputStream(http.getInputStream());
			FileOutputStream fos = new FileOutputStream(this.out);
			BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
			byte[] buffer = new byte[1024];
			double downloaded = 0.00;
			int read = 0;
			double percentDownloaded = 0.00;
			
			while ((read = in.read(buffer, 0, 1024)) >= 0) {
				bout.write(buffer, 0, read);
				downloaded+=read;
				percentDownloaded = (downloaded * 100) / fileSize;
				String percent = String.format("%.4f", percentDownloaded);
				System.out.println("Downloaded " + percent + "%");
			}
			bout.close();
			in.close();
			System.out.println("Download Complete!");
		} catch(IOException e) {
			e.printStackTrace();
		}
		
	}
}
