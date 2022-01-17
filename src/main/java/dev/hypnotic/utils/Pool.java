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

import java.util.ArrayDeque;
import java.util.Queue;

public class Pool<T> {
    private final Queue<T> items = new ArrayDeque<>();
    private final Producer<T> producer;

    public Pool(Producer<T> producer) {
        this.producer = producer;
    }

    public synchronized T get() {
        if (items.size() > 0) return items.poll();
        return producer.create();
    }

    public synchronized void free(T obj) {
        items.offer(obj);
    }
    
    public interface Producer<T> {
        T create();
    }
}
