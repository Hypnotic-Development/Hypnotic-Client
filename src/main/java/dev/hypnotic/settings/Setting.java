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
