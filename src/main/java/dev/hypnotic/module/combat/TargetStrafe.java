package dev.hypnotic.module.combat;

import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventRender3D;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.movement.Flight;
import dev.hypnotic.module.movement.Speed;
import dev.hypnotic.settings.settingtypes.BooleanSetting;
import dev.hypnotic.settings.settingtypes.NumberSetting;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.RotationUtils;
import dev.hypnotic.utils.player.PlayerUtils;
import dev.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public class TargetStrafe extends Mod {
    public static NumberSetting radius = new NumberSetting("Radius", 3, 0.5, 5, 0.1);
    public static BooleanSetting spacebar = new BooleanSetting("Spacebar", false);
    public static BooleanSetting thirdPerson = new BooleanSetting("Third Person", false);
    public BooleanSetting rainbow = new BooleanSetting("Rainbow", false);
    public TargetStrafe() {
        super("TargetStrafe", "Strafe around targets", Category.COMBAT);
        addSettings(radius, spacebar, rainbow, thirdPerson);
    }
    
    @Override
    public void onTick() {
    	if (thirdPerson.isEnabled() && ModuleManager.INSTANCE.getModule(Killaura.class).isEnabled()) {
    		if (Killaura.target != null && canStrafe() && (ModuleManager.INSTANCE.getModule(Speed.class).isEnabled() || ModuleManager.INSTANCE.getModule(Flight.class).isEnabled()))
    			mc.options.setPerspective(Perspective.THIRD_PERSON_BACK);
    		else
    			mc.options.setPerspective(Perspective.FIRST_PERSON);
    		
    	}
    	super.onTick();
    }

    @EventTarget
    public final void onRender3D(EventRender3D event) {
    	if (Killaura.target != null)
    		this.setDisplayName("TargetStrafe " + ColorUtils.gray + Killaura.target.getName().getString());
    	else
    		this.setDisplayName("TargetStrafe " + ColorUtils.gray + "None");
        if (ModuleManager.INSTANCE.getModule(Killaura.class).isEnabled() && Killaura.target != null) {
        	LivingEntity target = Killaura.target;
            drawCircle(event.getMatrices(), target, event.getTickDelta(), radius.getValue(), 0.1);
        }
    }

    public static void strafe(double moveSpeed, LivingEntity target,  boolean direction, boolean flight) {
    	try {
	        double direction1 = direction ? 1 : -1;
	        float[] rotations = RotationUtils.getRotations(target);
	        
	        if ((double) mc.player.distanceTo(target) <= radius.getValue()) {
	            PlayerUtils.setSpeed(moveSpeed, mc.player.getVelocity().y, rotations[0], direction1, 0.0D);
	        } else {
	            PlayerUtils.setSpeed(moveSpeed, mc.player.getVelocity().y, rotations[0], direction1, 1.0D);
	        }
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }

    public static boolean canStrafe() {
    	if (Killaura.target == null) return false;
        return Killaura.target != null && !Killaura.target.isDead() && (spacebar.isEnabled() ? ModuleManager.INSTANCE.getModule(Killaura.class).isEnabled() && Killaura.target != null && PlayerUtils.isMoving() && ModuleManager.INSTANCE.getModule(TargetStrafe.class).isEnabled() && (ModuleManager.INSTANCE.getModule(Flight.class).isEnabled() ? true : mc.options.keyJump.isPressed()) : ModuleManager.INSTANCE.getModule(Killaura.class).isEnabled() && Killaura.target != null && PlayerUtils.isMoving() && ModuleManager.INSTANCE.getModule(TargetStrafe.class).isEnabled());
    }

    private void drawCircle(MatrixStack matrices, Entity entity, float partialTicks, double rad, double height) {
    	boolean canSee = ModuleManager.INSTANCE.getModule(Speed.class).isEnabled() || ModuleManager.INSTANCE.getModule(Flight.class).isEnabled();
    	if (!canSee) {
    		return;
    	}
    	RenderUtils.drawCircle(matrices, entity.getPos(), partialTicks, rad, height, -1);
    }
}
