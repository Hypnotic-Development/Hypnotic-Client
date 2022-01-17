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

import java.util.Arrays;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import dev.hypnotic.settings.Setting;

public class ModeSetting extends Setting {

    @Expose
    @SerializedName("value")
    private String selected;
    private int index;
    private List<String> modes;

    public ModeSetting(String name, String defaultSelected, String... options) {
        this.name = name;
        this.modes = Arrays.asList(options);
        this.index = modes.indexOf(defaultSelected);
        this.selected = modes.get(index);
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
        index = modes.indexOf(selected);
    }

    public boolean is(String mode) {
        return mode.equals(selected);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        this.selected = modes.get(index);
    }

    public List<String> getModes() {
        return modes;
    }

    public void setModes(List<String> modes) {
        this.modes = modes;
    }

    public void cycle() {
        if (index < modes.size() - 1) {
            index++;
            selected = modes.get(index);
        } else if (index >= modes.size() - 1) {
            index = 0;
            selected = modes.get(0);
        }
    }
}
