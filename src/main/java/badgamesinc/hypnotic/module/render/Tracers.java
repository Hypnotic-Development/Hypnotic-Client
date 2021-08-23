package badgamesinc.hypnotic.module.render;

import java.awt.Color;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import badgamesinc.hypnotic.config.friends.FriendManager;
import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventRender3D;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;

public class Tracers extends Mod {

	public BooleanSetting players = new BooleanSetting("Players", true);
	public BooleanSetting monsters = new BooleanSetting("Monsters", true);
	public BooleanSetting animals = new BooleanSetting("Animals", true);
	public BooleanSetting passives = new BooleanSetting("Passives", true);
	public BooleanSetting invisibles = new BooleanSetting("Invisibles", true);
	
	public Tracers() {
		super("Tracers", "draw cool lines", Category.RENDER);
		addSettings(players, monsters, animals, passives, invisibles);
	}

	@EventTarget
	public void render3d(EventRender3D event) {
		for (Entity entity : StreamSupport.stream(mc.world.getEntities().spliterator(), false).sorted(Comparator
                .comparingDouble(value -> -value.distanceTo(mc.player))).collect(Collectors.toList())) {
            if (entity.squaredDistanceTo(mc.player) > 4096) continue;
            double dc = entity.squaredDistanceTo(mc.player) / 4096;
            dc = Math.abs(1 - dc);
            if (entity.getUuid().equals(mc.player.getUuid())) continue;
            Color c = getEntityColor(entity);

            if (shouldRenderEntity(entity)) {

                    RenderUtils.line(RenderUtils.center(), entity.getPos().add(0, 0, 0), c, event.getMatrices());
                    RenderUtils.line(entity.getPos(), entity.getPos().add(0, entity.getStandingEyeHeight(), 0), c, event.getMatrices());
            }
        }
	}
	
	Vec2f getPY(Vec3d target1) {
        Camera c = mc.gameRenderer.getCamera();
        double vec = 57.2957763671875;
        Vec3d target = target1.subtract(c.getPos());
        double square = Math.sqrt(target.x * target.x + target.z * target.z);
        float pitch = MathHelper.wrapDegrees((float) (-(MathHelper.atan2(target.y, square) * vec)));
        float yaw = MathHelper.wrapDegrees((float) (MathHelper.atan2(target.z, target.x) * vec) - 90.0F);
        return new Vec2f(pitch, yaw);
    }
	
	public boolean shouldRenderEntity(Entity entity) {
		if (players.isEnabled() && entity instanceof PlayerEntity) return true;
		if (monsters.isEnabled() && entity instanceof Monster) return true;
		if (animals.isEnabled() && entity instanceof AnimalEntity) return true;
		if (passives.isEnabled() && entity instanceof PassiveEntity && !(entity instanceof AnimalEntity)) return true;
		if (invisibles.isEnabled() && entity.isInvisible()) return true;
		return false;
	}
	
	public Color getEntityColor(Entity entity) {
		if (entity instanceof LivingEntity && FriendManager.INSTANCE.isFriend((LivingEntity) entity)) return new Color(ColorUtils.rainbow(4, 0.6f, 1));
		if (entity instanceof PlayerEntity) return new Color(255, 10, 100, 255);
		if (entity instanceof Monster) return new Color(255, 255, 255, 255);
		if (entity instanceof AnimalEntity) return new Color(30, 255, 30, 255);
		if (entity instanceof PassiveEntity) return new Color(255, 255, 255, 255);
		if (entity.isInvisible()) return new Color(255, 255, 255, 255);
		
		return new Color(255, 255, 255);
	}
}
