package badgamesinc.hypnotic.module.misc;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordRPCModule extends Mod {

	private final String ID = "899900778519601223";
	public DiscordRPC discordRPC;
	public DiscordRichPresence presence;
	public Thread updateThread;
	public static String status = "";
	
	public DiscordRPCModule() {
		super("DiscordRPC", "Displays that you are playing Hypnotic on Discord", Category.MISC);
		this.setEnabled(true);
	}
	
	@Override
	public void onEnable() {
		discordRPC = DiscordRPC.INSTANCE;
		DiscordEventHandlers handlers = new DiscordEventHandlers();
		handlers.ready = (user) -> Hypnotic.LOGGER.info("Discord handlers ready");
		discordRPC.Discord_Initialize(ID, handlers, true, "");
		presence = new DiscordRichPresence();
		presence.startTimestamp = System.currentTimeMillis() / 1000;
		presence.largeImageKey = "hypnotic_icon";
        presence.largeImageText = Hypnotic.fullName;
        presence.details = Hypnotic.fullName;
		presence.state = status;
		discordRPC.Discord_UpdatePresence(presence);
		
		updateThread = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				discordRPC.Discord_RunCallbacks();
				presence.details = Hypnotic.fullName;
				presence.state = status;
				discordRPC.Discord_UpdatePresence(presence);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					discordRPC.Discord_Shutdown();
					break;
				}
			}
		}, "RPC-Callback-Handler");
		updateThread.start();
		super.onEnable();
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		if (enabled) {
			discordRPC = DiscordRPC.INSTANCE;
			DiscordEventHandlers handlers = new DiscordEventHandlers();
			handlers.ready = (user) -> Hypnotic.LOGGER.info("Discord handlers ready");
			discordRPC.Discord_Initialize(ID, handlers, true, "");
			presence = new DiscordRichPresence();
			presence.startTimestamp = System.currentTimeMillis() / 1000;
			presence.largeImageKey = "hypnotic_icon";
	        presence.largeImageText = Hypnotic.fullName;
	        presence.details = Hypnotic.fullName;
			presence.state = status;
			discordRPC.Discord_UpdatePresence(presence);
			
			updateThread = new Thread(() -> {
				while (!Thread.currentThread().isInterrupted()) {
					discordRPC.Discord_RunCallbacks();
					presence.details = Hypnotic.fullName;
					presence.state = status;
					discordRPC.Discord_UpdatePresence(presence);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						discordRPC.Discord_Shutdown();
						break;
					}
				}
			}, "RPC-Callback-Handler");
			updateThread.start();
		} else {
			if (updateThread != null && !updateThread.isInterrupted()) {
				updateThread.interrupt();
			}
			if (discordRPC != null) {
				try {
					discordRPC.Discord_Shutdown();
					discordRPC = null;
					presence = null;
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
		super.setEnabled(enabled);
	}
	
	@Override
	public void onTick() {
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		if (updateThread != null && !updateThread.isInterrupted()) {
			updateThread.interrupt();
		}
		if (discordRPC != null) {
			try {
				discordRPC.Discord_Shutdown();
				discordRPC = null;
				presence = null;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		super.onDisable();
	}
}
