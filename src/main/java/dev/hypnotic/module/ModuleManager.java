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

import dev.hypnotic.module.combat.*;
import dev.hypnotic.module.exploit.*;
import dev.hypnotic.module.hud.*;
import dev.hypnotic.module.misc.*;
import dev.hypnotic.module.movement.*;
import dev.hypnotic.module.player.*;
import dev.hypnotic.module.render.*;
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
				new ServerCrasher(),
				new IRC(),
				new TrueSight(),
				new AntiCactus(),
				new ChestStealer(),
				new LightningLocator(),
				new FightBot(),
				new AntiSculkSensor()
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
