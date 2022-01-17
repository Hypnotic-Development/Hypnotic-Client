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
package dev.hypnotic.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import java.util.Comparator;

import dev.hypnotic.module.Mod;

public class Comparators {
	
	public static MinecraftClient mc = MinecraftClient.getInstance();
    public static final EntityDistance entityDistance = new EntityDistance();
    public static final BlockDistance blockDistance = new BlockDistance();
    public static final ModuleNameLength moduleStrLength = new ModuleNameLength();
    public static final ModuleAlphabetic moduleAlphabetic = new ModuleAlphabetic();

    private static class EntityDistance implements Comparator<Entity> {
        @Override
        public int compare(Entity p1, Entity p2) {
            final double one = Math.sqrt(mc.player.distanceTo(p1));
            final double two = Math.sqrt(mc.player.distanceTo(p2));
            return Double.compare(one, two);
        }
    }

    private static class BlockDistance implements Comparator<BlockPos> {
        @Override
        public int compare(BlockPos pos1, BlockPos pos2) {
            final double one = Math.sqrt(mc.player.squaredDistanceTo(pos1.getX() + 0.5, pos1.getY() + 0.5, pos1.getZ() + 0.5));
            final double two = Math.sqrt(mc.player.squaredDistanceTo(pos2.getX() + 0.5, pos2.getY() + 0.5, pos2.getZ() + 0.5));
            return Double.compare(one, two);
        }
    }

	private static class ModuleNameLength implements Comparator<Mod> {
        @Override
		public int compare(Mod h1, Mod h2) {
			final double h1Width = mc.textRenderer.getWidth(h1.getDisplayName());
			final double h2Width = mc.textRenderer.getWidth(h2.getDisplayName());
			return Double.compare(h2Width, h1Width);
		}
	}

    private static class ModuleAlphabetic implements Comparator<Module> {
        @Override
        public int compare(Module h1, Module h2) {
            return h1.getName().compareTo(h2.getName());
        }
    }
}
