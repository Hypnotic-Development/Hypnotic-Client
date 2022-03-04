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

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.script.ScriptException;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.event.EventManager;
import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventKeyPress;
import dev.hypnotic.event.events.EventMotionUpdate;
import dev.hypnotic.event.events.EventReceiveChat;
import dev.hypnotic.event.events.EventReceivePacket;
import dev.hypnotic.event.events.EventRender3D;
import dev.hypnotic.event.events.EventRenderGUI;
import dev.hypnotic.event.events.EventSendMessage;
import dev.hypnotic.event.events.EventSendPacket;
import dev.hypnotic.event.events.EventTick;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.ColorSetting;
import dev.hypnotic.settings.settingtypes.ModeSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Wrapper;

/**
* @author BadGamesInc
*/
public class Script extends Mod {

	private Context context;
	public String author;
	private File scriptFile;
	private Map<String, Value> events = new HashMap<>();
	
	
	public Script(File scriptFile) {
		super("", "", Category.SCRIPT);
		
		this.scriptFile = scriptFile;

		context = Context.newBuilder().allowExperimentalOptions(true).option("js.nashorn-compat", "true").option("engine.WarnInterpreterOnly", "false").allowHostAccess(HostAccess.ALL).build();
		context.getBindings("js").putMember("mc", mc);
		context.getBindings("js").putMember("hypnotic", Hypnotic.INSTANCE);
		context.getBindings("js").putMember("utils", ScriptUtils.INSTANCE);
		context.getBindings("js").putMember("renderer", new ScriptRenderer());
		context.getBindings("js").putMember("colors", new ColorUtils());
		context.getBindings("js").putMember("newScript", new SetupScript());
	}
	
	protected static Script define(String name, String description, String author) {
		this.name = name;
		this.displayName = name;
		this.description = description;
		this.author = author;
		return this;
	}

	public class SetupScript extends Function<Map<String, Object>, Script> {
		@Override
        public Script apply(Map<String, Object> script) {
            name = (String)script.get("name");
			displayName = name;
            author = (String)script.get("author");
			description = (String)script.get("description");

            return Script.define(name, description, author);
        }
    }
	
	public void sendChatMessage(String message, boolean prefix) {
		if (prefix) Wrapper.tellPlayer(message);
		else Wrapper.tellPlayerRaw(message);
	}
	
	public BooleanSetting booleanSetting(String name, boolean defaultValue) {
		return new BooleanSetting(name, defaultValue);
	}
	
	public NumberSetting numberSetting(String name, double defaultValue, double minValue, double maxValue, double increment) {
		return new NumberSetting(name, defaultValue, minValue, maxValue, increment);
	}
	
	public ModeSetting modeSetting(String name, String defaultValue, String... modes) {
		return new ModeSetting(name, defaultValue, modes);
	}
	
	public ColorSetting colorSetting(String name, Color defaultValue) {
		return new ColorSetting(name, defaultValue);
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthor() {
		return this.author;
	}
	
	public File getScriptFile() {
		return scriptFile;
	}
	
	@Override
	public void setName(String name) {
		this.displayName = name;
		super.setName(name);
	}
	
	// Event stuff
	
	@EventTarget
	private void onTick(EventTick event) {
		executeEvent("tick", event);
	}
	
	@EventTarget
	private void onRender2d(EventRenderGUI event) {
		executeEvent("render2d", event);
	}
	
	@EventTarget
	private void onRender3d(EventRender3D event) {
		executeEvent("render3d", event);
	}
	
	@EventTarget
	private void onMotionUpdate(EventMotionUpdate event) {
		executeEvent("motionUpdate", event);
	}
	
	@EventTarget
	private void onKeyPress(EventKeyPress event) {
		executeEvent("keyPress", event);
	}
	
	@EventTarget
	private void onReceivePacket(EventReceivePacket event) {
		executeEvent("receivePacket", event);
	}
	
	@EventTarget
	private void onSendPacket(EventSendPacket event) {
		executeEvent("sendPacket", event);
	}
	
	@EventTarget
	private void onChatMessage(EventReceiveChat event) {
		executeEvent("onChat");
	}
	
	@EventTarget
	private void onSendChat(EventSendMessage event) {
		executeEvent("sendChat");
	}
	
	public void onSendChat(Value function) {
		executeEvent("sendChat", function);
	}
	
	public void onReceiveChat(Value function) {
		onEvent("onChat", function);
	}
	
	public void onSendPacket(Value function) {
		onEvent("sendPacket", function);
	}
	
	public void onReceivePacket(Value function) {
		onEvent("receivePacket", function);
	}
	
	public void onKeyPress(Value function) {
		onEvent("keyPress", function);
	}
	
	public void onMotionUpdate(Value function) {
		onEvent("motionUpdate", function);
	}
	
	public void onRender2d(Value function) {
		onEvent("render2d", function);
	}
	
	public void onRender3d(Value function) {
		onEvent("render3d", function);
	}
	
	public void onTick(Value function) {
		onEvent("tick", function);
	}
	
	public void onLoad(Value function) {
		onEvent("load", function);
	}
	
	public void onEnable(Value function) {
		onEvent("enable", function);
		super.onEnable();
	}
	
	public void onDisable(Value function) {
		onEvent("disable", function);
		super.onDisable();
	}
	
	public void load() throws ScriptException, IOException {
		context.eval(Source.newBuilder("js", scriptFile).build());
		executeEvent("load");
		EventManager.INSTANCE.register(this);
	}
	
	private void onEvent(String event, Value function) {
		events.put(event, function);
	}
	
	private void executeEvent(String event) {
		if (events.containsKey(event) && canExecute(event)) events.get(event).executeVoid();
	}
	
	private void executeEvent(String event, Object... args) {
		if (events.containsKey(event) && canExecute(event)) events.get(event).execute(args);
	}
	
	private boolean canExecute(String event) {
		if (this.isEnabled()) return true; 
		else return (event.equalsIgnoreCase("load") || event.equalsIgnoreCase("enable") || event.equalsIgnoreCase("disable"));
	}
}
