package badgamesinc.hypnotic.ui.altmanager;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import com.mojang.authlib.exceptions.AuthenticationException;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;

public class AltManagerScreen extends Screen {
	
	private String status;
	private boolean loggingIn = false;
	
	protected AltManagerScreen() {
		super(new LiteralText("AltManager"));
	}
	
	public static AltManagerScreen INSTANCE = new AltManagerScreen();
	public File altsFile = new File(Hypnotic.hypnoticDir, "alts.txt");
	public ArrayList<Alt> alts = new ArrayList<Alt>();
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		fill(matrices, 100, 70, width - 100, height - 70, new Color(0, 0, 0, 100).getRGB());
		drawStringWithShadow(matrices, textRenderer, MinecraftClient.getInstance().getSession().getProfile().getName(), 20, 20, -1);
		RenderUtils.drawCenteredStringWithShadow(matrices, textRenderer, status, width / 2, 20, -1);
		((ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, height - 50, 200, 20, new LiteralText("Add alt"), (button) -> {
	         MinecraftClient.getInstance().setScreen(new AddAltScreen(this));
	    }))).active = true;
		
		int offset = 0;
		for (Alt alt : alts) {
			alt.setX(120);
			alt.setY(110 + offset);
			if (alt.hoveredAlt(mouseX, mouseY)) {
				fill(matrices, alt.getX(), alt.getY(), alt.getX() + 200, alt.getY() + 30, new Color(0, 0, 0, 120).getRGB());
			}
			if (alt.isSelected()) {
				fill(matrices, alt.getX(), alt.getY(), alt.getX() + 200, alt.getY() + 30, new Color(0, 0, 0, 120).getRGB());
			}
			drawStringWithShadow(matrices, textRenderer, alt.getEmail(), alt.getX() + 5, alt.getY() + 5, -1);
			drawStringWithShadow(matrices, textRenderer, alt.getPassword().replaceAll("(?s).", "*"), alt.getX() + 5, alt.getY() + 20, -1);
			offset+=50;
		}
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	protected void init() {
		status = "Idle...";
		alts.clear();
		AltsFile.INSTANCE.loadAlts();
		super.init();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (Alt alt : alts) {
			if (alt.hoveredAlt(mouseX, mouseY) && button == 0) {
				alt.setSelected(true);
			} else if (!alt.hoveredAlt(mouseX, mouseY) && button == 0) {
				alt.setSelected(false);
			}
			if (MinecraftClient.getInstance().getSession().getProfile().getName() != alt.getUsername() && !loggingIn && alt.isSelected() && button == 0) {
				try {
					status = "Logging into " + ColorUtils.green + alt.getEmail();
					loggingIn = true;
					alt.login();
					status = "Logged into " + alt.getUsername();
					loggingIn = false;
				} catch (AuthenticationException e) {
					e.printStackTrace();
				}
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
}
