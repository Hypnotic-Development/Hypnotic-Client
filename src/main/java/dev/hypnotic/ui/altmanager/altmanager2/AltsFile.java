package dev.hypnotic.ui.altmanager.altmanager2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.UUID;

import dev.hypnotic.Hypnotic;

public class AltsFile {

	public static AltsFile INSTANCE = new AltsFile();
	public File altsFile = new File(Hypnotic.hypnoticDir, "alts.txt");
	
	public AltsFile() {
		
	}
	
	public void saveAlts() {
		ArrayList<String> credentials = new ArrayList<>();
		if (!altsFile.exists()) {
			try {
				altsFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		for (Alt alt : AltManagerScreen.INSTANCE.alts) {
			if (!alt.getPassword().equalsIgnoreCase("cracked")) credentials.add(alt.getEmail() + ":" + alt.getPassword() + ":" + alt.getUsername() + ":" + alt.getUuid());
			else credentials.add(alt.getEmail() + ":cracked");
		}
		
		try {
            PrintWriter pw = new PrintWriter(altsFile);
            for (String alt : credentials) {
                pw.println(alt);
            }
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
	}
	
	public void loadAlts() {
		ArrayList<String> lines = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(altsFile));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String s : lines) {
            String[] args = s.split(":");
            if (!args[1].equalsIgnoreCase("cracked")) {
	            Alt alt = new Alt(args[0], args[1], AltManagerScreen.INSTANCE.alts.size());
	            try {
	            	if (args[2] != null) alt.setUsername(args[2]);
	            	if (args[3] != null && !args[3].equalsIgnoreCase("null") && !args[3].equalsIgnoreCase("")) alt.setUuid(UUID.fromString(args[3]));
	            } catch(Exception e) {
	            	e.printStackTrace();
	            }
	            AltManagerScreen.INSTANCE.alts.add(alt);
            } else {
            	Alt alt = new Alt(args[0], "cracked", AltManagerScreen.INSTANCE.alts.size());
	            if (args[0] != null) alt.setUsername(args[0]);
	            AltManagerScreen.INSTANCE.alts.add(alt);
            }
        }
	}
}