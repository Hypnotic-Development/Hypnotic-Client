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

import java.util.ArrayList;
import java.util.Comparator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import dev.hypnotic.config.ConfigSetting;
import dev.hypnotic.config.PositionsConfig;
import dev.hypnotic.event.EventManager;
import dev.hypnotic.settings.Setting;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class Mod {

	protected static MinecraftClient mc = MinecraftClient.getInstance();
	public String displayName;
	public String description;
	private Category category;
	public boolean expanded;
	private ArrayList<Setting> settings = new ArrayList<>();
	@Expose
    @SerializedName("key")
	public int keyCode;
	@Expose
    @SerializedName("enabled")
	public boolean enabled;
	@Expose
    @SerializedName("name")
	public String name;
	@Expose
    @SerializedName("settings")
    public ConfigSetting[] cfgSettings;
	public boolean wasFlag = false;
	public BooleanSetting visible = new BooleanSetting("Visible", true);
	public transient int index;
	public transient double animation = 0;
	public transient float offset = 0;
	
    private boolean binding;
	
	public Mod(String name, String description, Category category) {
		this.name = name;
		this.category = category;
		this.description = description;
		enabled = false;
		displayName = name;
		this.keyCode = 0;
		this.binding = false;
	}
	
	public void addSetting(Setting setting) {
		this.settings.sort(Comparator.comparing(s -> s == visible ? 1 : 0));
        getSettings().add(setting);
    }
	
	public ArrayList<Setting> getSettings() {
		this.settings.sort(Comparator.comparing(s -> s == visible ? 1 : 0));
        return settings;
    }

    public void addSettings(Setting... settings) {
    	this.settings.sort(Comparator.comparing(s -> s == visible ? 1 : 0));
        for (Setting setting : settings) {
            addSetting(setting);
        }
    }

	public String getDescription() {
		return description;
	}
	
	public boolean isExpanded() {
		return expanded;
	}
	
	public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public void toggle() {
		enabled = !enabled;
		mc.getSoundManager().play(PositionedSoundInstance.ambient(SoundEvents.UI_BUTTON_CLICK, enabled ? 1 : 0.8f, 1));
		if(enabled) {
			onEnable();
			EventManager.INSTANCE.register(this);
		} else {
			onDisable();
			EventManager.INSTANCE.unregister(this);
		}
	}
	
	public void toggleSilent() {
		enabled = !enabled;
		if(enabled) {
			onEnableSilent();
			EventManager.INSTANCE.register(this);
		} else {
			onDisableSilent();
			EventManager.INSTANCE.unregister(this);
		}
	}
	
	public void onTick() {}
	public void onTickDisabled() {}
	public void onMotion() {};
	
	public void onEnable() {
		EventManager.INSTANCE.register(this);
	}
	
	public void onDisable() {
		EventManager.INSTANCE.unregister(this);
	}
	
	public void onEnableSilent() {
		EventManager.INSTANCE.register(this);
	}
	
	public void onDisableSilent() {
		EventManager.INSTANCE.unregister(this);
	}
	
	public int getKey() {
		return keyCode;
	}

	public void setKey(int key) {
		this.keyCode = key;
		if(PositionsConfig.INSTANCE != null) {
			PositionsConfig.INSTANCE.save();
		}
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		
		if (enabled){
			if (EventManager.INSTANCE != null)
	            EventManager.INSTANCE.register(this);
        } else {
        	if (EventManager.INSTANCE != null)
        		EventManager.INSTANCE.unregister(this);
        }
        this.enabled = enabled;
        if (PositionsConfig.INSTANCE != null){
            PositionsConfig.INSTANCE.save();
        }
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	public boolean isBinding() {
		return binding;
	}

	public void setBinding(boolean binding) {
		this.binding = binding;
	}
}
