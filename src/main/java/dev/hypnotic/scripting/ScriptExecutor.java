package dev.hypnotic.scripting;

import java.io.File;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import net.minecraft.client.MinecraftClient;

public class ScriptExecutor {

	private File script;
	
	private ScriptEngine engine;
	
	public ScriptExecutor(File script) {
		this.script = script;
		// fuck graal.js
		engine = new ScriptEngineManager().getEngineByName("graal.js");
	}
	
	@SuppressWarnings("resource")
	public void executeFunction() {
		System.out.println(script.getName());
		try {
			engine.eval(new FileReader(script));
			Invocable invocable = (Invocable)engine;
			
			Object result = invocable.invokeFunction("test", MinecraftClient.getInstance().player);
			System.out.println(result);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
