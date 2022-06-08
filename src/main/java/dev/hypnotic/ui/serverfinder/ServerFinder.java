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
package dev.hypnotic.ui.serverfinder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;

import org.apache.logging.log4j.Level;

import dev.hypnotic.Hypnotic;
import dev.hypnotic.ui.Button;
import dev.hypnotic.ui.HypnoticScreen;
import dev.hypnotic.ui.TextBox;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.MultiplayerServerListPinger;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.util.math.MatrixStack;

public class ServerFinder extends HypnoticScreen {

	private String findMode = "Similar IP";
	private String saveMode = "Add servers";
	private Button findModeButton;
	private Button saveModeButton;
	private Button startButton;
	private TextBox ipBox;
	private int findModeIndex = 0;
	private int saveModeIndex = 0;
	private String[] findModes = new String[] {"Similar IP"};
	private String[] saveModes = new String[] {"Add servers", "Save to file", "Both"};
	private int valid = 0;
	private MultiplayerScreen multiplayerScreen;
	private int threads = 0;
	
	public ServerFinder(MultiplayerScreen multiplayerScreen) {
		this.multiplayerScreen = multiplayerScreen;
	}
	
	@Override
	protected void init() {
		findModeButton = new Button("Find Mode: " + findMode, 10, 10, 100, 20, true, this::cycleFindMode);
		saveModeButton = new Button("Save Mode: " + saveMode, 10, 30, 100, 20, true, this::cycleSaveMode);
		startButton = new Button("Start", 10, 50, 100, 20, true, this::start);
		ipBox = new TextBox(25, 100, 150, 20, "IP");
		ipBox.setMaxLength(30);
		this.addButton(findModeButton);
		this.addButton(saveModeButton);
		this.addButton(startButton);
		super.init();
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		findModeButton.render(matrices, mouseX, mouseY, delta);
		saveModeButton.render(matrices, mouseX, mouseY, delta);
		if (findMode.equalsIgnoreCase("Similar IP")) ipBox.renderButton(matrices, mouseX, mouseY, delta);
		startButton.render(matrices, mouseX, mouseY, delta);
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public void tick() {
		ipBox.tick();
	}
	
	private void cycleFindMode() {
		if (findModeIndex < findModes.length - 1) {
			findModeIndex++;
		} else {
			findModeIndex = 0;
		}
		findMode = findModes[findModeIndex];
		findModeButton.setText("Find Mode: " + findMode);
	}
	
	private void cycleSaveMode() {
		if (saveModeIndex < saveModes.length - 1) {
			saveModeIndex++;
		} else {
			saveModeIndex = 0;
		}
		saveMode = saveModes[saveModeIndex];
		saveModeButton.setText("Save Mode: " + saveMode);
	}
	
	private void start() {
		multiplayerScreen.getServerList().add(new ServerInfo("Server finder ", "139.99.125.158:25569", false), false);
		threads = 0;
		try {
			findServers();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		ipBox.mouseClicked(mouseX, mouseY, button);
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		return super.mouseReleased(mouseX, mouseY, button);
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers) {
		ipBox.charTyped(chr, modifiers);
		return super.charTyped(chr, modifiers);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		ipBox.keyPressed(keyCode, scanCode, modifiers);
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		ipBox.keyReleased(keyCode, scanCode, modifiers);
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	
	private void findServers() throws IOException {
		InetAddress addr = InetAddress.getByName(ipBox.getText().split(":")[0].trim());
		int[] ipParts = new int[4];
		
		for(int i = 0; i < 4; i++) {
			ipParts[i] = addr.getAddress()[i] & 0xff;
		}
		
		int[] variances = {0, 1, -1, 2, -2, 3, -3};
		for(int variance : variances) {
			for(int i2 = 0; i2 <= 500; i2++) {
				int[] ipParts2 = ipParts.clone();
				ipParts2[2] = ipParts[2] + variance & 0xff;
				ipParts2[3] = i2;
				String ip = ipParts2[0] + "." + ipParts2[1] + "." + ipParts2[2] + "." + ipParts2[3];
				
				if (threads > 150) break;
				
				this.pingServer(ip);
				threads++;
			}
		}
	}
	
	private void saveServer(String ip) {
		try {
			if (saveMode.equalsIgnoreCase("Add servers") || saveMode.equalsIgnoreCase("Both")) {
				multiplayerScreen.getServerList().add(new ServerInfo("Server finder " + valid, ip, false), false);
				multiplayerScreen.getServerList().saveFile();
			}
			if (saveMode.equalsIgnoreCase("Save to file") || saveMode.equalsIgnoreCase("Both")) {
				File serversFile = new File(Hypnotic.hypnoticDir + "/ServerFinder.txt");
				if (!serversFile.exists()) {
					serversFile.createNewFile();
				}
				BufferedWriter writer = new BufferedWriter(new FileWriter(serversFile));
				writer.write("Server " + valid + " " + ip + "/n");
				writer.close();
			}
			Hypnotic.LOGGER.log(Level.INFO, "Saved " + ip + " as Server " + valid);
			valid++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void pingServer(String ip) {
		if (!alreadyAdded(ip)) {
			new Thread(() -> {
				MultiplayerServerListPinger pinger = new MultiplayerServerListPinger();
				try {
					pinger.add(new ServerInfo("", ip, false), () -> {});
					saveServer(ip);
				} catch(Exception e) {
					System.out.println("Unable to ping " + ip);
				}
				pinger.cancel();
			}).start();
		}
	}
	
	private boolean alreadyAdded(String ip) {
		for(int i = 0; i < multiplayerScreen.getServerList().size(); i++)
			if(multiplayerScreen.getServerList().get(i).address.equals(ip))
				return true;
			
		return false;
	}
}
