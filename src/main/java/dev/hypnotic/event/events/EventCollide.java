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
package dev.hypnotic.event.events;

import dev.hypnotic.event.Event;
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
