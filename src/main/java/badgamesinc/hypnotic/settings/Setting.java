package badgamesinc.hypnotic.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Setting {

    @Expose
    @SerializedName("name")
    public String name = " ";
    public String displayName = " ";
    public boolean focused;
    private boolean visible = true;
    
    public boolean isVisible() {
		return visible;
	}
    
    public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
