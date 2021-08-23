package badgamesinc.hypnotic.event.events;

import badgamesinc.hypnotic.event.Event;
import net.minecraft.client.util.math.MatrixStack;

public class EventRenderHand extends Event {

	private MatrixStack matrices;
	
	public EventRenderHand(MatrixStack matrices) {
		this.matrices = matrices;
	}
	
	public MatrixStack getMatrices() {
		return matrices;
	}
}
