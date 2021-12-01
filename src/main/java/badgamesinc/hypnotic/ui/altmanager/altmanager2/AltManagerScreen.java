package badgamesinc.hypnotic.ui.altmanager.altmanager2;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import com.mojang.authlib.exceptions.AuthenticationException;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.ui.Button;
import badgamesinc.hypnotic.ui.HypnoticScreen;
import badgamesinc.hypnotic.ui.altmanager.account.Accounts;
import badgamesinc.hypnotic.ui.altmanager.account.MicrosoftLogin;
import badgamesinc.hypnotic.ui.altmanager.account.types.MicrosoftAccount;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.util.Session;
import net.minecraft.client.util.math.MatrixStack;

public class AltManagerScreen extends HypnoticScreen {
	
	private String status;
	@SuppressWarnings("unused")
	private boolean loggingIn = false;
	private Screen previousScreen = new TitleScreen();
	private Button add = new Button("Add alt", 54321, this.width / 2 - 100, height - 50, 200, 20, false);
	private Button back = new Button("Back", 12345, this.width / 2 - 100, height - 25, 200, 20, false);
	private Button msLogin = new Button("Microsoft Login", 69420, this.width / 2 - 310, height - 25, 200, 20, false);
	private Button login = new Button("Login", 1337, this.width / 2 - 310, height - 50, 200, 20, false);
	private Button remove = new Button("Remove", 8008, this.width / 2 + 210, height - 25, 200, 20, false);
	private Button cracked = new Button("Add cracked", 800, this.width / 2 + 210, height - 25, 200, 20, false);
	private Button sessionID = new Button("Use sessionID", 800, this.width / 2 + 210, height - 25, 200, 20, false);
	private Alt selectedAlt = null;
	
	@Override
	public void tick() {
		super.tick();
	}
	
	public AltManagerScreen() {
	}
	
	public static AltManagerScreen INSTANCE = new AltManagerScreen();
	public File altsFile = new File(Hypnotic.hypnoticDir, "alts.txt");
	public ArrayList<Alt> alts = new ArrayList<Alt>();
	
	int scrollY;
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		DrawableHelper.fill(matrices, 0, 0, width, height, new Color(20, 20, 20).getRGB());
		RenderUtils.drawBorderRect(matrices, 1, 1, width - 1, height - 1, ColorUtils.defaultClientColor, 1);
		fill(matrices, 100, 70, width - 100, height - 70, new Color(0, 0, 0, 80).getRGB());
		font.drawWithShadow(matrices, MinecraftClient.getInstance().getSession().getProfile().getName(), 20, 20, -1);
		font.drawCenteredString(matrices, status, width / 2, 20, -1, true);
		int offset = 0;
		RenderUtils.startScissor(100, 70, width - 100, height - 140);
		RenderUtils.fill(matrices, width - 100, 50 - scrollY, width - 104, 80 - scrollY, Color.DARK_GRAY.getRGB());
		for (Alt alt : alts) {
			alt.setX(120);
			alt.setY(110 + offset);
			RenderUtils.drawBorderRect(matrices, alt.getX(), alt.getY() + scrollY - 1, alt.getX() + 130, alt.getY() + scrollY + 33, ColorUtils.defaultClientColor, 1);
			fill(matrices, alt.getX(), alt.getY() + scrollY, alt.getX() + 130, alt.getY() + scrollY + 32, new Color(0, 0, 0, 120).getRGB());
			if (hovered(mouseX, mouseY, alt.getX(), alt.getY() + scrollY, alt.getX() + 130, alt.getY() + 30 + scrollY)) {
				fill(matrices, alt.getX(), alt.getY() + scrollY, alt.getX() + 130, alt.getY() + scrollY + 32, new Color(40, 40, 40, 120).getRGB());
			}
			if (selectedAlt == alt) {
				fill(matrices, alt.getX(), alt.getY() + scrollY, alt.getX() + 130, (int) (alt.getY() + scrollY + 32), new Color(50, 50, 50, 150).getRGB());
			}
			font.drawWithShadow(matrices, !alt.getUsername().equalsIgnoreCase("null") && !alt.getUsername().equalsIgnoreCase("") ? alt.getUsername() : alt.getEmail(), alt.getX() + 37, alt.getY() + scrollY + 5, -1);
			font.drawWithShadow(matrices, alt.getPassword().replaceAll("(?s).", "*"), alt.getX() + 37, alt.getY() + scrollY + 20, -1);
			alt.drawFace(matrices, alt.getX(), alt.getY() + scrollY);
			offset+=50;
		}
		RenderUtils.endScissor();
		add.setX(this.width / 2 - 100);
		add.setY(height - 50);
		cracked.setX(this.width / 2 + 110);
		cracked.setY(height - 50);
		back.setX(this.width / 2 - 100);
		back.setY(height - 25);
		msLogin.setX(this.width / 2 - 310);
		msLogin.setY(height - 25);
		remove.setX(width / 2 + 110);
		remove.setY(height - 25);
		login.setX(width / 2 - 310);
		login.setY(height - 50);
		login.render(matrices, mouseX, mouseY, delta);
		remove.render(matrices, mouseX, mouseY, delta);
		add.render(matrices, mouseX, mouseY, delta);
		back.render(matrices, mouseX, mouseY, delta);
		msLogin.render(matrices, mouseX, mouseY, delta);
		cracked.render(matrices, mouseX, mouseY, delta);
		if (selectedAlt == null) {
			login.enabled = false;
			remove.enabled = false;
		} else {
			login.enabled = true;
			remove.enabled = true;
		}
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	protected void init() {
		status = "Idle...";
		alts.clear();
		this.buttons.clear();
		this.addButton(login);
		this.addButton(remove);
		this.addButton(add);
		this.addButton(back);
		this.addButton(msLogin);
		this.addButton(cracked);
		this.addButton(sessionID);
		AltsFile.INSTANCE.loadAlts();
		super.init();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		for (Alt alt : alts) {
			if (hovered((int)mouseX, (int)mouseY, alt.getX(), alt.getY() + scrollY, alt.getX() + 130, alt.getY() + 30 + scrollY)) {
				selectedAlt = alt;
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void buttonClicked(Button button) {
		if (selectedAlt != null) {
			if (button.getId() == 1337) {
				try {
					status = "Logging into " + selectedAlt.getEmail();
					loggingIn = true;
					if (!selectedAlt.getPassword().equalsIgnoreCase("cracked")) selectedAlt.login();
					else selectedAlt.setSession(new Session(selectedAlt.getUsername(), "", "", null, null, Session.AccountType.MOJANG));
					status = "Logged into " + ColorUtils.green + "\"" + selectedAlt.getUsername() + "\"";
					loggingIn = false;
					AltsFile.INSTANCE.saveAlts();
				} catch (AuthenticationException e) {
					e.printStackTrace();
				}
			} else if (button.getId() == 8008) {
				if (alts.contains(selectedAlt)) {
					try {
						alts.remove(selectedAlt);
						status = "Removed " + ColorUtils.red + selectedAlt.getUsername();
						AltsFile.INSTANCE.saveAlts();
					} catch(Exception e) {
						status = "Error removing alt";
						e.printStackTrace();
					}
				}
			}
		}
		if (button.getId() == 69420) {
			MicrosoftLogin.getRefreshToken(refreshToken -> {
              if (refreshToken != null) {
                  MicrosoftAccount account = new MicrosoftAccount(refreshToken);
                  account.login();
                  Accounts.get().add(account);
                  status = "Logged into " + ColorUtils.green + "\"" + account.getUsername() + "\"";
              }
			});
		}
		if (button.getId() == 12345) {
			MinecraftClient.getInstance().setScreen(previousScreen);
		}
		if (button.getId() == 54321) {
			MinecraftClient.getInstance().setScreen(new AddAltScreen(this));
		}
		if (button.getId() == 800) {
			MinecraftClient.getInstance().setScreen(new AddCrackedAltScreen(this));
		}
		super.buttonClicked(button);
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		if (amount > 0 && scrollY < -20) {
			scrollY+=5;
		}  else if (amount < 0) {
			scrollY-=5;
		}
		
//		if (amount < 0 && scrollY > -alts.size() * 15) {
//			scrollY-=5;
//		}
		return super.mouseScrolled(mouseX, mouseY, amount);
	}
	
	public boolean hovered(int mouseX, int mouseY, int x1, int y1, int x2, int y2) {
		return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2 && !(y1 > height - 70);
	}
}
