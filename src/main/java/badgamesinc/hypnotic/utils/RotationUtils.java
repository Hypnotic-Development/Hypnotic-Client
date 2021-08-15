package badgamesinc.hypnotic.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;

public class RotationUtils {

	public static float serverPitch;
	public static boolean isCustomPitch = false;
	public static boolean isCustomYaw = false;
	
	public static void setSilentPitch(float pitch) {
		RotationUtils.serverPitch = pitch;
		isCustomPitch = true;
	}
	
	@SuppressWarnings("resource")
	public static void setSilentYaw(float yaw) {
		MinecraftClient.getInstance().player.setBodyYaw(yaw);
		MinecraftClient.getInstance().player.setHeadYaw(yaw);
		isCustomYaw = true;
	}
	
	@SuppressWarnings("resource")
	public static void setSilentRotations(LivingEntity target, float yaw, float pitch) {
		MinecraftClient.getInstance().player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround((float) RotationUtils.getRotations(target)[0], (float) RotationUtils.getRotations(target)[1], MinecraftClient.getInstance().player.isOnGround()));
		RotationUtils.serverPitch = pitch;
		isCustomPitch = true;
		MinecraftClient.getInstance().player.setBodyYaw(yaw);
		MinecraftClient.getInstance().player.setHeadYaw(yaw);
		isCustomYaw = true;
	}
	
	public static void resetPitch() {
		isCustomPitch = false;
	}
	
	public static void resetYaw() {
		isCustomYaw = false;
	}
	
	@SuppressWarnings("resource")
	public static float[] getRotationFromPosition(double x, double z, double y) {
	    double xDiff = x - MinecraftClient.getInstance().player.getX();
	    double zDiff = z - MinecraftClient.getInstance().player.getZ();
	    double yDiff = y - MinecraftClient.getInstance().player.getY() - 1.2;
	
	    double dist = MathHelper.sqrt((float) (xDiff * xDiff + zDiff * zDiff));
	    float yaw = (float) (Math.atan2(zDiff, xDiff) * 180.0D / 3.141592653589793D) - 90.0F;
	    float pitch = (float) -(Math.atan2(yDiff, dist) * 180.0D / 3.141592653589793D);
	    return new float[]{yaw, pitch};
	}

	public static float[] getRotations(LivingEntity ent) {
        double x = ent.getX();
        double z = ent.getZ();
        double y = ent.getY() + ent.getStandingEyeHeight() / 2.0F;
        return getRotationFromPosition(x, z, y);
    }
}
