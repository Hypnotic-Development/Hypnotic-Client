package badgamesinc.hypnotic.module.render;

import org.lwjgl.glfw.GLFW;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventKeyPress;
import badgamesinc.hypnotic.event.events.EventPushOutOfBlocks;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.NumberSetting;
import badgamesinc.hypnotic.utils.RotationUtils;
import badgamesinc.hypnotic.utils.input.KeyUtils;
import badgamesinc.hypnotic.utils.math.Vec3;
import net.minecraft.client.option.Perspective;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class Freecam extends Mod {

	public NumberSetting speed = new NumberSetting("Speed", 1, 0, 10, 0.1);
	
	public final Vec3 pos = new Vec3();
    public final Vec3 prevPos = new Vec3();

    private Perspective perspective;

    public float yaw, pitch;
    public float prevYaw, prevPitch;

    private boolean forward, backward, right, left, up, down;
    public static PlayerEntity playerEntity;
    
	public Freecam() {
		super("Freecam", "Leave your body and explore", Category.RENDER);
		addSettings(speed);
	}
    
    @EventTarget
    public void pushOutOfBlocks(EventPushOutOfBlocks event) {
    	event.setCancelled(true);
    }
    
    @Override
    public void onTick() {
    	perspective = mc.options.getPerspective();
    	if (mc.cameraEntity.isInsideWall()) mc.getCameraEntity().noClip = true;
        if (!perspective.isFirstPerson()) mc.options.setPerspective(Perspective.FIRST_PERSON);

        if (mc.currentScreen != null) return;

        yaw = mc.player.getYaw();
        Vec3d forward = Vec3d.fromPolar(0, yaw);
        Vec3d right = Vec3d.fromPolar(0, yaw + 90);
        double velX = 0;
        double velY = 0;
        double velZ = 0;


        BlockPos crossHairPos;
        Vec3d crossHairPosition;

        if (mc.crosshairTarget instanceof EntityHitResult) {
            crossHairPos = ((EntityHitResult) mc.crosshairTarget).getEntity().getBlockPos();
            RotationUtils.rotate(RotationUtils.getYaw(crossHairPos), RotationUtils.getPitch(crossHairPos), 0, null);
        } else {
            crossHairPosition = mc.crosshairTarget.getPos();
            crossHairPos = ((BlockHitResult) mc.crosshairTarget).getBlockPos();

            if (!mc.world.getBlockState(crossHairPos).isAir()) {
                RotationUtils.rotate(RotationUtils.getYaw(crossHairPosition), RotationUtils.getPitch(crossHairPosition), 0, null);
            }
        }

        double s = 0.5;
        if (mc.options.keySprint.isPressed()) s = 1;

        boolean a = false;
        if (this.forward) {
            velX += forward.x * s * speed.getValue();
            velZ += forward.z * s * speed.getValue();
            a = true;
        }
        if (this.backward) {
            velX -= forward.x * s * speed.getValue();
            velZ -= forward.z * s * speed.getValue();
            a = true;
        }

        boolean b = false;
        if (this.right) {
            velX += right.x * s * speed.getValue();
            velZ += right.z * s * speed.getValue();
            b = true;
        }
        if (this.left) {
            velX -= right.x * s * speed.getValue();
            velZ -= right.z * s * speed.getValue();
            b = true;
        }

        if (a && b) {
            double diagonal = 1 / Math.sqrt(2);
            velX *= diagonal;
            velZ *= diagonal;
        }

        if (this.up) {
            velY += s * speed.getValue();
        }
        if (this.down) {
            velY -= s * speed.getValue();
        }

        prevPos.set(pos);
        pos.set(pos.x + velX, pos.y + velY, pos.z + velZ);
    	super.onTick();
    }

    @Override
    public void onEnable() {
    	yaw = mc.player.getYaw();
        pitch = mc.player.getPitch();

        perspective = mc.options.getPerspective();

        pos.set(mc.gameRenderer.getCamera().getPos());
        prevPos.set(mc.gameRenderer.getCamera().getPos());

        prevYaw = yaw;
        prevPitch = pitch;

        forward = false;
        backward = false;
        right = false;
        left = false;
        up = false;
        down = false;

        unpress();
        super.onEnable();
    }

    @EventTarget
    private void onKey(EventKeyPress event) {
        if (KeyUtils.isKeyPressed(GLFW.GLFW_KEY_F3)) return;

        boolean cancel = true;

        if (mc.options.keyForward.matchesKey(event.getKey(), 0) || mc.options.keyForward.matchesMouse(event.getKey())) {
            forward = event.getAction() != GLFW.GLFW_RELEASE;
        } else if (mc.options.keyBack.matchesKey(event.getKey(), 0) || mc.options.keyBack.matchesMouse(event.getKey())) {
            backward = event.getAction() != GLFW.GLFW_RELEASE;
        } else if (mc.options.keyRight.matchesKey(event.getKey(), 0) || mc.options.keyRight.matchesMouse(event.getKey())) {
            right = event.getAction() != GLFW.GLFW_RELEASE;
        } else if (mc.options.keyLeft.matchesKey(event.getKey(), 0) || mc.options.keyLeft.matchesMouse(event.getKey())) {
            left = event.getAction() != GLFW.GLFW_RELEASE;
        } else if (mc.options.keyJump.matchesKey(event.getKey(), 0) || mc.options.keyJump.matchesMouse(event.getKey())) {
            up = event.getAction() != GLFW.GLFW_RELEASE;
        } else if (mc.options.keySneak.matchesKey(event.getKey(), 0) || mc.options.keySneak.matchesMouse(event.getKey())) {
            down = event.getAction() != GLFW.GLFW_RELEASE;
        } else {
            cancel = false;
        }

        if (cancel) event.setCancelled(true);;
    }
    
    @Override
    public void onDisable() {
        super.onDisable();
        mc.options.setPerspective(perspective);
    }

    private void unpress() {
        mc.options.keyForward.setPressed(false);
        mc.options.keyBack.setPressed(false);
        mc.options.keyRight.setPressed(false);
        mc.options.keyLeft.setPressed(false);
        mc.options.keyJump.setPressed(false);
        mc.options.keySneak.setPressed(false);
    }
    
    public double getX(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPos.x, pos.x);
    }
    public double getY(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPos.y, pos.y);
    }
    public double getZ(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPos.z, pos.z);
    }

    public double getYaw(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevYaw, yaw);
    }
    public double getPitch(float tickDelta) {
        return MathHelper.lerp(tickDelta, prevPitch, pitch);
    }
}
