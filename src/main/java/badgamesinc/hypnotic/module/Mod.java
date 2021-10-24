package badgamesinc.hypnotic.module;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.config.ConfigSetting;
import badgamesinc.hypnotic.event.EventManager;
import badgamesinc.hypnotic.settings.Setting;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;

public class Mod {

	protected static MinecraftClient mc = MinecraftClient.getInstance();
	public String displayName;
	private String description;
	private Category category;
	public boolean expanded;
	public ArrayList<Setting> settings = new ArrayList<>();
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
	
	private long currentMS = 0L;
	protected long lastMS = -1L;
	
	public float mSize = 0;
    public float lastSize = 0;
    
    public long start = 0;
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
	
	public void onLivingUpdate() {
		
	}
	
	public int getKey() {
		return keyCode;
	}

	public void setKey(int key) {
		this.keyCode = key;
		if(Hypnotic.INSTANCE.saveload != null) {
			Hypnotic.INSTANCE.saveload.save();
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
        if(Hypnotic.INSTANCE.saveload != null){
            Hypnotic.INSTANCE.saveload.save();
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

	public final void updateMS()
	{
		currentMS = System.currentTimeMillis();
	}
	
	public final void updateLastMS()
	{
		lastMS = System.currentTimeMillis();
	}
	
	public final boolean hasTimePassedM(long MS)
	{
		return currentMS >= lastMS + MS;
	}
	
	public final boolean hasTimePassedS(float speed)
	{
		return currentMS >= lastMS + (long)(1000 / speed);
	}
}
