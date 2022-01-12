package dev.hypnotic.utils.input;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventMouseButton;

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
