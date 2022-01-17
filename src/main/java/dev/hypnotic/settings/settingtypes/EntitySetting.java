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
package dev.hypnotic.settings.settingtypes;

import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import dev.hypnotic.settings.Setting;
import net.minecraft.entity.Entity;

public class EntitySetting extends Setting {

	@Expose
    @SerializedName("value")
    private boolean selected;
    private ArrayList<Setting> children = new ArrayList<>();

    public EntitySetting(String name, boolean selected, Entity entity) {
        this.name = name;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void toggle() {
        setSelected(!isSelected());
    }

    public ArrayList<Setting> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Setting> children) {
        this.children = children;
    }

    public void addChild(Setting child) {
        children.add(child);
    }

    public void addChildren(Setting... children) {
        for (Setting child : children)
            addChild(child);
    }
}
