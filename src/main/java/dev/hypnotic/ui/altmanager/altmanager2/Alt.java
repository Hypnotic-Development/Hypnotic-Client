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
package dev.hypnotic.ui.altmanager.altmanager2;

import java.util.Optional;
import java.util.UUID;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import dev.hypnotic.mixin.MinecraftClientAccessor;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import net.minecraft.client.util.Session.AccountType;
import net.minecraft.client.util.math.MatrixStack;

public class Alt {

	private YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(((MinecraftClientAccessor) MinecraftClient.getInstance()).getProxy(), "").createUserAuthentication(Agent.MINECRAFT);
	private String email, password, username;
	private int x, y, id;
	private boolean selected;
	private UUID uuid;

	public Alt(String username, String password, int id) {
		this.email = username;
		this.password = password;
		this.id = id;
	}
	
	public void login() throws AuthenticationException {
		auth.setUsername(email);
        auth.setPassword(password);
        auth.logIn();
        this.username = auth.getSelectedProfile().getName();
        this.uuid = auth.getSelectedProfile().getId();
		setSession(new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), Optional.empty(), Optional.empty(), AccountType.MOJANG));
		setUsername(auth.getSelectedProfile().getName());
	}
	
	protected void setSession(Session session) {
        ((MinecraftClientAccessor) MinecraftClient.getInstance()).setSession(session);
        MinecraftClient.getInstance().getSessionProperties().clear();
    }
	
	public void drawFace(MatrixStack matrices, int x, int y) {
		RenderUtils.drawFace(matrices, x, y, 4, RenderUtils.getPlayerSkin(uuid));
	}

	public boolean hoveredAlt(double mouseX, double mouseY) {
		return mouseX >= x && mouseX <= x + 230 && mouseY >= y && mouseY <= y + 30;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public YggdrasilUserAuthentication getAuth() {
		return auth;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getUsername() {
		return !username.equalsIgnoreCase("null") && !username.equalsIgnoreCase("") ? username : email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
}
