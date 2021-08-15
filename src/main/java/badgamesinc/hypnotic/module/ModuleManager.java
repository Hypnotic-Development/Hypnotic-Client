package badgamesinc.hypnotic.module;

import java.util.ArrayList;

import badgamesinc.hypnotic.module.combat.*;
import badgamesinc.hypnotic.module.movement.*;
import badgamesinc.hypnotic.module.player.*;
import badgamesinc.hypnotic.module.render.*;
import badgamesinc.hypnotic.module.world.*;

public class ModuleManager {

	public ArrayList<Mod> modules = new ArrayList<>();
	/*
	 * Here to prevent a NullPointerException in various mixins
	 */
	public static ModuleManager INSTANCE = new ModuleManager();
	
	public ArrayList<Mod> getEnabledModules() {
		ArrayList<Mod> enabledModules = new ArrayList<>();
		for (Mod module : modules) {
			if (!module.isEnabled())
				continue;
			enabledModules.add(module);
		}
		return enabledModules;
	}
	
	public ArrayList<Mod> getModulesInCategory(Category category){
		ArrayList<Mod> categoryModules = new ArrayList<>();
		for(Mod m : modules){
		    if (m.getCategory() == category){
			categoryModules.add(m);
		    }
		}
		return categoryModules;
    }
	
	public void registerModule(Mod module) {
		modules.add(module);
	}
	
	public void registerModules(Mod... modules) {
		for (Mod module : modules) {
			this.modules.add(module);
		}
	}
	
	public ModuleManager() {
		registerModules(
					new Flight(),
					new Killaura(),
					new ClickGUIModule(),
					new Criticals(),
					new Sprint(),
					new Fullbright(),
					new Speed(),
					new Step(),
					new Velocity(),
					new Timer(),
					new NoFall(),
					new Scaffold(),
					new SpeedMine(),
					new Disabler(),
					new SpinBot(),
					new Spammer(),
					new ESP(),
					new AntiVoid(),
					new StorageESP(),
					new Nametags(),
					new Blink(),
					new Freecam(),
					new OffhandCrash(),
					new ChatImprovements(),
					new InvMove(),
					new NoSlow(),
					new OldBlock(),
					new TabGUI(),
					new AutoTotem(),
					new Xray()
				);
	}
	
	public Mod getModuleByName(String moduleName) {
		for(Mod mod : modules) {
			if ((mod.getName().trim().equalsIgnoreCase(moduleName)) || (mod.toString().trim().equalsIgnoreCase(moduleName.trim()))) {
				return mod;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Mod> T getModule(Class<T> clazz) {
    	return (T) modules.stream().filter(mod -> mod.getClass() == clazz).findFirst().orElse(null);
	}
}
