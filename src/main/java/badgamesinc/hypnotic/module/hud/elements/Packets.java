package badgamesinc.hypnotic.module.hud.elements;

import java.util.ArrayList;
import java.util.List;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventSendPacket;
import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import net.minecraft.client.util.math.MatrixStack;

public class Packets extends HudModule {

	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	
	private List<Long> packets = new ArrayList<>();
	private long lastSent;
	
	public Packets() {
		super("Packets/s", "Shows how many packets a second you are sending", 4, 50, 1, 1);
		addSettings(color);
	}
	
	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		font.drawWithShadow(matrices, "Packets/s " + ColorUtils.gray + getPacketsPerSecond(), this.getX(), this.getY(), color.getRGB());
		this.setWidth(font.getStringWidth("Packets/s " + ColorUtils.gray + getPacketsPerSecond()));
		this.setHeight(font.getStringHeight("Packets/s " + ColorUtils.gray + getPacketsPerSecond()));
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
	
	private int getPacketsPerSecond() {
		try {
			final long time = System.currentTimeMillis();
			packets.removeIf(aLong -> aLong + 1000 < time);
			return this.packets.size();
		} catch(Exception e) {
			return 0;
		}
	}
	
	@EventTarget
	public void onSendPacket(EventSendPacket event) {
		lastSent = System.currentTimeMillis();
		packets.add(lastSent);
	}
}
