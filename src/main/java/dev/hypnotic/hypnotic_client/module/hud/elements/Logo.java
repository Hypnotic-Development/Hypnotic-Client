package dev.hypnotic.hypnotic_client.module.hud.elements;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import dev.hypnotic.hypnotic_client.Hypnotic;
import dev.hypnotic.hypnotic_client.module.hud.HudModule;
import dev.hypnotic.hypnotic_client.settings.settingtypes.BooleanSetting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ColorSetting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ModeSetting;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.font.FontManager;
import dev.hypnotic.hypnotic_client.utils.render.RenderUtils;
import net.minecraft.client.util.math.MatrixStack;

public class Logo extends HudModule {

	public ModeSetting mode = new ModeSetting("Mode", "Text", "Text", "Onetap", "Hypnosense");
	public BooleanSetting rainbow = new BooleanSetting("Rainbow Strip", true);
	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	
	public Logo() {
		super("Logo", "Renders the Hypnotic logo", 4, 4, (int) FontManager.robotoMed.getStringWidth(Hypnotic.name + ColorUtils.gray + " " + Hypnotic.version), 4);
		addSettings(mode, rainbow, color);
		this.setEnabled(true);
	}
	
	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
		String hours = (Integer.parseInt(time.split(":")[0]) > 12 || Integer.parseInt(time.split(":")[0]) == 12) && Integer.parseInt(time.split(":")[0]) < 24 ? (Integer.parseInt(time.split(":")[0]) - (Integer.parseInt(time.split(":")[0]) == 12 ? 0 : 12) + ":" + time.split(":")[1] + "pm") : ((Integer.parseInt(time.split(":")[0]) == 24 || Integer.parseInt(time.split(":")[0]) == 0 ? 12 : time.split(":")[0])) + ":" + time.split(":")[1] +  "am";
		String serverIp = mc.getCurrentServerEntry() != null ? mc.getCurrentServerEntry().address : "Singleplayer";
		String text = ColorUtils.white + "Hypno" + ColorUtils.reset + "sense" + ColorUtils.white + " | " + mc.player.getName().getString() + " | " + serverIp + " | " + hours;
		
		switch(mode.getSelected()) {
			case "Text":
				this.setWidth(font.getStringWidth(Hypnotic.name + ColorUtils.gray + " " + Hypnotic.version));
				font.drawWithShadow(matrices, Hypnotic.name + ColorUtils.gray + " " + Hypnotic.version, this.getX(), this.getY(), color.getRGB());
				break;
			case "Hypnosense":
				this.setWidth(font.getStringWidth(text) - (font.mcFont ? -2 : 8) + 10);
				this.setHeight((int) font.getStringHeight(Hypnotic.fullName) + 10);
				RenderUtils.fill(matrices, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), new Color(65, 65, 65).getRGB());
				RenderUtils.fill(matrices, this.getX() + 3, this.getY() + 3, this.getX() + this.getWidth() - 3, this.getY() + this.getHeight() - 3, new Color(35, 35, 35).getRGB());
				RenderUtils.fill(matrices, this.getX(), this.getY(), this.getX() + 1, this.getY() + this.getHeight(), new Color(35, 35, 35).getRGB());
				RenderUtils.fill(matrices, this.getX() + this.getWidth(), this.getY(), this.getX() + this.getWidth() - 1, this.getY() + this.getHeight(), new Color(35, 35, 35).getRGB());
				RenderUtils.fill(matrices, this.getX(), this.getY() + 1, this.getX() + this.getWidth(), this.getY(), new Color(35, 35, 35).getRGB());
				RenderUtils.fill(matrices, this.getX(), this.getY() + this.getHeight(), this.getX() + this.getWidth(), this.getY() + this.getHeight() - 1, new Color(35, 35, 35).getRGB());
				for (int i = 3; i < this.getWidth() - 3; i++) {
					RenderUtils.fill(matrices, this.getX() + i + 1, this.getY() + 3, this.getX() + i, this.getY() + 4, rainbow.isEnabled() ? ColorUtils.rainbow(6, 0.6f, 1, i * 10) : color.getColor().getRGB());
				}
				font.drawWithShadow(matrices, text, this.getX() + 5, this.getY() + 4.5f, color.getRGB());
				break;
			case "Onetap":
				text = ColorUtils.white + Hypnotic.fullName + " | " + mc.player.getName().getString() + " | " + serverIp + " | " + hours;
				this.setWidth(font.getStringWidth(text) + 4);
				this.setHeight((int) font.getStringHeight(Hypnotic.fullName) + 5);
				RenderUtils.fill(matrices, this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), new Color(35, 35, 35, 190).getRGB());
				for (int i = 0; i < this.getWidth(); i++) {
					RenderUtils.fill(matrices, this.getX() + i + 1, this.getY(), this.getX() + i, this.getY() + 1, rainbow.isEnabled() ? ColorUtils.rainbow(6, 0.6f, 1, i * 10) : color.getColor().getRGB());
				}
				font.drawWithShadow(matrices, text, this.getX() + 2, this.getY() + 2, color.getRGB());
		}
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
	
	@Override
	public void onTickDisabled() {
		rainbow.setVisible(!mode.is("Text"));
		super.onTickDisabled();
	}
}
