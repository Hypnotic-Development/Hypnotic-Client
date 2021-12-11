package dev.hypnotic.hypnotic_client.module.hud;

import java.util.ArrayList;

import dev.hypnotic.hypnotic_client.module.hud.elements.*;
import dev.hypnotic.hypnotic_client.module.render.TargetHUD;

public class HudManager {

	public static HudManager INSTANCE = new HudManager();
	public ArrayList<HudModule> hudModules = new ArrayList<>();
	
	public HudManager() {
		registerHudElements(
				new Radar(),
				new TPS(),
				new FPS(),
				new Ping(),
				new BPS(),
				new XYZ(),
				new NetherXYZ(),
				new Armor(),
				new Logo(),
				new ArrayListModule(),
				new Packets(),
				new Doll(),
				new TargetHUD()
			);
	}
	
	
	public void registerHudElements(HudModule... elements) {
		for (HudModule element : elements) {
			hudModules.add(element);
		}
	}
}
