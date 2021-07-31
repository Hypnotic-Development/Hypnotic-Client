package badgamesinc.hypnotic.module.combat;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;

public class Criticals extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Packet", "Packet");
	
	public Criticals() {
		super("Criticals", "Attacks select surrounding entities", Category.COMBAT);
		addSettings(mode);
	}
	
	@Override
	public void onTick(){
		
		super.onTick();
	}

}
