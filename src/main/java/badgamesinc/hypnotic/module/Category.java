package badgamesinc.hypnotic.module;

public enum Category {
	COMBAT("Combat"),
	MOVEMENT("Movement"),
	RENDER("Render"),
	WORLD("World"), 
	PLAYER("Player");
	
	public String name;
	
	Category(String name) {
		this.name = name;
	}
}
