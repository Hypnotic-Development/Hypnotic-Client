/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.ui.altmanager;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

import com.mojang.authlib.exceptions.AuthenticationException;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.ui.Button;
import dev.hypnotic.ui.HypnoticScreen;
import dev.hypnotic.ui.altmanager.account.Accounts;
import dev.hypnotic.ui.altmanager.account.MicrosoftLogin;
import dev.hypnotic.ui.altmanager.account.types.MicrosoftAccount;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.IRCClient;
import dev.hypnotic.utils.render.RenderUtils;
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
	private Button add = new Button("Add alt", this.width / 2 - 100, height - 50, 200, 20, false, () -> { MinecraftClient.getInstance().setScreen(new AddAltScreen(this)); });
	private Button back = new Button("Back", this.width / 2 - 100, height - 25, 200, 20, false, () -> { MinecraftClient.getInstance().setScreen(previousScreen); });
	private Button msLogin = new Button("Microsoft Login", this.width / 2 - 310, height - 25, 200, 20, false, this::msLogin);
	private Button login = new Button("Login", this.width / 2 - 310, height - 50, 200, 20, false, this::login);
	private Button remove = new Button("Remove", this.width / 2 + 210, height - 25, 200, 20, false, this::remove);
	private Button cracked = new Button("Add cracked", this.width / 2 + 210, height - 25, 200, 20, false, () -> { MinecraftClient.getInstance().setScreen(new AddCrackedAltScreen(this)); });
	private Button sessionID = new Button("Use sessionID", this.width / 2 + 210, height - 25, 200, 20, false, () -> {});
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
		RenderUtils.drawFace(matrices, 15, 15, 4, RenderUtils.getPlayerSkin(MinecraftClient.getInstance().getSession().getProfile().getId()));
		font.drawWithShadow(matrices, MinecraftClient.getInstance().getSession().getProfile().getName(), 55, 20, -1);
		String accountType = MinecraftClient.getInstance().getSession().getProfile().isLegacy() && MinecraftClient.getInstance().getSession().getProfile().getId() != null ? "Mojang" : "Microsoft";
		font.drawWithShadow(matrices, MinecraftClient.getInstance().getSession().getProfile().getId() != null ? accountType : "Cracked", 55, 35, -1);
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
			
			RenderUtils.preStencil();
			
			RenderUtils.renderRoundedQuad(matrices, ColorUtils.defaultClientColor(), alt.getX(), alt.getY() + scrollY, alt.getX() + 32, alt.getY() + 32 + scrollY, 5, 50);
			
			RenderUtils.postStencil();
			
			alt.drawFace(matrices, alt.getX(), alt.getY() + scrollY);
			
			RenderUtils.disableStencil();
			
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
	
	private void login() {
		if (selectedAlt != null) {
			try {
				status = "Logging into " + selectedAlt.getEmail();
				loggingIn = true;
				if (!selectedAlt.getPassword().equalsIgnoreCase("cracked")) selectedAlt.login();
				else selectedAlt.setSession(new Session(selectedAlt.getUsername(), "", "", Optional.empty(), Optional.empty(), Session.AccountType.MOJANG));
				status = "Logged into " + ColorUtils.green + "\"" + selectedAlt.getUsername() + "\"";
				loggingIn = false;
				AltsFile.INSTANCE.saveAlts();
				
				IRCClient.INSTNACE.init("");
			} catch (AuthenticationException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void msLogin() {
		MicrosoftLogin.getRefreshToken(refreshToken -> {
            if (refreshToken != null) {
                MicrosoftAccount account = new MicrosoftAccount(refreshToken);
                account.login();
                Accounts.get().add(account);
                status = "Logged into " + ColorUtils.green + "\"" + account.getUsername() + "\"";
                
                IRCClient.INSTNACE.init("");
            }
		});
	}
	
	private void remove() {
		if (selectedAlt != null) {
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
}
