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
import net.minecraft.entity.Entity;

public class EventEntity extends Event {
    private final Entity entity;

    public EventEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public static class Spawn extends EventEntity {
        public Spawn(Entity entity) {
            super(entity);
        }
    }

    public static class Remove extends EventEntity {
        public Remove(Entity entity) {
            super(entity);
        }
    }
}
