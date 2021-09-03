package badgamesinc.hypnotic.ui.altmanager.altmanager2;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.blaze3d.systems.RenderSystem;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.ui.altmanager.account.Accounts;
import badgamesinc.hypnotic.ui.altmanager.account.MicrosoftLogin;
import badgamesinc.hypnotic.ui.altmanager.account.types.MicrosoftAccount;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class AltManagerScreen extends Screen {
	
	private String status;
	@SuppressWarnings("unused")
	private boolean loggingIn = false;
	private Screen previousScreen = new TitleScreen();
	
	@Override
	public void tick() {
		super.tick();
	}
	
	public AltManagerScreen() 
	{
		super(new LiteralText("AltManager"));
	}
	public AltManagerScreen(Screen prevScreen) 
	{
		super(new LiteralText("AltManager"));
		previousScreen = prevScreen;
	}
	
	public static AltManagerScreen INSTANCE = new AltManagerScreen();
	public File altsFile = new File(Hypnotic.hypnoticDir, "alts.txt");
	public ArrayList<Alt> alts = new ArrayList<Alt>();
	
	int scrollY;
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		fill(matrices, 100, 70, width - 100, height - 70, new Color(0, 0, 0, 80).getRGB());
		drawStringWithShadow(matrices, textRenderer, MinecraftClient.getInstance().getSession().getProfile().getName(), 20, 20, -1);
		RenderUtils.drawCenteredStringWithShadow(matrices, textRenderer, status, width / 2, 20, -1);
		((ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, height - 50, 200, 20, new LiteralText("Add alt"), (button) -> {
	         MinecraftClient.getInstance().setScreen(new AddAltScreen(this));
	    }))).active = true;
		RenderUtils.drawCenteredStringWithShadow(matrices, textRenderer, status, width / 2, 20, -1);
		((ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, height - 25, 200, 20, new LiteralText("Back"), (button) -> {
	         MinecraftClient.getInstance().setScreen(previousScreen);
	    }))).active = true;
		int offset = 0;
		RenderSystem.enableScissor(100, 70 * 2, (width - 100) * 2, (height - 70) * 2);
//		GL11.glScissor(100, 10, 10000, 10000);
		for (Alt alt : alts) {
			alt.setX(120);
			alt.setY(110 + offset);
			fill(matrices, alt.getX(), alt.getY() + scrollY, alt.getX() + 230, alt.getY() + scrollY + 30, new Color(0, 0, 0, 120).getRGB());
			if (alt.hoveredAlt(mouseX, mouseY)) {
				fill(matrices, alt.getX(), alt.getY() + scrollY, alt.getX() + 230, alt.getY() + scrollY + 30, new Color(40, 40, 40, 120).getRGB());
			}
			if (alt.isSelected()) {
				fill(matrices, alt.getX(), alt.getY() + scrollY, alt.getX() + 230, (int) (alt.getY() + scrollY + 30), new Color(50, 50, 50, 150).getRGB());
			}
			((ButtonWidget)this.addDrawableChild(new ButtonWidget(alt.getX() + 180, alt.getY() + 5, 40, 20, new LiteralText("Login"), (button) -> {
				try {
					status = "Logging into " + ColorUtils.green + alt.getEmail();
					loggingIn = true;
					alt.login();
					status = "Logged into " + alt.getUsername();
					loggingIn = false;
				} catch (AuthenticationException e) {
					e.printStackTrace();
				}
		    }))).visible = true;
			((ButtonWidget)this.addDrawableChild(new ButtonWidget(alt.getX() + 180, alt.getY() + 5, 40, 20, new LiteralText("Edit"), (button) -> {
				MinecraftClient.getInstance().setScreen(new EditAltScreen(this, alt));
		    }))).visible = false;
			drawStringWithShadow(matrices, textRenderer, alt.getEmail(), alt.getX() + 5, alt.getY() + scrollY + 5, -1);
			drawStringWithShadow(matrices, textRenderer, alt.getPassword().replaceAll("(?s).", "*"), alt.getX() + 5, alt.getY() + scrollY + 20, -1);
			offset+=50;
		}
		RenderSystem.disableScissor();
		((ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 - 310, height - 25, 200, 20, new LiteralText("Login to Microsoft account"), (button) -> {
			MicrosoftLogin.getRefreshToken(refreshToken -> {
                if (refreshToken != null) {
                    MicrosoftAccount account = new MicrosoftAccount(refreshToken);
                    account.login();
                    Accounts.get().add(account);
                    status = "Logged into " + ColorUtils.green + "\"" + account.getUsername() + "\"";
                }
            });
	    }))).active = true;
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
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		scrollY+=amount;
		return super.mouseScrolled(mouseX, mouseY, amount);
	}
	
	public boolean hovered(int mouseX, int mouseY, int x1, int y1, int x2, int y2) {
		return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
	}
}
