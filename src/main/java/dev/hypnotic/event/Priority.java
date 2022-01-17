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
package dev.hypnotic.event;

/**
 * Created by Hexeption on 18/12/2016.
 */
public class Priority{

    public static final byte FIRST = 0, SECOND = 1, THIRD = 2, FOURTH = 3, FIFTH = 4;

    public static final byte[] VALUE_ARRAY;

    static {
        VALUE_ARRAY = new byte[]{0, 1, 2, 3, 4};
    }

}
