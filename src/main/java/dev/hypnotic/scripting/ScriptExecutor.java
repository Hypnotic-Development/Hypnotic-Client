package dev.hypnotic.scripting;

import java.io.File;
import java.io.FileReader;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ScriptExecutor {

	// fuck graal.js
	private static ScriptEngine engine = new ScriptEngineManager().getEngineByName("graal.js");
	
	public static void executeFunction(File script, String functionName, Object args) {
		System.out.println(script.getName());
		try {
			engine.eval(new FileReader(script));
			Invocable invocable = (Invocable)engine;

			Object result = invocable.invokeFunction("fun1", args);
			System.out.println(result + " sdfsdfsfsdf");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void executeFunction(String script, String functionName, Object args) {
		try {
			engine.eval(script);
			Invocable invocable = (Invocable)engine;

			Object result = invocable.invokeFunction(functionName, args);
			System.out.println(result + " sdfsdfsfsdf");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ScriptEngine getEngine() {
		return ScriptExecutor.engine;
	}
}
