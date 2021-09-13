package badgamesinc.hypnotic.event.events;

import badgamesinc.hypnotic.event.Event;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class EventCollide extends Event {

	public static class Block extends EventCollide {

		private Box boundingBox;
		private BlockPos pos;
		
		public Block(Box boundingBox, BlockPos pos) {
			this.boundingBox = boundingBox;
			this.pos = pos;
		}
		
		public Box getBoundingBox() {
			return boundingBox;
		}
		
		public void setBoundingBox(Box boundingBox) {
			this.boundingBox = boundingBox;
		}
		
		public BlockPos getPos() {
			return pos;
		}
		
		public void setPos(BlockPos pos) {
			this.pos = pos;
		}
	}
	
	public static class Entity extends EventCollide {
		
	}
}
