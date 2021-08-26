package badgamesinc.hypnotic.module.hud.elements;

import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.hud.HudModule;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.font.FontManager;
import badgamesinc.hypnotic.utils.math.MathUtils;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class BPS extends HudModule {

	public BPS() {
		super("Blocks/s Display", "Renders your blocks/s", (int) FontManager.roboto.getStringWidth("Ping 80") + 5, 1050, (int)FontManager.roboto.getStringWidth("Ping 80"), (int)FontManager.roboto.getStringHeight("Ping 80"));
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
		String bpsString = "Blocks/s " + ColorUtils.gray + MathUtils.round(ModuleManager.INSTANCE.getModule(badgamesinc.hypnotic.module.world.Timer.class).isEnabled() ? moveSpeed() * ModuleManager.INSTANCE.getModule(badgamesinc.hypnotic.module.world.Timer.class).speed.getValue() : moveSpeed(), 2);
		this.setDefaultX((int) FontManager.roboto.getStringWidth("TPS 20"));
		this.setDefaultY(scaledHeight - 18);
		this.setWidth((int) font.getStringWidth(bpsString));
		this.setHeight((int) font.getStringHeight(bpsString));
		font.drawWithShadow(matrices, bpsString, this.getX(), this.getY(), ColorUtils.getClientColorInt());
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
