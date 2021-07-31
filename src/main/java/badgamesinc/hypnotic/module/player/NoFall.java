package badgamesinc.hypnotic.module.player;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class NoFall extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Packet", "Packet", "Vanilla");
	
    public NoFall() {
        super("NoFall", "Prevents you from taking fall damage", Category.PLAYER);
        addSetting(mode);
    }

    @Override
    public void onTick() {
    	this.setDisplayName("NoFall " + ColorUtils.gray + mode.getSelected());
    	if((mode.is("Packet") || mode.is("Vanilla")) && mc.player.fallDistance >= 2.5)	mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.OnGroundOnly(true));
        super.onTick();
    }
}
