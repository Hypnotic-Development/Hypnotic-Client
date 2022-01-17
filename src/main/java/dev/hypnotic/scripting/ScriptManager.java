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
import java.util.ArrayList;

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
		ModuleManager.INSTANCE.modules.removeAll(scripts);
		scripts.clear();
		Hypnotic.LOGGER.info("Refreshing scripts");
		for (File script : scriptsFolder.listFiles()) {
			registerScript(script);
		}
		ModuleManager.INSTANCE.modules.addAll(scripts);
	}
	
	public boolean registerScript(File scriptFile) {
		if (scriptFile.getName().contains(".js")) {
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
