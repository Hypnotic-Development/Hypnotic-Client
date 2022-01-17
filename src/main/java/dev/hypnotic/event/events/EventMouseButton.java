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

public class EventMouseButton extends Event {

    private int button;
    private ClickType clickType;

    public EventMouseButton(int button, ClickType clickType) {
        this.button = button;
        this.clickType = clickType;
    }

    public int getButton() {
        return button;
    }

    public ClickType getClickType() {
        return clickType;
    }

    public enum ClickType {
        IN_GAME,
        IN_MENU
    }
}
