package dev.hypnotic.hypnotic_client.module.misc;

import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.settings.settingtypes.NumberSetting;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.ReflectionHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;

public class Timer extends Mod {

	public NumberSetting speed = new NumberSetting("Speed", 10, 0.1, 20, 0.1);
	
    public Timer() {
        super("Timer", "Speeds up the game time", Category.MISC);
        addSetting(speed);
    }

    @Override
    public void onTick() {
    	this.setDisplayName("Timer " + ColorUtils.gray + speed.getValue());
        ReflectionHelper.setPrivateValue(RenderTickCounter.class, ReflectionHelper.getPrivateValue(MinecraftClient.class, mc, "renderTickCounter", "field_1728"), 1000.0F / (float) speed.getValue() / 20, "tickTime", "field_1968");
    }

    @Override
    public void onDisable() {
        ReflectionHelper.setPrivateValue(RenderTickCounter.class, ReflectionHelper.getPrivateValue(MinecraftClient.class, mc, "renderTickCounter", "field_1728"), 1000.0F / 20.0F, "tickTime", "field_1968");
    }
}
