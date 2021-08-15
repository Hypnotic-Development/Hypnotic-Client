package badgamesinc.hypnotic.module.render;

import java.awt.Color;

import badgamesinc.hypnotic.event.EventTarget;
import badgamesinc.hypnotic.event.events.EventRender3D;
import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.settings.settingtypes.BooleanSetting;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class ESP extends Mod {

	public BooleanSetting players = new BooleanSetting("Players", true);
	public BooleanSetting monsters = new BooleanSetting("Monsters", true);
	public BooleanSetting animals = new BooleanSetting("Animals", true);
	public BooleanSetting passives = new BooleanSetting("Passives", true);
	public BooleanSetting invisibles = new BooleanSetting("Invisibles", true);
	
	public ESP() {
		super("ESP", "Renders a box on players", Category.RENDER);
		addSettings(players, monsters, animals, passives, invisibles);
	}

	@EventTarget
	public void eventRender3D(EventRender3D event) {
		for (Entity entity : mc.world.getEntities()) {
			if (shouldRenderEntity(entity) && entity != mc.player) {
				Vec3d renderPos = RenderUtils.getEntityRenderPosition(entity, event.getTickDelta());
				RenderUtils.drawEntityBox(event.getMatrices(), entity, renderPos.x, renderPos.y, renderPos.z, getEntityColor(entity, 100));
			}
		}
	}
	
	public boolean shouldRenderEntity(Entity entity) {
		if (players.isEnabled() && entity instanceof PlayerEntity) return true;
		if (monsters.isEnabled() && entity instanceof Monster) return true;
		if (animals.isEnabled() && entity instanceof AnimalEntity) return true;
		if (passives.isEnabled() && entity instanceof PassiveEntity && !(entity instanceof AnimalEntity)) return true;
		if (invisibles.isEnabled() && entity.isInvisible()) return true;
		return false;
	}
	
	public Color getEntityColor(Entity entity, int alpha) {
		if (entity instanceof PlayerEntity) return new Color(255, 10, 100, alpha);
		if (entity instanceof Monster) return new Color(255, 255, 255, alpha);
		if (entity instanceof AnimalEntity) return new Color(30, 255, 30, alpha);
		if (entity instanceof PassiveEntity) return new Color(255, 255, 255, alpha);
		if (entity.isInvisible()) return new Color(255, 255, 255, alpha);
		return new Color(255, 255, 255);
	}
}
