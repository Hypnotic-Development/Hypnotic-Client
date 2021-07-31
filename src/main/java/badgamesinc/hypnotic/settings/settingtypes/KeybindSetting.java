package badgamesinc.hypnotic.settings.settingtypes;

import badgamesinc.hypnotic.settings.Setting;

public class KeybindSetting extends Setting {

    private int code;

    public KeybindSetting(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
        return code == -1 ? 0 : code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
