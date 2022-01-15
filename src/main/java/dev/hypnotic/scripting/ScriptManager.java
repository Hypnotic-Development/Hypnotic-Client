package dev.hypnotic.scripting;

import java.io.File;
import java.util.ArrayList;

import dev.hypnotic.Hypnotic;

public class ScriptManager {

	public static ScriptManager INSTANCE = new ScriptManager();
	private ArrayList<Script> scripts;
	private File scriptsFolder = new File(Hypnotic.scriptDir);
	
	public ScriptManager() {
		scripts = new ArrayList<>();
		refreshScripts();
	}
	
	public void makeScriptsFolder() {
		if (!scriptsFolder.exists()) {
			scriptsFolder.mkdirs();
		}
	}
	
	public void refreshScripts() {
		scripts.clear();
		Hypnotic.LOGGER.info("Refreshing scripts");
		for (File script : scriptsFolder.listFiles()) {
			registerScript(script);
		}
	}
	
	public boolean registerScript(File scriptFile) {
		if (scriptFile.getName().contains(".js")) {
			Script script = new Script(scriptFile);
			loadScript(script);
			scripts.add(script);
		}
		return false;
	}
	
	public void loadScript(Script script) {
		try {
			script.load();
			Hypnotic.LOGGER.info("Loaded: " + (script.getName() == null ? script.getScriptFile().getName().replaceAll("js", "") : script.getName()));
		} catch(Exception e) {
			Hypnotic.LOGGER.error("Error loading script: " + script.getScriptFile().getName());
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
