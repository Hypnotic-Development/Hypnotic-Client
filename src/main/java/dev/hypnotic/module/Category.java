package dev.hypnotic.module;

import java.awt.Color;

public enum Category {
	COMBAT("Combat", new Color(255, 72, 72), "a"),
	MOVEMENT("Movement", new Color(0, 193, 169), "b"),
	RENDER("Render", new Color(199, 78, 255), "g"),
	MISC("Misc", new Color(20, 255, 50), "e"), 
	PLAYER("Player", new Color(255, 147, 0), "c"),
	EXPLOIT("Exploit", new Color(255, 110, 215), "d"),
	SCRIPT("Scripts", new Color(255, 127, 122), "h");
	
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
