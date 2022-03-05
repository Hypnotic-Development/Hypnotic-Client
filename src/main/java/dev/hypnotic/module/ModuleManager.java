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
package dev.hypnotic.module;

import java.util.ArrayList;

import dev.hypnotic.module.combat.AutoCity;
import dev.hypnotic.module.combat.AutoTotem;
import dev.hypnotic.module.combat.Criticals;
import dev.hypnotic.module.combat.CrystalAura;
import dev.hypnotic.module.combat.Killaura;
import dev.hypnotic.module.combat.Offhand;
import dev.hypnotic.module.combat.TargetStrafe;
import dev.hypnotic.module.combat.Velocity;
import dev.hypnotic.module.exploit.AntiHunger;
import dev.hypnotic.module.exploit.Disabler;
import dev.hypnotic.module.exploit.EntityControl;
import dev.hypnotic.module.exploit.InvDupe;
import dev.hypnotic.module.exploit.OffhandCrash;
import dev.hypnotic.module.exploit.PortalGui;
import dev.hypnotic.module.exploit.ResourcePackSpoof;
import dev.hypnotic.module.exploit.ServerCrasher;
import dev.hypnotic.module.hud.HudManager;
import dev.hypnotic.module.hud.HudModule;
import dev.hypnotic.module.misc.DiscordRPCModule;
import dev.hypnotic.module.misc.FakePlayer;
import dev.hypnotic.module.misc.Nuker;
import dev.hypnotic.module.misc.PCPinger;
import dev.hypnotic.module.misc.SoundBlocker;
import dev.hypnotic.module.misc.Surround;
import dev.hypnotic.module.misc.Timer;
import dev.hypnotic.module.movement.ElytraFly;
import dev.hypnotic.module.movement.Flight;
import dev.hypnotic.module.movement.FlightBlink;
import dev.hypnotic.module.movement.InvMove;
import dev.hypnotic.module.movement.Phase;
import dev.hypnotic.module.movement.Speed;
import dev.hypnotic.module.movement.Sprint;
import dev.hypnotic.module.movement.Step;
import dev.hypnotic.module.player.AirPlace;
import dev.hypnotic.module.player.AntiVoid;
import dev.hypnotic.module.player.AutoArmor;
import dev.hypnotic.module.player.Blink;
import dev.hypnotic.module.player.EntityDesync;
import dev.hypnotic.module.player.ExpFast;
import dev.hypnotic.module.player.MiddleClickFriend;
import dev.hypnotic.module.player.NoFall;
import dev.hypnotic.module.player.NoSlow;
import dev.hypnotic.module.player.PacketCanceller;
import dev.hypnotic.module.player.Scaffold;
import dev.hypnotic.module.player.Spammer;
import dev.hypnotic.module.player.SpeedMine;
import dev.hypnotic.module.render.ArmCustomize;
import dev.hypnotic.module.render.BlockOutline;
import dev.hypnotic.module.render.Cape;
import dev.hypnotic.module.render.ChatImprovements;
import dev.hypnotic.module.render.CityESP;
import dev.hypnotic.module.render.ClickGUIModule;
import dev.hypnotic.module.render.CustomFont;
import dev.hypnotic.module.render.ESP;
import dev.hypnotic.module.render.Freecam;
import dev.hypnotic.module.render.Fullbright;
import dev.hypnotic.module.render.HoleESP;
import dev.hypnotic.module.render.Nametags;
import dev.hypnotic.module.render.NewChunks;
import dev.hypnotic.module.render.NoRender;
import dev.hypnotic.module.render.OldBlock;
import dev.hypnotic.module.render.ParticleBlocker;
import dev.hypnotic.module.render.Search;
import dev.hypnotic.module.render.SpinBot;
import dev.hypnotic.module.render.StorageESP;
import dev.hypnotic.module.render.TabGUI;
import dev.hypnotic.module.render.Tracers;
import dev.hypnotic.module.render.Waypoints;
import dev.hypnotic.module.render.Xray;
import dev.hypnotic.ui.OptionsScreen;

public class ModuleManager {

	public ArrayList<Mod> modules = new ArrayList<>();
	
	// Here to prevent a NullPointerException in various mixins
	public static final ModuleManager INSTANCE = new ModuleManager();
	
	public ArrayList<Mod> getModules() {
		return modules;
	}
	
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
		for (Mod m : modules){
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
		
	}
	
	public void loadModules() {
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
				new ElytraFly(),
				new Tracers(),
				new EntityDesync(),
				new PacketCanceller(),
				new Search(),
				new MiddleClickFriend(),
				new EntityControl(),
				new CustomFont(),
				new AntiHunger(),
				new ArmCustomize(),
				new ParticleBlocker(),
				new Nuker(),
				new Phase(),
				new TargetStrafe(),
				new NewChunks(),
				new ExpFast(),
				new CrystalAura(),
				new CityESP(),
				new AutoCity(),
				new HoleESP(),
				new AutoArmor(),
				new SoundBlocker(),
				new Offhand(),
				new NoRender(),
				new PortalGui(),
				new Cape(),
				new InvDupe(),
				new Waypoints(),
				new BlockOutline(),
				new AirPlace(),
				new DiscordRPCModule(),
				new FlightBlink(),
				new PCPinger(),
				new ResourcePackSpoof(),
				new ServerCrasher()
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
	
	//All modules including hud modules
	public ArrayList<Mod> getAllModules() {
		ArrayList<Mod> mods = new ArrayList<>();
		for (Mod mod : modules) {
			mods.add(mod);
		}
		for (HudModule hudMod : HudManager.INSTANCE.hudModules) {
			mods.add(hudMod);
		}
		mods.add(OptionsScreen.INSTANCE.options);
		return mods;
	}
}
