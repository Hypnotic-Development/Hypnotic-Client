package badgamesinc.hypnotic.module.hud.elements;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.settings.settingtypes.ColorSetting;
import badgamesinc.hypnotic.settings.settingtypes.ModeSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import net.minecraft.client.util.math.MatrixStack;

public class Logo extends HudModule {

	public ModeSetting mode = new ModeSetting("Mode", "Text", "Text", "Onetap", "Hypnosense");
	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	
	public Logo() {
		super("Logo", "Renders the Hypnotic logo", 4, 4, (int) FontManager.robotoMed.getStringWidth(Hypnotic.name + ColorUtils.gray + " " + Hypnotic.version), 4);
		addSettings(mode, color);
		this.setEnabled(true);
	}
	
	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		this.setHeight((int) font.getStringHeight(Hypnotic.fullName));
		switch(mode.getSelected()) {
			case "Text":
				this.setWidth(font.getStringWidth(Hypnotic.name + ColorUtils.gray + " " + Hypnotic.version));
				font.drawWithShadow(matrices, Hypnotic.name + ColorUtils.gray + " " + Hypnotic.version, this.getX(), this.getY(), color.getRGB());
				break;
			case "Hypnosense":
				String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
				String hours = Integer.parseInt(time.split(":")[0]) > 12 ? Integer.parseInt(time.split(":")[0]) - 11 + "" : time.split(":")[0];
				String serverIp = mc.getCurrentServerEntry() != null ? mc.getCurrentServerEntry().address : "Singleplayer";
				String text = ColorUtils.white + "Hypno" + ColorUtils.reset + "sense" + ColorUtils.white + " | " + mc.player.getName().getString() + " | " + serverIp + " | " + time;
				font.drawWithShadow(matrices, text, this.getX(), this.getY(), color.getRGB());
		}
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
}
