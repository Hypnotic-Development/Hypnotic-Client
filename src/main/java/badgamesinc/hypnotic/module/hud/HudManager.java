package badgamesinc.hypnotic.module.hud;

import java.util.ArrayList;

import badgamesinc.hypnotic.module.hud.elements.*;

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
				new Packets()
			);
	}
	
	
	public void registerHudElements(HudModule... elements) {
		for (HudModule element : elements) {
			hudModules.add(element);
		}
	}
}
