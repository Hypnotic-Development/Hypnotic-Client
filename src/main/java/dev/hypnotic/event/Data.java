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

import java.lang.reflect.Method;

/**
 * Created by Hexeption on 18/12/2016.
 */
public class Data {

    public final Object source;

    public final Method target;

    public final byte priority;

    public Data(Object source, Method target, byte priority) {

        this.source = source;
        this.target = target;
        this.priority = priority;
    }

}
