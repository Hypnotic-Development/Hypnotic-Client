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

import net.minecraft.util.math.Direction;

public class Dir {
    public static final byte UP = 1 << 1;
    public static final byte DOWN = 1 << 2;
    public static final byte NORTH = 1 << 3;
    public static final byte SOUTH = 1 << 4;
    public static final byte WEST = 1 << 5;
    public static final byte EAST = 1 << 6;

    public static byte get(Direction dir) {
        return switch (dir) {
            case UP    -> UP;
            case DOWN  -> DOWN;
            case NORTH -> NORTH;
            case SOUTH -> SOUTH;
            case WEST  -> WEST;
            case EAST  -> EAST;
        };
    }

    public static boolean is(int dir, byte idk) {
        return (dir & idk) == idk;
    }

    public static boolean isNot(int dir, byte idk) {
        return (dir & idk) != idk;
    }
}
