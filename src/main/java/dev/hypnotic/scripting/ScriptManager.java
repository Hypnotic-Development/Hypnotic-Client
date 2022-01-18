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
package dev.hypnotic.scripting;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.common.collect.Lists;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.module.ModuleManager;

/**
* @author BadGamesInc
*/
public class ScriptManager {

	public static ScriptManager INSTANCE = new ScriptManager();
	private ArrayList<Script> scripts;
	private File scriptsFolder = new File(Hypnotic.scriptDir);
	
	public ScriptManager() {
		makeScriptsFolder();
		scripts = new ArrayList<>();
		refreshScripts();
	}
	
	public void makeScriptsFolder() {
		if (!scriptsFolder.exists()) {
			scriptsFolder.mkdirs();
		}
	}
	
	public void refreshScripts() {
		Hypnotic.LOGGER.info("Refreshing scripts");
		List<String> enabledScripts = Lists.newArrayList();
		scripts.forEach(script -> {
			enabledScripts.add(script.getName());
			script.setEnabled(false);
		});
		ModuleManager.INSTANCE.modules.removeAll(scripts);
		scripts.clear();
		System.out.println(scripts.size());
		for (File script : scriptsFolder.listFiles()) {
			registerScript(script);
		}
		ModuleManager.INSTANCE.modules.addAll(scripts);
		enabledScripts.forEach(name -> getScriptByName(name).setEnabled(true));
		enabledScripts.clear();
	}
	
	// Just in case some retard can't read someone's poorly made session grabber
	private boolean containsMaliciousLine(File scriptFile) {
		try {
			Scanner scanner = new Scanner(scriptFile);
			int lineNum = 1;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (line.contains("getSessionId") || line.contains("getAccessToken")) {
					scanner.close();
					Hypnotic.LOGGER.warn("[ScriptManager] Found malicious line in " + scriptFile.getName() + " at line " + lineNum);
					Hypnotic.LOGGER.warn("[ScriptManager] Malicious code: " + line);
					Hypnotic.LOGGER.warn("[ScriptManager] Skipping malicious script");
					return true;
				}
				lineNum++;
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean registerScript(File scriptFile) {
		if (scriptFile.getName().contains(".js") && !containsMaliciousLine(scriptFile)) {
			Script script = new Script(scriptFile);
			loadScript(script);
			scripts.add(script);
			return true;
		}
		return false;
	}
	
	public void loadScript(Script script) {
		try {
			script.load();
			if (script.getName() == null) script.setName(script.getScriptFile().getName().replaceAll("js", ""));
			if (script.getAuthor() == null) script.setAuthor("No author provided");
			if (script.getDescription() == null) script.setDescription("No description provided");
			Hypnotic.LOGGER.info("[ScriptManager] Loaded: " + script.getName());
		} catch(Exception e) {
			Hypnotic.LOGGER.error("[ScriptManager] Error loading script: " + script.getScriptFile().getName());
			e.printStackTrace();
		}
	}
	
	public ArrayList<Script> getScripts() {
		return this.scripts;
	}
	
	public Script getScriptByName(String scriptName) {
		for (Script script : scripts) {
			if (script.getName().equalsIgnoreCase(scriptName)) return (Script)script;
		}
		return null;
	}
}
