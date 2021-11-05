package badgamesinc.hypnotic.module.options;

import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.Setting;

public class OptionModule extends Mod {
	public OptionModule(Setting... settings) {
		super("Options", "Options for the client", null);
		addSettings(settings);
	}
}
