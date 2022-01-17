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
package dev.hypnotic.mixin;

import net.minecraft.client.render.chunk.ChunkOcclusionDataBuilder;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.event.events.EventMarkChunkClosed;

@Mixin(ChunkOcclusionDataBuilder.class)
public class ChunkOcclusionDataBuilderMixin {

    @Inject(method = "markClosed", at = @At("HEAD"), cancellable = true)
    public void markClosed(BlockPos blockPos, CallbackInfo ci) {
        try {
            EventMarkChunkClosed eventMarkChunkClosed = new EventMarkChunkClosed();
            eventMarkChunkClosed.call();
            if (eventMarkChunkClosed.isCancelled())
                ci.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
