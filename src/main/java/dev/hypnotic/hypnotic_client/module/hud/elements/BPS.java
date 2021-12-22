package dev.hypnotic.hypnotic_client.module.hud.elements;

import dev.hypnotic.hypnotic_client.mixin.MinecraftClientAccessor;
import dev.hypnotic.hypnotic_client.mixin.RenderTickCounterAccessor;
import dev.hypnotic.hypnotic_client.module.hud.HudModule;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ColorSetting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ModeSetting;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.font.FontManager;
import dev.hypnotic.hypnotic_client.utils.math.MathUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BPS extends HudModule {

	public ColorSetting color = new ColorSetting("Color", ColorUtils.pingle);
	public ModeSetting mode = new ModeSetting("Mode", "Blocks/s", "Blocks/s", "Kilometers/h");
	
	public BPS() {
		super("Blocks/s Display", "Renders your blocks/s", (int) FontManager.roboto.getStringWidth("Ping 80") + 5, 1050, (int)FontManager.roboto.getStringWidth("Ping 80"), (int)FontManager.roboto.getStringHeight("Ping 80"));
		addSettings(color, mode);
	}
	
	@Override
	public void onEnable() {
		this.setDefaultX((int) FontManager.roboto.getStringWidth("Ping 80"));
		this.setDefaultY(mc.getWindow().getScaledHeight() - 20);
		if (this.getX() > mc.getWindow().getScaledWidth()) this.setX(this.getDefaultX());
		if (this.getY() > mc.getWindow().getScaledHeight()) this.setY(this.getDefaultY());
		super.onEnable();
	}

	@Override
	public void render(MatrixStack matrices, int scaledWidth, int scaledHeight, float partialTicks) {
		String bpsString = "Blocks/s " + ColorUtils.gray + MathUtils.round(moveSpeed() * (50f / ((RenderTickCounterAccessor)((MinecraftClientAccessor)mc).getRenderTickCounter()).getTickTime()), 2);
		if (mode.is("Kilometers/h")) bpsString = "Kilometers/h " + ColorUtils.gray + MathUtils.round(moveSpeed() * (50f / ((RenderTickCounterAccessor)((MinecraftClientAccessor)mc).getRenderTickCounter()).getTickTime())*3.6f, 2);
		this.setDefaultX((int) FontManager.roboto.getStringWidth("TPS 20"));
		this.setDefaultY(scaledHeight - 18);
		this.setWidth((int) font.getStringWidth(bpsString));
		this.setHeight((int) font.getStringHeight(bpsString));
		font.drawWithShadow(matrices, bpsString, this.getX(), this.getY(), color.getRGB());
		super.render(matrices, scaledWidth, scaledHeight, partialTicks);
	}
	
	
	private double moveSpeed() {
        Vec3d move = new Vec3d(mc.player.getX() - mc.player.prevX, 0, mc.player.getZ() - mc.player.prevZ).multiply(20);

        return Math.abs(length2D(move)) ;
    }
	
	public double length2D(Vec3d vec3d) {
        return MathHelper.sqrt((float)(vec3d.x * vec3d.x + vec3d.z * vec3d.z));
    }
}
