package badgamesinc.hypnotic.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;

import java.util.Comparator;

import badgamesinc.hypnotic.module.Mod;

public class Comparators {
	
	public static MinecraftClient mc = MinecraftClient.getInstance();
    public static final EntityDistance entityDistance = new EntityDistance();
    public static final BlockDistance blockDistance = new BlockDistance();
    public static final ModuleNameLength moduleStrLength = new ModuleNameLength();
    public static final ModuleAlphabetic moduleAlphabetic = new ModuleAlphabetic();

    private static class EntityDistance implements Comparator<Entity> {
        @Override
        public int compare(Entity p1, Entity p2) {
            final double one = Math.sqrt(mc.player.distanceTo(p1));
            final double two = Math.sqrt(mc.player.distanceTo(p2));
            return Double.compare(one, two);
        }
    }

    private static class BlockDistance implements Comparator<BlockPos> {
        @Override
        public int compare(BlockPos pos1, BlockPos pos2) {
            final double one = Math.sqrt(mc.player.squaredDistanceTo(pos1.getX() + 0.5, pos1.getY() + 0.5, pos1.getZ() + 0.5));
            final double two = Math.sqrt(mc.player.squaredDistanceTo(pos2.getX() + 0.5, pos2.getY() + 0.5, pos2.getZ() + 0.5));
            return Double.compare(one, two);
        }
    }

	private static class ModuleNameLength implements Comparator<Mod> {
        @Override
		public int compare(Mod h1, Mod h2) {
			final double h1Width = mc.textRenderer.getWidth(h1.getDisplayName());
			final double h2Width = mc.textRenderer.getWidth(h2.getDisplayName());
			return Double.compare(h2Width, h1Width);
		}
	}

    private static class ModuleAlphabetic implements Comparator<Module> {
        @Override
        public int compare(Module h1, Module h2) {
            return h1.getName().compareTo(h2.getName());
        }
    }
}
