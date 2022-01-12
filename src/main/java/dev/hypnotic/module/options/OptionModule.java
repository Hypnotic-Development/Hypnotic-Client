package dev.hypnotic.module.options;

import dev.hypnotic.module.Mod;
import dev.hypnotic.settings.Setting;

public class OptionModule extends Mod {
	public OptionModule(Setting... settings) {
		super("Options", "Options for the client", null);
		addSettings(settings);
	}
}
