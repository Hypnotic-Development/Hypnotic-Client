package badgamesinc.hypnotic.event.events;

import badgamesinc.hypnotic.event.Event;
import net.minecraft.client.util.math.MatrixStack;

public class EventRenderGUI extends Event {

	private MatrixStack matrices;
	private float partialTicks;
	
	public EventRenderGUI(MatrixStack matrices, float partialTicks) {
		this.matrices = matrices;
		this.partialTicks = partialTicks;
	}

	public MatrixStack getMatrices() {
		return matrices;
	}
	
	public float getPartialTicks() {
		return partialTicks;
	}
}
