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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import dev.hypnotic.settings.Setting;

public class NumberSetting extends Setting {


    double min, max, increment;
    @Expose
    @SerializedName("value")
    private double value;

    public NumberSetting(String name, double defaultValue, double minimum, double maximum, double increment) {
        this.name = name;
        this.value = defaultValue;
        this.min = minimum;
        this.max = maximum;
        this.increment = increment;
    }

    public static double clamp(double value, double min, double max) {
        value = Math.max(min, value);
        value = Math.min(max, value);
        return value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        value = clamp(value, this.min, this.max);
        value = Math.round(value * (1.0 / this.increment)) / (1.0 / this.increment);
        this.value = value;
    }

    public void increment(boolean positive) {
        if (positive) {
            setValue(getValue() + getIncrement());
        }
        if (!positive) {
            setValue(getValue() - getIncrement());
        }
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getIncrement() {
        return increment;
    }

    public void setIncrement(double increment) {
        this.increment = increment;
    }

}
