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
package dev.hypnotic.module;

import java.awt.Color;

public enum Category {
	COMBAT("Combat", new Color(255, 72, 72), "a"),
	MOVEMENT("Movement", new Color(0, 193, 169), "b"),
	RENDER("Render", new Color(199, 78, 255), "g"),
	MISC("Misc", new Color(20, 255, 50), "e"), 
	PLAYER("Player", new Color(255, 147, 0), "c"),
	EXPLOIT("Exploit", new Color(255, 110, 215), "d"),
	SCRIPT("Scripts", new Color(255, 127, 122), "i");
	
	public String name;
	public Color color;
	public String icon;
	public int moduleIndex;
	
	Category(String name, Color color, String icon) {
		this.name = name;
		this.color = color;
		this.icon = icon;
	}
}
