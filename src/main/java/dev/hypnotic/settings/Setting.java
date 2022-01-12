package dev.hypnotic.settings;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Setting {

    @Expose
    @SerializedName("name")
    public String name = " ";
    public String displayName = " ";
    public boolean focused;
    private boolean visible = true;
	public ArrayList<Setting> children = new ArrayList<>();
    
    public boolean isVisible() {
		return visible;
	}
    
    public void setVisible(boolean visible) {
		this.visible = visible;
	}
    
    public void addChild(Setting child) {
        children.add(child);
    }

    public void addChildren(Setting... children) {
        for (Setting child : children)
            addChild(child);
    }
}
