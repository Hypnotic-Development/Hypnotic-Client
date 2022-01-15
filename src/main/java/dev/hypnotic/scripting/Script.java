package dev.hypnotic.scripting;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;

public class Script {

	private Context context;
	public String name, description, author;
	private File scriptFile;
	private Map<String, Value> events = new HashMap<>();
	private MinecraftClient mc = MinecraftClient.getInstance();
	
	public Script(File scriptFile) {
		this.scriptFile = scriptFile;

		context = Context.newBuilder().allowExperimentalOptions(true).option("js.nashorn-compat", "true").option("engine.WarnInterpreterOnly", "false").allowHostAccess(HostAccess.ALL).build();
		
		context.getBindings("js").putMember("mc", mc);
		context.getBindings("js").putMember("hypnotic", Hypnotic.INSTANCE);
		context.getBindings("js").putMember("player", mc.player);
		context.getBindings("js").putMember("renderer", RenderUtils.INSTANCE);
		context.getBindings("js").putMember("createScript", this);
	}
	

	public void onLoad(Value function) {
		onEvent("load", function);
	}
	
	public void onEnable(Value function) {
		onEvent("enable", function);
	}
	
	public void onDisable(Value function) {
		onEvent("disable", function);
	}
	
	public void load() throws ScriptException, IOException {
		context.eval(Source.newBuilder("js", scriptFile).build());
		executeEvent("load");
	}
	
	private void onEvent(String event, Value function) {
		events.put(event, function);
	}
	
	private void executeEvent(String event) {
		if (events.containsKey(event)) events.get(event).executeVoid();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getAuthor() {
		return this.author;
	}
	
	public File getScriptFile() {
		return scriptFile;
	}
}
