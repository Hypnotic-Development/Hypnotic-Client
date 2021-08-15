package badgamesinc.hypnotic.ui.altmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.ui.altmanager.account.Account;
import badgamesinc.hypnotic.ui.altmanager.account.Accounts;
import badgamesinc.hypnotic.ui.altmanager.account.types.PremiumAccount;

public class AltsFile {

	public static AltsFile INSTANCE = new AltsFile();
	public File altsFile = new File(Hypnotic.hypnoticDir, "alts.txt");
	
	public AltsFile() {
		
	}
	
	public void saveAlts() {
		ArrayList<String> credentials = new ArrayList<>();
		if (!altsFile.exists()) {
			altsFile.mkdirs();
		}
		
		for (Account<?> alt : Accounts.get()) {
			if (alt instanceof PremiumAccount) {
				PremiumAccount premAlt = (PremiumAccount)alt;
				credentials.add(alt.getUsername() + ":" + premAlt.getPassword());
			}
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
            PremiumAccount alt = new PremiumAccount(args[0], args[1]);
            Accounts.get().add(alt);;
        }
	}
}
