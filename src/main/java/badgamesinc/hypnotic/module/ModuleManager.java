package badgamesinc.hypnotic.module;

import java.util.ArrayList;

import badgamesinc.hypnotic.module.combat.AutoCrystal;
import badgamesinc.hypnotic.module.combat.AutoTotem;
import badgamesinc.hypnotic.module.combat.Criticals;
import badgamesinc.hypnotic.module.combat.CrystalAura;
import badgamesinc.hypnotic.module.combat.Killaura;
import badgamesinc.hypnotic.module.combat.Velocity;
import badgamesinc.hypnotic.module.hud.HudManager;
import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.module.hud.elements.Radar;
import badgamesinc.hypnotic.module.movement.ElytraFly;
import badgamesinc.hypnotic.module.movement.Flight;
import badgamesinc.hypnotic.module.movement.InvMove;
import badgamesinc.hypnotic.module.movement.Speed;
import badgamesinc.hypnotic.module.movement.Sprint;
import badgamesinc.hypnotic.module.movement.Step;
import badgamesinc.hypnotic.module.player.AntiHunger;
import badgamesinc.hypnotic.module.player.AntiVoid;
import badgamesinc.hypnotic.module.player.Blink;
import badgamesinc.hypnotic.module.player.EntityDesync;
import badgamesinc.hypnotic.module.player.MiddleClickFriend;
import badgamesinc.hypnotic.module.player.NoFall;
import badgamesinc.hypnotic.module.player.NoSlow;
import badgamesinc.hypnotic.module.player.OffhandCrash;
import badgamesinc.hypnotic.module.player.PacketCanceller;
import badgamesinc.hypnotic.module.player.Scaffold;
import badgamesinc.hypnotic.module.player.Spammer;
import badgamesinc.hypnotic.module.player.SpeedMine;
import badgamesinc.hypnotic.module.render.ArmCustomize;
import badgamesinc.hypnotic.module.render.ChatImprovements;
import badgamesinc.hypnotic.module.render.ClickGUIModule;
import badgamesinc.hypnotic.module.render.CustomFont;
import badgamesinc.hypnotic.module.render.ESP;
import badgamesinc.hypnotic.module.render.Freecam;
import badgamesinc.hypnotic.module.render.Fullbright;
import badgamesinc.hypnotic.module.render.Nametags;
import badgamesinc.hypnotic.module.render.OldBlock;
import badgamesinc.hypnotic.module.render.ParticleBlocker;
import badgamesinc.hypnotic.module.render.Search;
import badgamesinc.hypnotic.module.render.SpinBot;
import badgamesinc.hypnotic.module.render.StorageESP;
import badgamesinc.hypnotic.module.render.TabGUI;
import badgamesinc.hypnotic.module.render.Tracers;
import badgamesinc.hypnotic.module.render.Xray;
import badgamesinc.hypnotic.module.world.Disabler;
import badgamesinc.hypnotic.module.world.EntityControl;
import badgamesinc.hypnotic.module.world.FakePlayer;
import badgamesinc.hypnotic.module.world.Nuker;
import badgamesinc.hypnotic.module.world.Surround;
import badgamesinc.hypnotic.module.world.Timer;

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
					new Xray(),
					new Surround(),
					new FakePlayer(),
					new AutoCrystal(),
					new CrystalAura(),
					new ElytraFly(),
					new Tracers(),
					new EntityDesync(),
					new PacketCanceller(),
					new Search(),
					new Radar(),
					new MiddleClickFriend(),
					new EntityControl(),
					new CustomFont(),
					new AntiHunger(),
					new ArmCustomize(),
					new ParticleBlocker(),
					new Nuker()
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
	
	public ArrayList<Mod> getAllModules() {
		ArrayList<Mod> mods = new ArrayList<>();
		for (Mod mod : modules) {
			mods.add(mod);
		}
		for (HudModule hudMod : HudManager.INSTANCE.hudModules) {
			mods.add(hudMod);
		}
		return mods;
	}
}
