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
package dev.hypnotic.utils.world;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.WorldChunk;

import java.util.Iterator;

import dev.hypnotic.mixin.ClientPlayNetworkHandlerAccessor;

public class WorldChunkIterator implements Iterator<WorldChunk> {
	private static MinecraftClient mc = MinecraftClient.getInstance();
    private final int px, pz;
    private final int r;

    private int x, z;
    private WorldChunk chunk;

    public WorldChunkIterator() {
        px = ChunkSectionPos.getSectionCoord(mc.player.getBlockX());
        pz = ChunkSectionPos.getSectionCoord(mc.player.getBlockZ());
        r = getRenderDistance();

        x = px - r;
        z = pz - r;

        nextChunk();
    }

    private void nextChunk() {
        chunk = null;

        while (true) {
            z++;
            if (z > pz + r) {
                z = pz - r;
                x++;
            }

            if (x > px + r || z > pz + r) break;

            chunk = (WorldChunk) mc.world.getChunk(x, z, ChunkStatus.FULL, false);
            if (chunk != null) break;
        }
    }

    @Override
    public boolean hasNext() {
        return chunk != null;
    }

    @Override
    public WorldChunk next() {
        WorldChunk chunk = this.chunk;

        nextChunk();

        return chunk;
    }
    
    public static int getRenderDistance() {
        return Math.max(mc.options.getViewDistance().getValue(), ((ClientPlayNetworkHandlerAccessor) mc.getNetworkHandler()).getChunkLoadDistance());
    }
}
