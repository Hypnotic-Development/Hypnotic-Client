package badgamesinc.hypnotic.module.world;

import badgamesinc.hypnotic.module.Category;
import badgamesinc.hypnotic.module.Mod;
import badgamesinc.hypnotic.utils.mixin.IHorseBaseEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.HorseBaseEntity;

public class EntityControl extends Mod {

	public EntityControl() {
		super("EntityControl", "no consent required", Category.WORLD);
	}
	
	@Override
    public void onDisable() {
        if (mc.world.getEntities() == null) return;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof HorseBaseEntity) ((IHorseBaseEntity) entity).setSaddled(false);
        }
    }

    @Override
	public void onTick() {
        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof HorseBaseEntity) ((IHorseBaseEntity) entity).setSaddled(true);
        }

//        if (maxJump.get()) ((ClientPlayerEntityAccessor) mc.player).setMountJumpStrength(1);
    }
}
