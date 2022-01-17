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
package dev.hypnotic.ui.altmanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.ui.altmanager.account.Account;
import dev.hypnotic.ui.altmanager.account.Accounts;
import dev.hypnotic.ui.altmanager.account.types.PremiumAccount;

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
