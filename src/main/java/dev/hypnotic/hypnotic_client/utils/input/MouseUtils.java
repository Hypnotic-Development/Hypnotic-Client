package dev.hypnotic.hypnotic_client.utils.input;

import dev.hypnotic.hypnotic_client.event.EventTarget;
import dev.hypnotic.hypnotic_client.event.events.EventMouseButton;

public class MouseUtils {

	private static int button;
	
	@EventTarget
	private void mouseEvent(EventMouseButton event) {
		button = event.getButton();
	}
	
	public static boolean isButtonDown(int mouseButton) {
		return mouseButton == button;
	}
}
