package badgamesinc.hypnotic.ui.altmanager.altmanager2;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;

import badgamesinc.hypnotic.mixin.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;

public class Alt {

	private YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(((MinecraftClientAccessor) MinecraftClient.getInstance()).getProxy(), "").createUserAuthentication(Agent.MINECRAFT);
	private String email, password, username;
	private int x, y;
	private boolean selected;
	
	public Alt(String username, String password) {
		this.email = username;
		this.password = password;
	}
	
	public void login() throws AuthenticationException {
		auth.setUsername(email);
        auth.setPassword(password);
        auth.logIn();
		setSession(new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), "mojang"));
		setUsername(auth.getSelectedProfile().getName());
	}
	
	protected void setSession(Session session) {
        ((MinecraftClientAccessor) MinecraftClient.getInstance()).setSession(session);
        MinecraftClient.getInstance().getSessionProperties().clear();
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
		return username;
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
}
