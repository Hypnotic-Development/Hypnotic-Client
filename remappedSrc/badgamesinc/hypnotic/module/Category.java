package badgamesinc.hypnotic.module;

import java.awt.Color;

public enum Category {
	COMBAT("Combat", new Color(255, 72, 72)),
	MOVEMENT("Movement", new Color(0, 193, 169)),
	RENDER("Render", new Color(199, 78, 255)),
	WORLD("World", new Color(20, 255, 50)), 
	PLAYER("Player", new Color(255, 147, 0));
	
	public String name;
	public Color color;
	public int moduleIndex;
	
	Category(String name, Color color) {
		this.name = name;
		this.color = color;
	}
}
