package dev.hypnotic.hypnotic_client.module.options;

import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.settings.Setting;

public class OptionModule extends Mod {
	public OptionModule(Setting... settings) {
		super("Options", "Options for the client", null);
		addSettings(settings);
	}
}
